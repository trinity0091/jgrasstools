/*
 * JGrass - Free Open Source Java GIS http://www.jgrass.org 
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

// THIS FILE HAS TO BE RUN FROM THE PROJECT ROOT LIKE:
// groovy extras/deploy/deploylibs.groovy 



def javaHome = System.getProperty("java.home");
def javaHomeFile = new File(javaHome);
def toolsJar = new File(javaHomeFile.getParentFile(), "lib/tools.jar");
if(!toolsJar.exists()){
    println "The JAVA_HOME variable has to be set and point to a JDK";
}


// copy also source jars?
def alsoSources = false;
// copy also javadoc jars?
def alsojavaDocs = false;
// your maven repo path
def mvnRepo = System.getProperty("user.home");
def repo = "${mvnRepo}/.m2/repository/"
println "Using maven repo in: ${repo}";
def repoFile = new File(repo);
if(!repoFile.exists()){
    println "The maven repo is not in the default location, please set it by hand in the script";
    System.exit(0);
}
// path to which to copy them
def copyPath = "./extras/deploy/libs/"
def copyPathFile = new File(copyPath);
if(!copyPathFile.exists()){
    copyPathFile.mkdir();
}

// copy jgrasstools modules
JGTMODULESCOPY: {
    def modulesFolder = "./extras/deploy/modules/"
    def modulesFolderFile = new File(modulesFolder);
    if(!modulesFolderFile.exists()){
        modulesFolderFile.mkdir();
    }
    // jgrassgears
    def jarFiles = new File("./jgrassgears/target").listFiles(new FilenameFilter() {  
        public boolean accept(File f, String filename) {  
            return filename.startsWith("jgrassgears-") && filename.endsWith(".jar")  
        }  
    });
    jarFiles.each{ 
        def copyToFile = new File(modulesFolderFile.absolutePath, it.name).absolutePath;
       (new AntBuilder()).copy( file : it , tofile : copyToFile )
    }
    // hortonmachine
    jarFiles = new File("./hortonmachine/target").listFiles(new FilenameFilter() {  
        public boolean accept(File f, String filename) {  
            return filename.startsWith("hortonmachine-") && filename.endsWith(".jar")  
        }  
    });
    jarFiles.each{ 
        def copyToFile = new File(modulesFolderFile.absolutePath, it.name).absolutePath;
       (new AntBuilder()).copy( file : it , tofile : copyToFile )
    }

    // tools.jar
    def newToolsJar = new File(copyPathFile, "tools.jar");
    new AntBuilder().copy ( file : toolsJar.absolutePath , tofile : newToolsJar.absolutePath )
}

// launch maven deps tree
def mvnCommand = "mvn dependency:tree";
def proc = mvnCommand.execute();
proc.waitFor();

def output = proc.in.text;
// clean out what we need
def lista=[];
def lines = output.split("\n");
def depsList = [];
def startIndex = -1;
def endIndex = -1;
for (int i = 0; i < lines.size(); i++){
    def line = lines[i];
    if(line.startsWith("[INFO] [dependency:tree]")){
        startIndex = i + 1;
        continue;
    }
    if(startIndex != -1 && line.startsWith("[INFO] ----------")){
        endIndex = i - 1
        break;
    }
    if(startIndex == -1){
        continue;
    }

    lista << line;
}

println "Search for:"
lista.each{
    println it
}

println "---------------------------------------"
println "---------------------------------------"

// find jars
def basedir = new File(repo)
def files = [];
basedir.eachDirRecurse () { dir ->
    dir.eachFileMatch(~/.*.jar/) { file ->  
         files << file
    }  
} 

// extract name pattern and version
def fileBeginList = []
def versionList = []
lista.each{
   def split = it.split(":");
   fileBeginList << split[1]
   versionList << split[3]
   println "${split[1]} --- ${split[3]}"
}

def finalList = [];
// extract right jars paths from list
for (it in files){
    def name = it.getName()
    def path = it.getAbsolutePath()
    
    if(!alsoSources && name.matches(".*sources.*")){
        continue;
    }
    if(!alsojavaDocs && name.matches(".*javadoc.*")){
        continue;
    }

    for (int i = 0; i < fileBeginList.size(); i++){
        def fBegin = fileBeginList.get(i);
        def version = versionList.get(i);
        if(name.startsWith(fBegin)){
            if(name.matches(".*"+version+".*")){
                finalList << it;
                break;
            }
        }
    }
}

println "---------------------------------------"
println "---------------------------------------"
println "Found:"
finalList.each{
    println it
}


println "---------------------------------------"
println "---------------------------------------"
if(copyPath){
    if(new File(copyPath).exists()){
        println "Copy deps jars to: ${copyPath}"
        finalList.each{
            def name = it.getName();
            def path = it.getAbsolutePath();
            def newPath = new File(copyPath, name).getAbsolutePath();
            new AntBuilder().copy ( file : path , tofile : newPath )
        }
    }


    // zip the thing
    def ant = new AntBuilder()  
    def date = new java.text.SimpleDateFormat("yyyyMMddHHmm").format(new Date());
    ant.zip(destfile: "./extras/deploy/jgrasstools-${date}.zip",  basedir: "./extras/deploy/",  includes: "**",  excludes: "*deploylibs.groovy*,jgrasstools*.zip")  
}
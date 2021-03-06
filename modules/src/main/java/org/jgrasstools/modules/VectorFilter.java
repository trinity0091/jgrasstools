/*
 * This file is part of JGrasstools (http://www.jgrasstools.org)
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * JGrasstools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jgrasstools.modules;

import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_AUTHORCONTACTS;
import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_AUTHORNAMES;
import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_DESCRIPTION;
import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_KEYWORDS;
import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_LABEL;
import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_LICENSE;
import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_NAME;
import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_STATUS;
import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_IN_VECTOR_DESCRIPTION;
import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_OUT_VECTOR_DESCRIPTION;
import static org.jgrasstools.gears.i18n.GearsMessages.OMSVECTORFILTER_P_CQL_DESCRIPTION;
import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Keywords;
import oms3.annotations.Label;
import oms3.annotations.License;
import oms3.annotations.Name;
import oms3.annotations.Status;
import oms3.annotations.UI;

import org.jgrasstools.gears.libs.modules.JGTConstants;
import org.jgrasstools.gears.libs.modules.JGTModel;
import org.jgrasstools.gears.modules.v.vectorfilter.OmsVectorFilter;

@Description(OMSVECTORFILTER_DESCRIPTION)
@Author(name = OMSVECTORFILTER_AUTHORNAMES, contact = OMSVECTORFILTER_AUTHORCONTACTS)
@Keywords(OMSVECTORFILTER_KEYWORDS)
@Label(OMSVECTORFILTER_LABEL)
@Name("_" + OMSVECTORFILTER_NAME)
@Status(OMSVECTORFILTER_STATUS)
@License(OMSVECTORFILTER_LICENSE)
public class VectorFilter extends JGTModel {

    @Description(OMSVECTORFILTER_IN_VECTOR_DESCRIPTION)
    @UI(JGTConstants.FILEIN_UI_HINT)
    @In
    public String inVector;

    @Description(OMSVECTORFILTER_P_CQL_DESCRIPTION)
    @In
    public String pCql = null;

    @Description(OMSVECTORFILTER_OUT_VECTOR_DESCRIPTION)
    @UI(JGTConstants.FILEOUT_UI_HINT)
    @In
    public String outVector;

    @Execute
    public void process() throws Exception {
        OmsVectorFilter omsvectorfilter = new OmsVectorFilter();
        omsvectorfilter.inVector = getVector(inVector);
        omsvectorfilter.pCql = pCql;
        omsvectorfilter.pm = pm;
        omsvectorfilter.doProcess = doProcess;
        omsvectorfilter.doReset = doReset;
        omsvectorfilter.process();
        dumpVector(omsvectorfilter.outVector, outVector);
    }

}

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
package org.jgrasstools.dbs.spatialite.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a db table level.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class TableLevel {
    public DbLevel parent;
    public String tableName;
    public boolean isGeo = false;

    public List<ColumnLevel> columnsList = new ArrayList<ColumnLevel>();

    public ColumnLevel getFirstGeometryColumn() {
        if (isGeo) {
            for( ColumnLevel columnLevel : columnsList ) {
                if (columnLevel.geomColumn != null) {
                    return columnLevel;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return tableName;
    }
}

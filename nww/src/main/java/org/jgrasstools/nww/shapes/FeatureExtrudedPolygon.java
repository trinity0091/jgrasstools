package org.jgrasstools.nww.shapes;

import org.opengis.feature.simple.SimpleFeature;

import gov.nasa.worldwind.render.ExtrudedPolygon;

public class FeatureExtrudedPolygon extends ExtrudedPolygon implements IFeatureShape{
    private SimpleFeature feature;

    public SimpleFeature getFeature() {
        return feature;
    }

    public void setFeature(SimpleFeature feature) {
        this.feature = feature;
    }
}
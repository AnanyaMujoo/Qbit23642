package geometry.polygons;


import java.util.ArrayList;
import java.util.Arrays;

import geometry.framework.GeometryObject;
import geometry.framework.Point;

/**
 * NOTE: Uncommented
 */

public class Polygon extends GeometryObject {
    public ArrayList<Point> points;

    public Polygon(Point... points){
        this.points = new ArrayList<>(Arrays.asList(points));
    }



}

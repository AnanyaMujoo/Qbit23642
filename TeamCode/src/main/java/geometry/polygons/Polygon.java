package geometry.polygons;


import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import geometry.framework.GeometryObject;
import geometry.framework.Point;
import util.template.Iterator;

/**
 * NOTE: Uncommented
 */

public class Polygon extends GeometryObject {


    public final double getArea(){
        double area = 0.0;
        int j = points.size() - 1;
        for (int i = 0; i < points.size(); i++){
            area += (points.get(j).getX()+points.get(i).getX())*(points.get(j).getY()-points.get(i).getY()); j=i;
        }
        return Math.abs(area/2.0);
    }


    public final Rect getBoundingBox() {
        ArrayList<Double> xs = new ArrayList<>();
        ArrayList<Double> ys = new ArrayList<>();
        Iterator.forAll(points, p -> xs.add(p.getX()));
        Iterator.forAll(points, p -> ys.add(p.getY()));
        return new Rect(new Point(Collections.min(xs), Collections.min(ys)), new Point(Collections.max(xs), Collections.max(ys)));
    }



}

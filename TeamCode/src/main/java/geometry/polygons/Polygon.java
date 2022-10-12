package geometry.polygons;


import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;

import geometry.framework.GeometryObject;
import geometry.framework.Point;
import util.template.Iterator;

/**
 * NOTE: Uncommented
 */

public class Polygon extends GeometryObject {


    public Rect getBoundingBox() {
        ArrayList<Double> xs = new ArrayList<>();
        ArrayList<Double> ys = new ArrayList<>();
        Iterator.forAll(points, p -> xs.add(p.getX()));
        Iterator.forAll(points, p -> ys.add(p.getY()));
        Object[] xSorted = xs.toArray();
        Arrays.sort(xSorted);
        Object[] ySorted = ys.toArray();
        Arrays.sort(ySorted);
        return new Rect(new Point((double) xSorted[0], (double) ySorted[0]), new Point((double) xSorted[xSorted.length-1], (double) ySorted[ySorted.length-1]));
    }



}

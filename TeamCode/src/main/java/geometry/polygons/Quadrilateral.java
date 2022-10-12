package geometry.polygons;

import geometry.framework.Point;
import geometry.polygons.Polygon;

/**
 * NOTE: Uncommented
 */

public class Quadrilateral extends Polygon {
    protected final Point p1, p2, p3, p4;
    public Quadrilateral(Point p1, Point p2, Point p3, Point p4){
        this.p1 = p1; this.p2 = p2; this.p3 = p3; this.p4 = p4;
        addPoints(p1, p2, p3, p4);
    }

    public Quadrilateral(Point bl, Point tr){
        double x1 = Math.min(bl.getX(), tr.getX());
        double x2 = Math.max(bl.getX(), tr.getX());
        double y1 = Math.min(bl.getY(), tr.getY());
        double y2 = Math.max(bl.getY(), tr.getY());
        this.p1 = new Point(x1, y1);
        this.p2 = new Point(x1, y2);
        this.p3 = new Point(x2, y2);
        this.p4 = new Point(x2, y1);
    }
}

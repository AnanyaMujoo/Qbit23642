package geometry.circles;

import geometry.framework.GeometryObject;
import geometry.framework.Point;
import geometry.framework.Tracer;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Constants;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class Circle extends GeometryObject implements Tracer {

    protected final Point center;
    protected double r;

    // TOD 5 Fix circle scaling (maybe have endpoint or something)

    public Circle(Point center, double r) {
        this.center = center;
        this.r = r;
        addPoints(center);
    }

    public double getCenterX(){ return center.getX(); }
    public double getCenterY(){ return center.getY(); }
    public Point getCenter() { return center; }
    public double getRadius(){ return r; }

    @Override
    public Point getAt(double t) {
        return center.getRotated(t/360.0);
    }

    public Point getClosestTo(Point p){ return new Vector(center, p).getUnitVector().getScaled(r).getPoint().getAdded(center); }

    public Circle getScaledRadius(double scale){
        return new Circle(this.center.getCopy(), r*scale);
    }

}

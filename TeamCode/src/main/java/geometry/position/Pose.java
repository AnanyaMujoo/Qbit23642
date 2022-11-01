package geometry.position;

import java.util.Locale;

import geometry.framework.GeometryObject;
import geometry.framework.Point;

/**
 * NOTE: Uncommented
 */

public class Pose extends GeometryObject {
    private final Point p;
    private double angle; // Units same as input units
    public Pose(Point p, double angle) { this.p = p; setAngle(angle); }
    public Pose(Vector v, double angle) { this.p = v.getPoint(); setAngle(angle); }
    public Pose(double x, double y, double angle) { this.p = new Point(x,y); setAngle(angle); }
    public Pose(double[] pose){this.p = new Point(pose[0], pose[1]); setAngle(pose[2]);}
    public Pose(){ this.p = new Point(); setAngle(0); }

    public void add(Pose in){p.translate(in.getX(), in.getY()); angle += in.getAngle(); }
    public void add(Vector in){p.translate(in.getX(), in.getY());}
    public Pose getAdded(Pose in){return new Pose(p.getTranslated(in.getX(), in.getY()), this.getAngle()+in.getAngle());}
    public Pose getInverted(){ return new Pose(-getX(), -getY(), -getAngle()); }

    public double getX(){ return p.getX(); }
    public double getY(){ return p.getY(); }
    public Point getPoint(){ return p; }
    public Vector getVector(){ return new Vector(p); }
    public Vector getAngleUnitVector(){ return new Vector(getAngle()); }
    public double getAngle(){ return angle; }
    public Pose getOnlyPointRotated(double angle){ return new Pose(p.getRotated(angle), getAngle()); }
    public void setX(double x){ p.setX(x);}
    public void setY(double y){ p.setY(y);}
    public void setVector(Vector in){ setX(in.getX()); setY(in.getY()); }
    public void setAngle(double angle){ this.angle = angle; }
    public void setZero(){ setX(0); setY(0); setAngle(0); }

    @Override
    public String toString() { return String.format(Locale.US, "Position {p: %s, angle: %f}", p.toString(), getAngle()); }
}
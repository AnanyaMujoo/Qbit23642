package geometry.position;

import java.util.Locale;

import geometry.framework.GeometryObject;
import geometry.framework.Point;

/**
 * NOTE: Uncommented
 */

public class Pose extends GeometryObject {
    private final Point p;
    private double angle;
    public Pose(Point p, double angle) { this.p = p; setAngle(angle); }
    public Pose(double x, double y, double angle) { this.p = new Point(x,y); setAngle(angle); }
    public Pose(double[] pose){this.p = new Point(pose[0], pose[1]);setAngle(pose[2]);}
    public Pose(){ this.p = new Point(); setAngle(0); }

    public double getX(){ return p.getX(); }
    public double getY(){ return p.getY(); }
    public Point getPoint(){ return p; }
    public double getAngle(){ return Math.toDegrees(angle); }
    public void setX(double x){ p.setX(x);}
    public void setY(double y){ p.setY(y);}

    public void setAngle(double angle){ this.angle = Math.toRadians(angle); }

    @Override
    public String toString() { return String.format(Locale.US, "Position {p: %s, angle: %f}", p.toString(), angle); }
}
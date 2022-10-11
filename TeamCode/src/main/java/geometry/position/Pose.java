package geometry.position;

import java.util.Locale;

import geometry.GeometryObject;

/**
 * NOTE: Uncommented
 */

public class Pose extends GeometryObject {
    private Point p;
    private double angle;
    public Pose(Point p, double angle) { setPoint(p); setAngle(angle); }
    public Pose(double x, double y, double angle) { setPoint(new Point(x,y)); setAngle(angle); }
    public Pose(double[] pose){setPoint(new Point(pose[0], pose[1]));setAngle(pose[2]);}
    public Pose(){ setPoint(new Point()); setAngle(0); }

    public double getX(){ return p.getX(); }
    public double getY(){ return p.getY(); }
    public Point getPoint(){ return p; }
    public double getAngle(){ return Math.toDegrees(angle); }
    public void setX(double x){ p.setX(x);}
    public void setY(double y){ p.setY(y);}
    public void setPoint(Point p){this.p = p;}
    public void setAngle(double angle){ this.angle = Math.toRadians(angle); }
//
//    @Override
//    public Pose getRelativeTo(Pose origin) {
//        Pose pos2 = new Pose(p.getRelativeTo(origin), ang);
//        pos2.rotate(-origin.ang);
//        return pos2;
//    }

    public void translate(double deltaX, double deltaY) { p.translate(deltaX, deltaY); }
    public void rotate(double deltaAngle) { this.angle += deltaAngle; }
    @Override
    public String toString() { return String.format(Locale.US, "Position {p: %s, angle: %f}", p.toString(), angle); }
}
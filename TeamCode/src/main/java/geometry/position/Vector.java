package geometry.position;

import java.util.Locale;

import geometry.GeometryObject;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class Vector extends GeometryObject {

    private Point p;
    private double theta;

    public Vector(double x, double y){ p = new Point(x, y); theta = atan2(p.getY(), p.getX()); }
//    public Vector(double length, double angle) {
//        p = new Point(len * cos(Math.toRadians(angle)), len * sin(Math.toRadians(angle)));
//        theta = atan2(p.getY(), p.getX());
//    }

    public Point getPoint(){ return p; }
    //Gets a rotated vector of the current vector angle - positive is anticlockwise

//    @Override
//    public Vector getRelativeTo(Pose origin) {
//        double ang = theta + origin.ang;
//        double radius = getLen();
//        return new Vector(cos(ang) * radius, sin(ang) * radius);
//    }

    public double getX() {
        return p.getX();
    }
    public double getY() {
        return p.getY();
    }
    public double getLength() {
        return p.getDistanceToOrigin();
    }
    public double getTheta() { return Math.toDegrees(theta); }


    public void add(Vector v2){ p.translate(v2.getPoint().getX(), v2.getPoint().getY() ); }
    public void subtract(Vector v2){ p.translate(-v2.getPoint().getX(), -v2.getPoint().getY() ); }

    public String toString() { return String.format(Locale.US,"Vector {x: %f, y: %f, length: %f, theta: %f}", getX(), getY(), getLength(), getTheta()); }

}
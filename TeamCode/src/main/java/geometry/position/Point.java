package geometry.position;

import geometry.GeometryObject;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class Point extends GeometryObject {

    private double x, y;
    public Point(double x, double y) { setX(x); setY(y); }
    public Point(){ setX(0); setY(0);}

    public double getX(){ return x; }
    public double getY(){ return y; }
    public void setX(double x){ this.x = x; }
    public void setY(double y){ this.y = y; }
    public void translate(double deltaX, double deltaY) {x += deltaX; y+= deltaY;}

    public double getDistanceTo(Point p2){ return sqrt(pow(getX()-p2.getX(), 2) + pow(getY()-p2.getY(), 2));}
    public double getDistanceToOrigin(){ return getDistanceTo(new Point()); }

//    @Override
//    public Point getRelativeTo(Pose origin) {
//        double sx = x - origin.p.x, sy = y - origin.p.y;
//        double nx = sx * cos(origin.ang) + sy * sin(origin.ang);
//        double ny = sx * -sin(origin.ang) + sy * cos(origin.ang);
//        return new Point(nx, ny);
//    }

    public String toString() {
        return "Point {x:" + x + ", y:" + y + "}";
    }
}

package geometry.position;

import geometry.GeometryObject;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class Point extends GeometryObject {
    public double x, y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Point getRelativeTo(Pose origin) {
        double sx = x - origin.p.x, sy = y - origin.p.y;
        double nx = sx * cos(origin.ang) + sy * sin(origin.ang);
        double ny = sx * -sin(origin.ang) + sy * cos(origin.ang);
        return new Point(nx, ny);
    }

    public static Point zero(){ return new Point(0,0); }

    public String toString() {
        return x + " " + y;
    }
}

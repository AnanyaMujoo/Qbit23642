package geometry.circles;

import geometry.GeometryObject;
import geometry.position.Point;
import geometry.position.Pose;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class Circle extends GeometryObject {
    public Point center;
    public double r; // center is (h, k) and radius is r

    public Circle(Point center, double r) {
        this.center = center;
        this.r = r;
    }

    public double getThetaFromPoint(Point p) {
        double ang = atan2(p.y - center.y, p.x - center.x);
        ang %= 2 * PI;
        return ang;
    }

    public Pose getPositionFromTheta(double theta) {
        Point p = new Point(center.x + r * cos(theta), center.y + r * sin(theta));
        double ang = PI/2 - atan2(p.y - center.y, center.x - p.x);
        return new Pose(p, ang);
    }

    @Override
    public GeometryObject getRelativeTo(Pose origin) {
        return new Circle(center.getRelativeTo(origin), r);
    }
}

package geometry.position;

import geometry.GeometryObject;
import geometry.circles.AngleType;

/**
 * NOTE: Uncommented
 */

public class Pose extends GeometryObject {
    public Point p;
    public double ang;

    public Pose(Point p, double ang) { this.p = p; this.ang = ang; }
    public Pose(Point p, double ang, AngleType angleType) {
        this.p = p;
        this.ang = toRad(ang, angleType);
    }
    public Pose(double x, double y, double ang) {
        this.p = new Point(x, y);
        this.ang = ang;
    }
    public Pose(double[] pose){
        this.p = new Point(pose[0], pose[1]);
        this.ang = pose[2];
    }


    public double[] asArray(){
        return new double[]{p.x, p.y, ang};
    }
    //public boolean angIsInf() { return ang == Constants.INF; }

    @Override
    public Pose getRelativeTo(Pose origin) {
        Pose pos2 = new Pose(p.getRelativeTo(origin), ang);
        pos2.rotate(-origin.ang);
        return pos2;
    }

    public void translate(double x, double y) { p.x += x; p.y += y; }

    public void rotate(double a) { ang += a; ang = boundAngleTo2Pi(ang);}

    @Override
    public String toString() {
        return "Position{" +
                "p=" + p +
                ", ang=" + ang +
                '}';
    }
}
package geometry.position;

import geometry.GeometryObject;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class Line extends GeometryObject {

    public Point p1;
    public Point p2;
    public double mx;
    public double my;

    //Define line using endpoints
    public Line(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;

        mx = p2.getX()-p1.getX();
        my = p2.getY()-p1.getY();

    }

    //Gets the position of the line at a certain t value
    public Point getAt(double t){
        return new Point ((p1.getX())+(mx*t), (p1.getY())+(my*t));
    }

    //Gets the length of the line
    public double getlength(){
        return sqrt(pow(mx, 2) + pow(my, 2));
    }

//    @Override
//    public GeometryObject getRelativeTo(Pose origin) {
//        return new Line(p1.getRelativeTo(origin), p2.getRelativeTo(origin));
//    }

    public String toString() {
        return "Line {" +
                " p1: " + p1 +
                ", p2: " + p2 +
                ", mx: " + mx +
                ", my: " + my +
                '}';
    }
}
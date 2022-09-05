package geometry.position;

import geometry.GeometryObject;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class Line extends GeometryObject {
    //start coords (x1,y1) end coords (x2,y2) mx is slope in x dir from 0-1 and my is the same for y
    public Point p1;
    public Point p2;
    public double mx;
    public double my;

    //Define line using endpoints
    public Line(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;

        mx = p2.x-p1.x;
        my = p2.y-p1.y;

    }

    //Gets the position of the line at a certain t value
    public Point getAt(double t){
        return new Point ((p1.x)+(mx*t), (p1.y)+(my*t));
    }

    //Gets the length of the line
    public double getlength(){
        return sqrt(pow(mx, 2) + pow(my, 2));
    }

    @Override
    public GeometryObject getRelativeTo(Pose origin) {
        return new Line(p1.getRelativeTo(origin), p2.getRelativeTo(origin));
    }

    public String toString() {
        return "Line {" +
                " p1: " + p1 +
                ", p2: " + p2 +
                ", mx: " + mx +
                ", my: " + my +
                '}';
    }
}
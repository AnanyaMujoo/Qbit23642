package geometry.position;

import geometry.GeometryObject;
import geometry.circles.AngleType;

import static geometry.circles.AngleType.radToDeg;
import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class Vector extends GeometryObject {
    //x and y coords of the tip of vector, theta is angle measured from the right horizontal
    public Point p;
    public double theta;

    //Constructor to create vect using coords
    public Vector(double x1, double y1){
        p = new Point(x1, y1);
        theta = atan2(p.y, p.x);
    }
    //Constructor to create vect using angle and length
    public Vector(double len, double angle, AngleType unit) {
        angle = toRad(angle, unit);
        p = new Point(len * cos(angle), len * sin(angle));
        theta = atan2(p.y, p.x);
    }
    //Gets a rotated vector of the current vector angle - positive is anticlockwise

    @Override
    public Vector getRelativeTo(Pose origin) {
        double ang = theta + origin.ang;
        double radius = getLen();
        return new Vector(cos(ang) * radius, sin(ang) * radius);
    }

    //Gets x
    public double getX() {
        return p.x;
    }
    //Gets y
    public double getY() {
        return p.y;
    }
    //Gets length
    public double getLen() {
        return sqrt(pow(p.x, 2) + pow(p.y, 2));
    }
    //Gets angle
    public double getAngle(AngleType type) {
        if (type == AngleType.RADIANS) {
            return theta;
        } else {
            return radToDeg(theta);
        }
    }
    //Sets x and y coords
    public void setXY(double x1, double y1){
        p.x = x1;
        p.y = y1;
        theta = atan2(p.y, p.x);
    }
    //Creates a string representation
    public String toString() {
        return "x: " + this.getX() + ", y: " + this.getY() + ", angle: " + this.getAngle(AngleType.DEGREES) + ", length: " + this.getLen();
    }


    public void add(Vector in){
        p.x = p.x+in.p.x;
        p.y = p.y+in.p.y;
    }

    public void subtract(Vector in){
        p.x = p.x-in.p.x;
        p.y = p.y-in.p.y;
    }

}
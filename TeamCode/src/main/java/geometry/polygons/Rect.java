package geometry.polygons;

import geometry.GeometryObject;
import geometry.position.Point;
import geometry.position.Pose;

/**
 * NOTE: Uncommented
 */

//Rect class defines a rectagle object
public class Rect extends Quadrilateral {
    //x, y, w, h are the x and y coords of the top left corner and w and h are width and height repectivly
    public Point ll, ur;

    public Rect(Point ll, Point ur){
        double x1 = Math.min(ll.x, ur.x);
        double x2 = Math.max(ll.x, ur.x);
        double y1 = Math.min(ll.y, ur.y);
        double y2 = Math.max(ll.y, ur.y);
        this.ll = new Point(x1, y1);
        this.ur = new Point(x2, y2);
    }

    //Gets area of rect
    public double getArea(){
        return Math.abs((ll.x - ur.x) * (ll.y - ur.y));
    }
    //Gets different corner values where x1 is left x2 right y1 top y2 bottom
    public double getX1(){
        return ll.x;
    }
    public double getX2(){
        return ur.x;
    }
    public double getY1(){
        return ll.y;
    }
    public double getY2(){
        return ur.y;
    }

    //Returns a cropped version of the current rectangle
    public Rect crop(int left, int top, int right, int bottom) {
        return new Rect(new Point(ll.x+left, ll.y+bottom),  new Point(ur.x-right, ur.y-top));
    }

    @Override
    public GeometryObject getRelativeTo(Pose origin) {
        return super.getRelativeTo(origin);
    }

    //Creates a string representation
    public String toString() {
        return "X1: " + getX1() + ", Y1: " + getY1() + ", X2: " + getX2() + ", Y2: " + getY2();
    }
}
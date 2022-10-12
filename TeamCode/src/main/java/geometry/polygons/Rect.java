package geometry.polygons;

import geometry.framework.Point;

/**
 * NOTE: Uncommented
 */

//Rect class defines a rectagle object
public class Rect extends Quadrilateral {
    //x, y, w, h are the x and y coords of the top left corner and w and h are width and height repectivly
    public Point ll, ur;

    public Rect(Point ll, Point ur){
        double x1 = Math.min(ll.getX(), ur.getX());
        double x2 = Math.max(ll.getX(), ur.getX());
        double y1 = Math.min(ll.getY(), ur.getY());
        double y2 = Math.max(ll.getY(), ur.getY());
        this.ll = new Point(x1, y1);
        this.ur = new Point(x2, y2);
    }

    //Gets area of rect
    public double getArea(){
        return Math.abs((ll.getX() - ur.getX()) * (ll.getY() - ur.getY()));
    }
    //Gets different corner values where x1 is left x2 right y1 top y2 bottom
    public double getX1(){
        return ll.getX();
    }
    public double getX2(){
        return ur.getX();
    }
    public double getY1(){
        return ll.getY();
    }
    public double getY2(){
        return ur.getY();
    }

    //Returns a cropped version of the current rectangle
    public Rect crop(int left, int top, int right, int bottom) {
        return new Rect(new Point(ll.getX() +left, ll.getY()+bottom),  new Point(ur.getX() -right, ur.getY()-top));
    }

//    @Override
//    public GeometryObject getRelativeTo(Pose origin) {
//        return super.getRelativeTo(origin);
//    }

    //Creates a string representation
    public String toString() {
        return "X1: " + getX1() + ", Y1: " + getY1() + ", X2: " + getX2() + ", Y2: " + getY2();
    }
}
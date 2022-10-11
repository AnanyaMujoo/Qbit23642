package geometry.polygons;
import java.util.Arrays;

import geometry.position.Point;
public class Triangle extends Polygon {
    //defining the input points
    Point p1;
    Point p2;
    Point p3;
    Point lb;
    Point rt;

    public Triangle(Point p1, Point p2, Point p3){
        //contructor for the points
        super(p1, p2, p3);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.lb = lb;
        this.rt = rt;
    }
    public double area()
    {
        //finding the distance between the 2 out of the 3 points
        double side1 = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
        double side2 = Math.sqrt(Math.pow(p2.getX() - p3.getX(), 2) + Math.pow(p2.getY() - p3.getY(), 2));
        double side3 = Math.sqrt(Math.pow(p3.getX() - p1.getX(), 2) + Math.pow(p3.getY() - p1.getY(), 2));
        //using Heron's formula to find the area using the sides
        double s = (side1 + side2 + side3)/2;
        double area = Math.pow((s*(s-side1)*(s-side2)*(s-side3)), 0.5);
        return area;

    }
    public Rect boundingbox()
    {
        //creating an array to store the x and y values
        double[] x = {p1.getX(), p2.getX(), p3.getX()};
        double[] y = {p1.getY(), p2.getY(), p3.getY()};
        //sorting the arry from least to greatest
        Arrays.sort(x);
        Arrays.sort(y);
        //finding the greatest and the least x and y values then assigning them to points
        return new Rect(new Point(x[0], y[0]), new Point(x[2], y[2]));
    }
}

package geometry.polygons;
import java.util.Arrays;

import geometry.framework.Point;
public class Triangle extends Polygon {
    private final Point p1, p2, p3;

    public Triangle(Point p1, Point p2, Point p3){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        addPoints(p1, p2, p3);
    }

    public double area() {
        double side1 = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
        double side2 = Math.sqrt(Math.pow(p2.getX() - p3.getX(), 2) + Math.pow(p2.getY() - p3.getY(), 2));
        double side3 = Math.sqrt(Math.pow(p3.getX() - p1.getX(), 2) + Math.pow(p3.getY() - p1.getY(), 2));
        double s = (side1 + side2 + side3) / 2;
        return Math.pow((s * (s - side1) * (s - side2) * (s - side3)), 0.5);
    }
}

package geometry.position;

import static java.lang.Math.atan2;
import static java.lang.Math.*;

public class Vector2 {
    private double x;
    private double y;

    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void rotate(double phi){
        double newX = x * cos(phi) - y * sin(phi);
        double newY = x * sin(phi) + y * cos(phi);
        x = newX;
        y = newY;
    }

    public Vector2 getRotated(double phi){
        Vector2 copy = copy();
        copy.rotate(phi);
        return copy;
    }

    public void add(Vector2 vectorToAdd){
        x += vectorToAdd.x;
        y += vectorToAdd.y;
    }

    public Vector2 getAdded(Vector2 vectorToAdd){
        Vector2 copy = copy();
        copy.add(vectorToAdd);
        return copy;
    }

    public Vector2 getNegative(){
        return new Vector2(-x,-y);
    }

    public Vector2 copy(){
        return new Vector2(x,y);
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
}

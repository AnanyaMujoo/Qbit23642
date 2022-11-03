package geometry.framework;

import geometry.position.Vector;
import math.linearalgebra.Matrix2D;
import util.codeseg.ParameterCodeSeg;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class Point {
    private double x, y;
    public Point(double x, double y) { setX(x); setY(y); }
    public Point(){ setX(0); setY(0);}
    public double getX(){ return x; }
    public double getY(){ return y; }
    public void set(Point p){setX(p.x); setY(p.y);}
    public void setX(double x){ this.x = x; }
    public void setY(double y){ this.y = y; }
    public void reflectX(){ x = -x; }
    public void reflectY(){ y = -y; }
    public void scale(double scale){ this.x *= scale; this.y *= scale;}
    public void scale(Point p, double scale){ applyMatrixTransformation(p, Matrix2D.getScaleMatrix(scale)); }
    public void scaleX(double scale){ this.x *= scale; }
    public void scaleY(double scale){ this.y *= scale; }
    public void translate(double deltaX, double deltaY) {x += deltaX; y+= deltaY;}
    public void translate(Vector v){ translate(v.getX(), v.getY());}
    public void rotate(double angle){ set(Matrix2D.getRotationMatrix(Math.toRadians(angle)).multiply(this)); }
    public void rotate(Point p, double angle){ applyMatrixTransformation(p, Matrix2D.getRotationMatrix(Math.toRadians(angle))); }
    private void applyMatrixTransformation(Point p, Matrix2D matrix){ Point offsetPoint = matrix.multiply(getSubtracted(p)); set(p.getAdded(offsetPoint)); }
    public Point getAdded(Point p){ return new Point(getX() + p.getX(), getY() + p.getY()); }
    public Point getSubtracted(Point p){ return new Point(getX() - p.getX(), getY() - p.getY()); }
    public Point getRotated(double angle){ return getCopy(p -> p.rotate(angle));}
    public Point getRotated(Point c, double angle){ return getCopy(p -> p.rotate(c, angle)); }
    public Point getTranslated(double deltaX, double deltaY){ return getCopy(p -> p.translate(deltaX, deltaY)); }
    public Point getTranslated(Vector vector){ return getTranslated(vector.getX(), vector.getY()); }
    public double getDistanceTo(Point p2){ return sqrt(pow(getX()-p2.getX(), 2) + pow(getY()-p2.getY(), 2));}
    public double getDistanceToOrigin(){ return getDistanceTo(new Point()); }
    public Point getCopy(){
        return new Point(getX(), getY());
    }
    public Point getCopy(ParameterCodeSeg<Point> operation){ Point copy = getCopy(); operation.run(copy); return copy; }
    public String toString() { return "Point {x:" + x + ", y:" + y + "}"; }
}


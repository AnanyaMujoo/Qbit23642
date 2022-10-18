package geometry.position;

import org.firstinspires.ftc.robotcore.external.matrices.DenseMatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;

import java.util.Locale;

import geometry.framework.GeometryObject;
import geometry.framework.Point;
import global.General;
import math.linearalgebra.Matrix2D;
import util.codeseg.ParameterCodeSeg;

import static java.lang.Math.atan2;
import static java.lang.Math.*;

public class Vector extends GeometryObject {
    private final Point p;
    private double theta; // Always in radians (effectively final)

    public Vector(double x, double y){
        p = new Point(x, y); setTheta();
        addPoints(p);
    }

    public Vector(Point start, Point end){
        p = end.getSubtracted(start);
        addPoints(p);
    }

    public double getX() {
        return p.getX();
    }
    public double getY() {
        return p.getY();
    }
    public Point getPoint(){ return p; }
    public double getLength() {
        return p.getDistanceToOrigin();
    }
    public double getTheta() { return Math.toDegrees(theta); }
    private void setTheta(){ theta = atan2(getY(), getX()); }

    public Vector getCopy(){
        return new Vector(getX(), getY());
    }
    public Vector getCopy(ParameterCodeSeg<Vector> operation){ Vector copy = getCopy(); operation.run(copy); return copy; }

    public void add(Vector v2){ translate(v2.getPoint().getX(), v2.getPoint().getY() ); }
    public void subtract(Vector v2){ translate(-v2.getPoint().getX(), -v2.getPoint().getY() ); }
    public void invert(){ scale(-1); }
    public void reflectX(){ p.reflectX(); }
    public void reflectY(){ p.reflectY(); }

    public Vector getAdded(Vector v2){ return getCopy(v -> v.add(v2)); }
    public Vector getSubtracted(Vector v2){ return getCopy(v -> v.subtract(v2)); }
    public Vector getRotated(double phi){ return getCopy(v -> v.rotate(phi)); }
    public Vector getScaled(double scale){ return getCopy(v -> v.scale(scale)); }
    public Vector getInverted(){ return getCopy(Vector::invert); }
    public Vector getReflectedX(){ return getCopy(Vector::reflectX); }
    public Vector getReflectedY(){ return getCopy(Vector::reflectY); }

    public String toString() { return String.format(Locale.US,"Vector {x: %f, y: %f, length: %f, theta: %f}", getX(), getY(), getLength(), getTheta()); }
}

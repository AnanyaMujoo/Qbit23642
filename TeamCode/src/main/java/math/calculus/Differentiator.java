package math.calculus;

import geometry.position.Point;
import math.Function;
import math.Operator;
import util.codeseg.ReturnCodeSeg;

public class Differentiator extends Operator {
    private double derivative;
    public void differentiate(double t, double step){
        derivative = (function.f(t+step)-function.f(t-step))/(2*step);
    }
    public void differentiate(Point f1, Point f2){
        derivative = (f2.y-f1.y)/(f2.x-f1.x);
    }
    public void differentiate(Point f1, Point f2, Point f3){
        derivative = (f3.y-f1.y)/(f3.x-f1.x);
    }
    public double getDerivative(){return derivative;}
}

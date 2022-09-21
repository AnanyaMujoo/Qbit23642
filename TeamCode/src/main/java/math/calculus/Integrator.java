package math.calculus;

import geometry.position.Point;
import math.Function;
import math.Operator;
import util.codeseg.ReturnCodeSeg;

public class Integrator extends Operator {
    private double integral;
    public void integrate(double t, double step){
        integral += (step/6.0)*(function.f(t) + (4*function.f(t + (step/2))) + function.f(t + step));
    }
    public void integrate(Point f1, Point f2){
        integral += ((f2.x - f1.x)/2)*(f1.y + f2.y);
    }
    public void integrate(Point f1, Point f2, Point f3){
        integral += (((f3.x-f1.x)/6.0)*(f1.y + (4*f2.y) + f3.y));
    }
    public double getIntegral(){
        return integral;
    }
    public void reset(){
        integral = 0;
    }
}

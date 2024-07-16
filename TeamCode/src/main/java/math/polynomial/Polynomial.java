package math.polynomial;

import java.util.ArrayList;
import java.util.Arrays;

import math.Function;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.fault;

/**
 * NOTE: Uncommented
 */

public class Polynomial extends Function {

    public ArrayList<Double> coefficients;

    protected Polynomial(Double... coefficients){
        this.coefficients = new ArrayList<>(Arrays.asList(coefficients));
    }

    public int degree(){return coefficients.size()-1;}

    public double a(int n){return coefficients.get(n);}

    public double[] roots(){
        return new double[1];
    }

    public Polynomial differentiate(){
        Double[] newCoeffs = new Double[degree()-1];
        for(int i = 0; i < degree()-1; i++){
            newCoeffs[i] = a(i)*(degree()-i);
        }
        return new Polynomial(newCoeffs);
    }

    @Override
    public double f(double x) {
        double f = 0;
        for(int i = 0; i < degree(); i++){
            f += a(i)*Math.pow(x,degree()-i);
        }
        return f;
    }

    @Override
    public double fprime(double x) {
        return differentiate().f(x);
    }

    public Polynomial nthDerivative(double n, double x){
        fault.warn(n +"th derivative taken on polynomial of degree"+ degree(), Expectation.EXPECTED, Magnitude.MINOR, n > degree(), false);
        Polynomial nth = this;
        for(int i = 0; i < n; i++){
            nth = nth.differentiate();
        }
        return nth;
    }
}

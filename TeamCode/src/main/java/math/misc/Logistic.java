package math.misc;


import math.Function;
import util.template.ParameterConstructor;

public class Logistic extends Function implements ParameterConstructor<Double> {
    // TODO 3 TEST This
    private double m;
    private double b;
    private double k;
    /**
     * Represents the equation m/(1+be^-kx)
     * This is a curve that has a yint of m/(1+b) that grows to m as x -> inf
     * It looks like an elongated S shaped curve
     */


    /**
     *
     * @param type
     * @param pars
     */
    public Logistic(LogisticParameterType type, Double... pars){
        addConstructor(LogisticParameterType.RANGE_ONE, 2, in -> new Double[]{1+in[0]*Math.exp(-in[1]), in[0], in[1]});
        addConstructor(LogisticParameterType.STANDARD_FORM, 3);
        createConstructors(type, pars, new Double[]{1.0,1.0,1.0});
    }

    /**
     * Internal constructor, [m, b, k]
     * @param in
     */
    @Override
    public void construct(Double[] in) {
        m = in[0]; b = in[1]; k = in[2];
    }


    /**
     * Define the logistic using m, b, and k
     * @param m
     * @param b
     * @param k
     */
    public Logistic(double m, double b, double k){
        this.m = m;
        this.b = b;
        this.k = k;
    }

    /**
     * Define the logistic such that m is always 1 (the max range is from 0-1)
     * @param b
     * @param k
     */
    public Logistic(double b, double k){
        this.m = 1+b*Math.exp(-k);
        this.b = b;
        this.k = k;
    }


    /**
     * Gets the output of the logistic curve at a certain x value
     * @param x
     * @return logistic(x)
     */
    @Override
    public double f(double x) {
        return m/(1+b*Math.exp(-k*x));
    }

    @Override
    public double fprime(double x) {
        return (m*b*k*Math.exp(k*x))/(Math.pow(Math.exp(k*x)+b,2));
    }


    /**
     * Type of parameters
     */
    public enum LogisticParameterType implements ParameterType{
        RANGE_ONE,
        STANDARD_FORM
    }
}

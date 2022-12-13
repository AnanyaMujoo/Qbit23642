package math.misc;


import math.Function;
import util.template.ParameterConstructor;

public class Logistic extends Function implements ParameterConstructor<Double> {

    private double m;
    private double b;
    private double k;
    /**
     * Represents the equation m/(1+be^-kx)
     * This is a curve that has a yint of m/(1+b) that grows to m as x -> inf
     * It looks like an elongated S shaped curve
     */

    /**
     * Equation to copy paste
     * \frac{1+be^{-k}}{1+be^{-kx}}
     * or
     * https://www.desmos.com/calculator/2mvgkmzyxb
     */


    /**
     *
     * @param type
     * @param pars
     */
    public Logistic(LogisticParameterType type, Double... pars){
        addConstructor(LogisticParameterType.ONE_ONE, 2, in -> new Double[]{1+in[0]*Math.exp(-in[1]), in[0], in[1]});
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
     * ONE_ONE - Goes through (1,1)
     * STANDARD_FORM - Define with all params
     */
    public enum LogisticParameterType implements ParameterType{
        ONE_ONE,
        STANDARD_FORM
    }
}

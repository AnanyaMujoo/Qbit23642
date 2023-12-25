package math.misc;

import math.Function;

public class Exponential extends Function {
    private final double m;
    private final double b;
    private final double k;

    /**
     * Represents the equation b - me^-kx
     * This is a curve that has a yint of m/(1+b) that grows to m as x -> inf
     * It looks like an elongated S shaped curve
     */

    public Exponential(double b, double m, double k) {
        this.m = m;
        this.b = b;
        this.k = k;
    }


    @Override
    public double f(double x) {
        return b - (m*Math.exp(-k*x));
    }
}

package autoutil.controllers.control1D;

import autoutil.paths.PathSegment;
import geometry.position.Pose;

public class PAR extends Controller1D{
    // Proportional, Approach Rate, Rest Power

    //Using x(t) we can calculate v(x)
    //1. First calculate v(t) which is d/dt(x(t))
    //2. Then calculate g(x) which is x^-1(t) (inverse)
    //3. Then calculate v(g(x)) which is v(x)

    //Approach rate [0,1] higher means stops farther away
    private final double approachRate;
    //Proportional coeff
    private final double proportional;

    public PAR(double proportional, double approachRate, double restPow){ this.proportional = proportional; this.approachRate = approachRate; setRestOutput(restPow); }

    public double VofS(){ return approachRate*Math.pow(Math.abs(getError()), 1/approachRate)*Math.signum(getError()); }

    @Override
    protected double setDefaultAccuracy() { return 0.5; }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 0.2; }

    @Override
    protected double setDefaultRestOutput() { return 0; }

    @Override
    protected void updateController(Pose pose, PathSegment pathSegment) {}

    @Override
    protected double setOutput() { return proportional * (VofS() - processVariableProfiler.getDerivative()); }

    @Override
    protected boolean hasReachedTarget() { return true; }

    public double[] getCoefficients(){ return new double[]{proportional, approachRate, getRestOutput()};}
}

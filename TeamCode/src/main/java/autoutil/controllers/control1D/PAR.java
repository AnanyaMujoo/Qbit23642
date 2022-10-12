package autoutil.controllers.control1D;

import autoutil.paths.PathSegment;
import geometry.position.Pose;

public class PAR extends Controller1D{

    // TODO 4 CHECK Works?

    //Using x(t) we can calculate v(x)
    //1. First calculate v(t) which is d/dt(x(t))
    //2. Then calculate g(x) which is x^-1(t) (inverse)
    //3. Then calculate v(g(x)) which is v(x)

    //Rest pow
    public double restPow = 0;
    //Approach rate [0,1] lower means stops farther away
    public double approachRate = 0;
    //Proportional coeff
    public double proportionalCoeff = 0;

    public PAR(double proportionalCoeff, double approachRate, double restPow){
        this.proportionalCoeff = proportionalCoeff;
        this.approachRate = approachRate;
        this.restPow = restPow;
    }

    public double[] getCoefficients(){
        return new double[]{proportionalCoeff, approachRate, restPow};
    }

    public double VofS(){
        return approachRate*Math.pow(Math.abs(getError()), 1/approachRate)*Math.signum(getError());
    }

    @Override
    protected void updateController(Pose pose, PathSegment pathSegment) {
        if(!isWithinAccuracyRange()){
            setOutput((proportionalCoeff * (VofS() - processVariableProfiler.getDerivative())) + Math.signum(getError())*restPow);
        }
    }
}

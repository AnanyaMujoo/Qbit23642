package autoutil.controllers.control1D;

import geometry.position.Pose;
import util.template.ParameterConstructor;

import static java.lang.Math.*;

public class PID extends Controller1D implements ParameterConstructor<Double> {

    private double kp;
    private double ki;
    private double kd;
    private double maximumDerivative;
    private double maximumIntegralRange;

    /**
     * Constructor for PID <br>
     * Input 1: Proportional coefficient <br>
     * Input 2: Integral coefficient <br>
     * Input 3: Derivative coefficient <br>
     * Input 4: Maximum Derivative <br>
     * Input 5: Maximum Integral Range <br>
     * @param parameterType
     * @param parameters
     */
    public PID(PIDParameterType parameterType, Double... parameters){
        addConstructor(PIDParameterType.DEFAULT, 3);
        addConstructor(PIDParameterType.DEFAULT_ALL, 5);
        addConstructor(PIDParameterType.STANDARD_FORM, 3, in -> new Double[]{in[0], in[0]/in[1], in[0]*in[2]});
        addConstructor(PIDParameterType.STANDARD_FORM_ALL, 5, in -> new Double[]{in[0], in[0]/in[1], in[0]*in[2], in[3], in[4]});
        createConstructors(parameterType, parameters, new Double[]{0.0, 0.0, 0.0, 100000.0, 100000.0});
    }

    @Override
    public void construct(Double[] in){ this.kp = in[0]; this.ki = in[1]; this.kd = in[2]; this.maximumDerivative = in[3]; this.maximumIntegralRange = in[4]; }

    @Override
    protected double setDefaultAccuracy() { return 1.0; }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 0.2; }

    @Override
    protected double setDefaultRestOutput() { return 0.05; }

    @Override
    protected void updateController(Pose pose, PathSegment pathSegment){ if(abs(getError()) > maximumIntegralRange){ errorProfiler.resetIntegral(); } }

    @Override
    protected double setOutput() { return (kp * getError() + (ki * errorProfiler.getIntegral()) + (kd * errorProfiler.getDerivative())); }

    @Override
    protected boolean hasReachedTarget() { return maxDerivativeTarget(maximumDerivative); }

    public double[] getCoefficients(){ return new double[]{kp, ki, kd}; }

    public enum PIDParameterType implements ParameterType {
        DEFAULT,
        DEFAULT_ALL,
        STANDARD_FORM,
        STANDARD_FORM_ALL;
    }

}
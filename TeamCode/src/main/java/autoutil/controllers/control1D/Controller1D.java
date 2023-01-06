package autoutil.controllers.control1D;

import autoutil.generators.Generator;
import autoutil.generators.PoseGenerator;
import autoutil.Profiler;
import geometry.framework.Point;
import geometry.position.Pose;
import util.codeseg.ReturnCodeSeg;
import util.template.Precision;

import static java.lang.Math.abs;

public abstract class Controller1D {

    private final Precision precision = new Precision();

    private ReturnCodeSeg<Double> processVariable = () -> 0.0;
    private ReturnCodeSeg<Double> processError = this::getRawError;
    protected Profiler processVariableProfiler = new Profiler(this::getCurrentValue);
    protected Profiler errorProfiler = new Profiler(this::getError);

    private double output = 0;
    private boolean isAtTarget = false;

    private double currentValue = 0;
    private double  targetValue = 0;

    private double restOutput = setDefaultRestOutput();
    private double minimumTime = setDefaultMinimumTimeReachedTarget();
    private double accuracy = setDefaultAccuracy();

    protected double scale = 1.0;
    protected double accuracyScale = 1.0;

    protected abstract double setDefaultAccuracy();
    protected abstract double setDefaultMinimumTimeReachedTarget();
    protected abstract double setDefaultRestOutput();
    protected abstract void updateController(Pose pose, Generator generator);
    protected abstract double setOutput();
    protected abstract boolean hasReachedTarget();

    public void scale(double scale){ this.scale = scale; }
    public void scaleAccuracy(double scale){ this.accuracyScale = scale; }

    public void setAccuracy(double accuracy){
        this.accuracy = accuracy;
    }
    public void setMinimumTime(double minimumTime){ this.minimumTime = minimumTime; }
    public void setRestOutput(double restOutput){ this.restOutput = restOutput; }
    public boolean isWithinAccuracyRange(){ return (abs(getError()) < accuracy*accuracyScale); }
    public double getRestOutput(){ return !isWithinAccuracyRange() ? Math.signum(getError()) * restOutput : 0;}
    public final void update(Pose pose, Generator generator){
        currentValue = processVariable.run();
        processVariableProfiler.update();
        errorProfiler.update();
        updateController(pose, generator);
        isAtTarget = precision.isInputTrueForTime(hasReachedTarget(), minimumTime);
        output = (scale*Precision.clip(setOutput(), 1)) + getRestOutput();
    }
    public final void update(){ update(new Pose(new Point(0,0),0), new PoseGenerator()); }
    protected double getCurrentValue(){
        return currentValue;
    }
    public double getOutput(){
        return output;
    }
    public void setTarget(double targetValue){ this.targetValue = targetValue; }
    public double getTarget(){ return targetValue; }
    public double getRawError(){ return targetValue-currentValue; }
    public double getError(){ return processError.run(); }
    public void setProcessVariable(ReturnCodeSeg<Double> processVariable){ this.processVariable = processVariable; }
    public void setProcessError(ReturnCodeSeg<Double> processError){ this.processError = processError; }
    public boolean maxDerivativeTarget(double maxDerivative){ return (abs(processVariableProfiler.getDerivative()) < maxDerivative); }
    public boolean isAtTarget(){ return isAtTarget; }
    public void reset(){ targetValue = 0; errorProfiler.reset(); processVariableProfiler.reset(); isAtTarget = false; precision.reset(); }
    public double[] getErrorState(){ return new double[]{getError(), errorProfiler.getIntegral(), errorProfiler.getDerivative()}; }
    public double[] getProcessVariableState(){ return new double[]{currentValue, processVariableProfiler.getIntegral(), processVariableProfiler.getDerivative()}; }
}
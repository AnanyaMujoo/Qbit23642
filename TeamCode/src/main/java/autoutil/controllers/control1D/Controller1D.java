package autoutil.controllers.control1D;

import autoutil.paths.PathPose;
import autoutil.paths.PathSegment;
import autoutil.profilers.Profiler;
import geometry.position.Point;
import geometry.position.Pose;
import util.codeseg.ReturnCodeSeg;

public abstract class Controller1D {

    protected ReturnCodeSeg<Double> processVariable;
    protected ReturnCodeSeg<Double> processError = this::getRawError;
    protected Profiler processVariableProfiler = new Profiler(this::getCurrentValue);
    protected Profiler errorProfiler = new Profiler(this::getError);

    private double output = 0;
    protected boolean isAtTarget = false;

    protected double currentValue = 0;
    protected double  targetValue = 0;
    protected double currentTime = 0;

    protected double accuracy = 0;


    public void setAccuracy(double accuracy){
        this.accuracy = accuracy;
    }

    public boolean isWithinAccuracyRange(){
        return (Math.abs(getError()) < accuracy);
    }


    public abstract void update(Pose pose, PathSegment pathSegment);
    public final void update(){
        update(new Pose(new Point(0,0),0), new PathPose(0,0,0));
    }

    public void updateProcessVariable(){
        currentValue = processVariable.run();
    }

    public void updateProfilers(){
        updateProcessVariable();
        processVariableProfiler.update();
        errorProfiler.update();
    }

    public double getCurrentValue(){
        return currentValue;
    }

    public double getOutput(){
        return output;
    }

    public void setOutput(double output){this.output = output;}

    public void setTarget(double targetValue){
        this.targetValue = targetValue;
    }

    public double getTarget(){
        return targetValue;
    }

    public double getRawError(){
        return targetValue-currentValue;
    }

    public double getError(){
        return processError.run();
    }


    public void setProcessVariable(ReturnCodeSeg<Double> processVariable){
        this.processVariable = processVariable;
    }

    public void setProcessError(ReturnCodeSeg<Double> processError){
        this.processError = processError;
    }


    public boolean isAtTarget(){ return isAtTarget;}

    public void reset(){
        targetValue = 0;
        errorProfiler.reset();
        processVariableProfiler.reset();
        isAtTarget = false;
    }


    public double[] getErrorState(){
        return new double[]{getError(), errorProfiler.getIntegral(), errorProfiler.getDerivative()};
    }

    public double[] getProcessVariableState(){
        return new double[]{currentValue, processVariableProfiler.getIntegral(), processVariableProfiler.getDerivative()};
    }
}
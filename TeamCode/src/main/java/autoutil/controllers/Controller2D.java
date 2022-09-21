package autoutil.controllers;

import autoutil.paths.PathSegment2;
import autoutil.profilers.Profiler;
import geometry.position.Pose;
import util.codeseg.ReturnCodeSeg;

public abstract class Controller2D extends Controller1D {
    public Controller1D xController;
    public Controller1D yController;
    protected double xOutput = 0;
    protected double yOutput = 0;

    public Controller2D(){}

    public Controller2D(Controller1D xController, Controller1D yController){
        this.xController = xController;
        this.yController = yController;
    }

    public void setTarget(double[] target){
        xController.setTarget(target[0]);
        yController.setTarget(target[1]);
    }

    public void setProcessVariable(ReturnCodeSeg<Double> processVariableX, ReturnCodeSeg<Double> processVariableY){
        xController.setProcessVariable(processVariableX);
        yController.setProcessVariable(processVariableY);
    }

    public void reset(){
        super.reset();
        xController.reset();
        yController.reset();
    }
    
    public void setOutputX(double xOutput){
        this.xOutput = xOutput;
    }

    public void setOutputY(double yOutput){
        this.yOutput = yOutput;
    }

    public double getOutputX(){
        return xOutput;
    }

    public double getOutputY(){
        return yOutput;
    }

}

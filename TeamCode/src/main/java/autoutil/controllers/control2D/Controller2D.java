package autoutil.controllers.control2D;

import autoutil.controllers.control1D.Controller1D;
import geometry.framework.Point;
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

    public void setTarget(Point point){
        xController.setTarget(point.getX());
        yController.setTarget(point.getY());
    }

    public void setProcessVariable(ReturnCodeSeg<Double> processVariableX, ReturnCodeSeg<Double> processVariableY){
        xController.setProcessVariable(processVariableX);
        yController.setProcessVariable(processVariableY);
        setProcessVariable(() -> 0.0);
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

    @Override
    public void scale(double scale) { xController.scale(scale); yController.scale(scale); }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 0; }

    @Override
    protected double setDefaultAccuracy() { return 0; }

    @Override
    protected double setDefaultRestOutput() { return 0; }

    @Override
    protected double setOutput() { return 0; }

}

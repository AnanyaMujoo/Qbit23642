package autoutil.controllers.control2D;

import autoutil.controllers.control1D.Controller1D;
import autoutil.generators.Generator;
import autoutil.generators.LineGenerator;
import geometry.framework.Point;
import util.codeseg.CodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.fault;

public abstract class Controller2D extends Controller1D {
    public Controller1D xController;
    public Controller1D yController;
    protected double xOutput = 0;
    protected double yOutput = 0;
    protected double time = 100;

    public Controller2D(){}

    public void setTime(double time){ this.time = time; }

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
    
    public void setOutputX(double xOutput){ this.xOutput = xOutput; }

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
    public void scale(double scale) { xController.scale(scale); yController.scale(scale);  }

    @Override
    public void scaleAccuracy(double scale) { xController.scaleAccuracy(scale); yController.scaleAccuracy(scale); }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 0; }

    @Override
    protected double setDefaultAccuracy() { return 0; }

    @Override
    protected double setDefaultRestOutput() { return 0; }

    @Override
    protected double setOutput() { return 0; }

    public final boolean notAtTarget(){
        return !hasReachedTarget();
    }


    public <T extends Generator> void checkGenerator(Generator generator, Class<T> type, ParameterCodeSeg<T> code){
        if(type.isInstance(generator)){
            code.run((T) generator);
        }else{
            fault.check("Use " + type + " for " + this, Expectation.UNEXPECTED, Magnitude.CATASTROPHIC);
        }
    }
}

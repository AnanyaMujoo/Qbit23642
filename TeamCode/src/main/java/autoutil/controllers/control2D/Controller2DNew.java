package autoutil.controllers.control2D;

import autoutil.controllers.control1D.Controller1D;
import autoutil.generators.Generator;
import geometry.framework.Point;
import geometry.position.Pose;
import util.codeseg.ParameterCodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.fault;

public abstract class Controller2DNew {

    protected double xOutput = 0;
    protected double yOutput = 0;
    protected double accuracyScale = 0;
    protected double scale = 0;

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

    public void scale(double scale) { this.scale = scale; }

    public void scaleAccuracy(double scale) { this.accuracyScale = scale; }

    public abstract void updateController(Pose pose,Generator generator);

    public abstract boolean hasReachedTarget();

    public abstract void reset();


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

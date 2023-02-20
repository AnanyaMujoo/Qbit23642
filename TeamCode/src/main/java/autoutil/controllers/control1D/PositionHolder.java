package autoutil.controllers.control1D;

import autoutil.generators.Generator;
import geometry.position.Pose;
import math.trigonmetry.Trig;
import util.codeseg.ReturnCodeSeg;
import util.template.Precision;

public class PositionHolder extends Controller1D {

    private double restPower, extraRestPower, extraExtraRestPower, pCoefficient = 0;
    public final double deltaPowerUp = 0.007;
    public final double deltaPowerDown = -0.007;
    public final double velocityThreshold = Trig.rad(10);
    private volatile boolean isUsed, isTargeting = false;
    private double currentPosition = 0;
    private ReturnCodeSeg<Double> restPowerFunction = () -> restPower;



    public void deactivate(){ isUsed = false; isTargeting = false; }
    public void activate(){ isUsed = true; isTargeting = false; }

    public void activate(double currentPosition){ isUsed = true; isTargeting = true; this.currentPosition = currentPosition; }

    @Override
    public void setRestOutput(double restOutput) { this.restPower = restOutput; }

    public void setRestPowerFunction(ReturnCodeSeg<Double> restPowerFunction){ this.restPowerFunction = restPowerFunction; }
    public void setPCoefficient(double coefficient){ pCoefficient = coefficient; }

    @Override
    protected double setDefaultAccuracy() { return 0.5; }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 1; }

    @Override
    protected double setDefaultRestOutput() { return 0; }

    @Override
    protected void updateController(Pose pose, Generator generator) {
        if(isUsed) {
            if(isTargeting){
                double error = (getTarget()-currentPosition);
                if(error > 0.5){
                    extraExtraRestPower += 0.005;
                    Precision.clip(extraExtraRestPower, 0.2);
                }else if(error < -1.5){
                    extraExtraRestPower -= 0.001;
                    Precision.clip(extraExtraRestPower, 0.2);
                }
                extraRestPower = pCoefficient*error;
            }else{
//                if(!isWithinAccuracyRange() && Math.abs(getCurrentValue()) > velocityThreshold)
                extraRestPower = 0;
            }
        }
    }

    @Override
    protected double setOutput() { return isUsed ? restPowerFunction.run()+extraRestPower+extraExtraRestPower : 0; }

    @Override
    protected boolean hasReachedTarget() { return false; }
}

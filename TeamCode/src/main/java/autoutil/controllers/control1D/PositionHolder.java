package autoutil.controllers.control1D;

import autoutil.generators.Generator;
import geometry.position.Pose;

public class PositionHolder extends Controller1D {

    private final double velocityThreshold;
    private double restPower;
    private double extraRestPower;
    private final double deltaPowerUp;
    private final double deltaPowerDown;
    private volatile boolean isUsed, isTargeting = false;
    private double currentPosition = 0;

    public PositionHolder(double restPower, double deltaPowerUp, double deltaPowerDown,  double velocityThreshold){
        this.restPower = restPower; this.deltaPowerUp = deltaPowerUp; this.deltaPowerDown = deltaPowerDown; this.velocityThreshold = velocityThreshold;
    }

    public PositionHolder(double restPower){
        this.restPower = restPower; this.deltaPowerUp = 0.007; this.deltaPowerDown = -0.007; this.velocityThreshold = Math.toRadians(10);
    }

    public void deactivate(){ isUsed = false; isTargeting = false; }
    public void activate(){ isUsed = true; isTargeting = false; }
    public void activate(double currentPosition){ isUsed = true; isTargeting = true; this.currentPosition = currentPosition; }

    @Override
    public void setRestOutput(double restOutput) { this.restPower = restOutput; }

    @Override
    protected double setDefaultAccuracy() { return 0.5; }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 1; }

    @Override
    protected double setDefaultRestOutput() { return 0; }

    @Override
    protected void updateController(Pose pose, Generator generator) {
        if(isUsed) {
            if(!isWithinAccuracyRange() && isTargeting){
                double error = (getTarget()-currentPosition);
                extraRestPower = 0.03*error;
            }else if(Math.abs(getCurrentValue()) > velocityThreshold) {
                restPower += getCurrentValue() > 0 ? deltaPowerDown : deltaPowerUp;
            }
        }
    }

    @Override
    protected double setOutput() { return isUsed ? restPower+extraRestPower : 0; }

    @Override
    protected boolean hasReachedTarget() { return false; }
}

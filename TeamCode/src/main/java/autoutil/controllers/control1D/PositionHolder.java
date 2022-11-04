package autoutil.controllers.control1D;

import geometry.position.Pose;

public class PositionHolder extends Controller1D {

    private final double velocityThreshold;
    private double restPower;
    private final double deltaPowerUp;
    private final double deltaPowerDown;
    private volatile boolean isUsed = false;

    public PositionHolder(double restPower, double deltaPowerUp, double deltaPowerDown,  double velocityThreshold){
        this.restPower = restPower; this.deltaPowerUp = deltaPowerUp; this.deltaPowerDown = deltaPowerDown; this.velocityThreshold = velocityThreshold;
    }

    public PositionHolder(double restPower){
        this.restPower = restPower; this.deltaPowerUp = 0.003; this.deltaPowerDown = -0.003; this.velocityThreshold = Math.toRadians(10);
    }

    public void deactivate(){ isUsed = false; }
    public void activate(){ isUsed = true; }

    @Override
    public void setRestOutput(double restOutput) { this.restPower = restOutput; }

    @Override
    protected double setDefaultAccuracy() { return 0; }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 1; }

    @Override
    protected double setDefaultRestOutput() { return 0; }

    @Override
    protected void updateController(Pose pose, PathSegment pathSegment) {
        if(isUsed && Math.abs(getCurrentValue()) > velocityThreshold) {
            restPower += getCurrentValue() > 0 ? deltaPowerDown : deltaPowerUp;
        }
    }

    @Override
    protected double setOutput() { return isUsed ? restPower : 0; }

    @Override
    protected boolean hasReachedTarget() { return false; }
}

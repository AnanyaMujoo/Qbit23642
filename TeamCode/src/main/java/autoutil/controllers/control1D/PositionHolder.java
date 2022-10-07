package autoutil.controllers.control1D;

import autoutil.paths.PathSegment;
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
        this.restPower = restPower; this.deltaPowerUp = 0.005; this.deltaPowerDown = -0.005; this.velocityThreshold = Math.toRadians(10);
    }

    public void setRestPower(double restPower){
        this.restPower = restPower;
    }

    public void deactivate(){ isUsed = false; }

    public void activate(){ isUsed = true; }

    @Override
    protected void updateController(Pose pose, PathSegment pathSegment) {
        if(isUsed) {
            if (Math.abs(getCurrentValue()) > velocityThreshold) {
                restPower += getCurrentValue() > 0 ? deltaPowerDown : deltaPowerUp;
            }
            setOutput(restPower);
        }else{
            setOutput(0);
        }
    }
}

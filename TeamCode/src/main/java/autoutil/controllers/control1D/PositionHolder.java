package autoutil.controllers.control1D;

import autoutil.paths.PathSegment;
import geometry.position.Pose;

public class PositionHolder extends Controller1D {

    private final double velocityThreshold;
    private double restPower;
    private final double deltaPowerUp;
    private final double deltaPowerDown;

    // TODO 4 NEW Create position holder and test

    public PositionHolder(double restPower, double deltaPowerUp,double deltaPowerDown,  double velocityThreshold){
        this.restPower = restPower;
        this.deltaPowerUp = deltaPowerUp;
        this.deltaPowerDown = deltaPowerDown;
        this.velocityThreshold = velocityThreshold;
    }

    @Override
    public void update(Pose pose, PathSegment pathSegment) {
        updateProfilers();
        if(Math.abs(getVelocity()) > velocityThreshold) {
            if(getVelocity() > 0){
                restPower -= deltaPowerDown;
            }else{
                restPower += deltaPowerUp;
            }
        }
        setOutput(restPower);
    }

    public double getVelocity(){
        return processVariableProfiler.getDerivative();
    }
}

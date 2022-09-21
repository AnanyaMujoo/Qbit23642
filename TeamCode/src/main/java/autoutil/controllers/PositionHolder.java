package autoutil.controllers;

import autoutil.paths.PathSegment2;
import geometry.position.Pose;

public class PositionHolder extends Controller1D{

    private final double velocityThreshold;
    private double restPower;
    private final double deltaPowerUp;
    private final double deltaPowerDown;

    public PositionHolder(double restPower, double deltaPowerUp,double deltaPowerDown,  double velocityThreshold){
        this.restPower = restPower;
        this.deltaPowerUp = deltaPowerUp;
        this.deltaPowerDown = deltaPowerDown;
        this.velocityThreshold = velocityThreshold;
    }

    @Override
    public void update(Pose pose, PathSegment2 pathSegment) {
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

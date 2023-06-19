package autoutil.controllers.control1D;

import autoutil.generators.Generator;
import geometry.position.Pose;
import util.Timer;

public class RV extends Controller1D{

    public Timer timer = new Timer();
    public double targetTime;
    public double kt;

    public final double maxAccel = 10000;

    public boolean stopMode = false;
    public double stopDis;


    public RV(double restPower, double kt){ setRestOutput(restPower); this.kt = kt; }


    @Override
    protected double setDefaultAccuracy() { return 0.25; }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 0.01; }

    @Override
    protected double setDefaultRestOutput() { return 0; }

    @Override
    protected void updateController(Pose pose, Generator generator) {}


    @Override
    protected double setOutput() {
        double velocity = Math.max(processVariableProfiler.getDerivative(), 0.1);

        double minDisToStop = (velocity*velocity)/(2*maxAccel);



//        double timeToTarget = getError()/velocity;
//
//        double timeRemaining = Math.max(targetTime - timer.seconds(), 0.1);

        double distanceRemaining = getError();
//
//        double targetVelocity = getError()/timeRemaining;

//        double deltaTime = (timeToTarget - timeRemaining)/timeToTarget;

        if(stopMode){
            return 0.2*distanceRemaining/stopDis;
//            double targetVelocity = velocity * distanceRemaining/stopDis;
//            return Math.signum(distanceRemaining) * (Math.abs(targetVelocity)-Math.abs(velocity))*kt;
        }else {
            if(Math.abs(distanceRemaining) < minDisToStop){
                stopMode = true;
                stopDis = minDisToStop;
            }
            return Math.signum(distanceRemaining)*0.5;
        }

    }

    @Override
    protected boolean hasReachedTarget() { return isWithinAccuracyRange(); }

    public void setTargetTime(double time){
        targetTime = time;
    }

    @Override
    public void setTarget(double targetValue) {
        stopMode = false;
        timer.reset();
        super.setTarget(targetValue);
    }
}

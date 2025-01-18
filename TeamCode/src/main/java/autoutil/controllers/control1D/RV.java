package autoutil.controllers.control1D;

import autoutil.generators.Generator;
import debugging.Fault;
import geometry.position.Pose;
import geometry.position.Vector;
import util.Timer;

public class RV extends Controller1D{

    public Timer timer = new Timer();
    public Timer time2 = new Timer();
    public double targetTime;
    public double kp;
    public double minVelocity;

    public boolean stopMode = false;
    public boolean endMode = false;
    public double stopDis = 0;
    public Vector velocityVector = new Vector();
    public Vector errorVector = new Vector();
    public double velocity = 0;
    public double initialVelocity = 0;
    public double lastPos = 0;
    public double lastTime = 0;
    public double accelPower = 1;
    public double stopPower = 1;
    public double stopConstant = 40;
    public boolean oneD = false;
    public double ratio = 1;
    public double maxTime = 0;

    public boolean isEndModeExit = false;

    public void setRatio(double ratio){
        this.ratio = ratio;
    }


    public RV(double kp, double restPower, double minVelocity, double ratio){
        setRestOutput(restPower); this.kp = kp;
        this.minVelocity = minVelocity;
        this.ratio = ratio;
    }

    public void scale(double scale){
        this.accelPower = scale;
        this.stopPower = scale*ratio;
    }


    public void setMinVelocity(double mv){
        minVelocity = mv;
    }


    @Override
    protected double setDefaultAccuracy() { return 0.5; }

    @Override
    protected double setDefaultMinimumTimeReachedTarget() { return 0.1; }

    @Override
    protected double setDefaultRestOutput() { return 0; }

    @Override
    protected void updateController(Pose pose, Generator generator) {}

    public void setVelocity(){
        double currentPos = getCurrentValue();
        double deltaPos = currentPos - lastPos;
        lastPos = currentPos;

        double currentTime = timer.seconds();
        double deltaTime = currentTime - lastTime;
        lastTime = currentTime;

        this.velocity = deltaPos/deltaTime;
    }
    public void setVelocity(Vector velocity){
        this.velocityVector = velocity;
        this.velocity = velocityVector.getLength();
    }

    public void setErrorVector(Vector errorVector){
        this.errorVector = errorVector;
    }

    public void setStopConstant(double stop){
        stopConstant = stop;
    }

    public void set1D(){
        oneD = true;
    }

    public void setMaxTime(double time){
        maxTime = time;
    }


    @Override
    protected double setOutput() {
        setVelocity();


        double minDisToStop = accelPower*stopConstant;


//
//        double minDisToStop = (velocity*velocity)/(2*maxAccel);



//        double timeToTarget = getError()/velocity;
//
//        double timeRemaining = Math.max(targetTime - timer.seconds(), 0.1);

        double distanceRemaining = getError();
//
//        double targetVelocity = getError()/timeRemaining;

//        double deltaTime = (timeToTarget - timeRemaining)/timeToTarget;

        if (stopMode) {

            if(endMode){
                if(oneD){
                    return kp * distanceRemaining;
                }else {
                    if (errorVector.getNormalizedDotProduct(velocityVector) > 0) {
                        return 0.5 * kp * distanceRemaining;
                    } else {
                        return kp * distanceRemaining;
                    }
                }
            }else{
                if(velocity < minVelocity){
                    endMode = true;
                }
                if(isEndModeExit && time2.seconds() > maxTime){
                    endMode = true;
                }
                return -stopPower;
            }
//
//            if(velocity > 20){
//                endMode = true;
//                return -0.5;
//            }else{
//
//                return 0.01*distanceRemaining;
//            }

//                double targetVelocity = initialVelocity * distanceRemaining / stopDis;
//                return 0.005 * distanceRemaining + 0.05*(targetVelocity-velocity);


//            double targetVelocity = velocity * distanceRemaining/stopDis;
//            return Math.signum(distanceRemaining) * (Math.abs(targetVelocity)-Math.abs(velocity))*kt;
        } else {
            if(!isEndModeExit) {
                if (Math.abs(distanceRemaining) < minDisToStop) {
                    stopMode = true;
                    stopDis = minDisToStop;
                    initialVelocity = velocity;
                }
            }else{
                if (isWithinAccuracyRange()) {
                    stopMode = true;
                    stopDis = minDisToStop;
                    initialVelocity = velocity;
                    if (maxTime > 0) {
                        time2.reset();
                    }
                }
            }
            return Math.signum(distanceRemaining)*accelPower;
        }

    }

    public void setToEndModeExit(){
        isEndModeExit = true;
    }

    @Override
    protected boolean hasReachedTarget() {
        if(!isEndModeExit){
            return isWithinAccuracyRange();
        }else{
            return endMode;
        }
    }

    public void setTargetTime(double time){
        targetTime = time;
    }



    @Override
    public void reset() {
        lastTime = 0; lastPos = 0; timer.reset();  endMode = false; stopMode = false;
        super.reset();
    }
}

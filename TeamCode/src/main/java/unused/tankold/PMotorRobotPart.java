package unused.tankold;


import java.util.Arrays;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Stop;
import robotparts.RobotPart;
import robotparts.electronics.positional.PMotor;
import util.codeseg.ReturnParameterCodeSeg;

@Deprecated
public abstract class PMotorRobotPart extends RobotPart {

    public PMotor[] motors;

    protected ReturnParameterCodeSeg<Double, Double>[] getTargets;

    protected boolean[] disabled = new boolean[3000];

    protected abstract double[] getRestPows();

    /**
     * Gets angle of the lift (overwritten by child classes)
     * Vertical lift is PI/2
     * @return angle in radians
     */
    protected abstract double getAngle();

    /**
     * Init method creates the lift motor and resets the encoder (done internally)
     */
    @Override
    public void init() {
        motors = getMotors();
        getTargets = getTargetConvertors();
        resetEncoder();

        Arrays.fill(disabled, false);
    }

    public abstract PMotor[] getMotors();

    public abstract ReturnParameterCodeSeg<Double, Double>[] getTargetConvertors();

    public abstract void move(double pow);

    /**
     * Gets the position of the lift (in ticks)
     * @return ticks
     */
    public double[] getPos(){
        double[] positions = new double[motors.length];
        for (int i = 0; i < motors.length; i++) {
            positions[i] = motors[i].getPosition();
        }
        return positions;
    }

    /**
     * Gets the current power of the motor
     * @return power
     */
    public double[] getPower() {
        double[] pows = new double[motors.length];
        for (int i = 0; i < motors.length; i++) {
            pows[i] = motors[i].getPower();
        }
        return pows;
    }

    /**
     * Resets the lift encoder
     */
    public void resetEncoder(){
        for (int i = 0; i < motors.length; i++) if (!disabled[i]) {
            motors[i].resetPosition();
        }
    }

    /**
     * Sets the lift target in cm
     * @param h
     */
    public void setTarget2(double h){
        for (int i = 0; i < motors.length; i++) if (!disabled[i]) {
            motors[i].setTarget(getTargets[i].run(h));
        }
    }

    /**
     * Initial to set the target
     * @param height
     * @return
     */
    public Initial initialSetTarget(double height){
        return new Initial(() -> setTarget2(getOverallTarget(height/ CM_PER_TICK())));
    }

    public double getOverallTarget(double in) { return in; }

    public abstract double CM_PER_TICK();

    /**
     * Gets the target of the lift (in cm)
     * @return
     */
    public double[] getTarget() {
        double[] targets = new double[motors.length];
        for (int i = 0; i < motors.length; i++) {
            targets[i] = motors[i].getTarget() * CM_PER_TICK();
        }
        return targets;
    }

    /**
     * Stops and resets the mode of the positional motor
     */
    public void stopAndResetMode() {
        for (PMotor li : motors) li.stopTarget();
    }

    public boolean hasReachedTarget() {
        boolean[] hasReachedEach = hasReachedTargetEach();
        for (int i = 0; i < motors.length; i++) if (!disabled[i]) {
            if (!hasReachedEach[i]) return false;
        }
        return true;
    }

    /**
     * Has the positional motor reached the target position?
     * @return hasReachedTarget
     */
    public boolean[] hasReachedTargetEach(){
        boolean[] reachedTarget = new boolean[motors.length];
        for (int i = 0; i < motors.length; i++) {
            reachedTarget[i] = motors[i].exitTarget();
        }
        return reachedTarget;
    }

    /**
     * Exit when the motors all reached the target
     * @return
     */
    public Exit exitReachedTarget(){return new Exit(this::hasReachedTarget);}


    /**
     * Stops the motors and reset the mode
     * @return
     */
    public Stop stopEncoder(){return new Stop(this::stopAndResetMode);}
}
package robotparts.hardware;

import static global.General.fault;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Arrays;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import autoutil.controllers.PositionHolder;
import elements.Level;
import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.positional.PMotor;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnParameterCodeSeg;

public class Lift extends RobotPart {
//
//    // TODO 4 NEW Make the lift classes again
//
//
//    public PositionHolder positionHolder;
//    private final double UP_SCALE_CONST = 0.96;
//    private Level levelMode = Level.TOP;
//
//
//    @Override
//    public void move(double p) {
//        if (p > 0) {
//            motorUp.setPower(p + getRestPows()[0]);
//            motorDown.setPower(0);
//        } else if (p < 0) {
//            motorUp.setPower(0);
//            motorDown.setPower(p + getRestPows()[1]);
//        } else {
//            motorUp.setPower(getRestPows()[0]);
//            motorDown.setPower(getRestPows()[1]);
//        }
//    }
//
//    @Override
//    public void moveDown(double p) {
//        if (p > 0) {
//            motorUp.setPower(p + getRestPows()[0]);
//            motorDown.setPower(0);
//        } else if (p < 0) {
//            motorUp.setPower(-0.005);
//            motorDown.setPower(p + getRestPows()[1]);
//        } else {
//            motorUp.setPower(getRestPows()[0]);
//            motorDown.setPower(getRestPows()[1]);
//        }
//    }
//
//
//    @Override
//    public void init() {
//        super.init();
//
//        positionHolder = new PositionHolder(0.0, 0.007, 0.003, 0.1);
//        positionHolder.setProcessVariable(this::getPositionUp);
//
//
//        // TOD4 FIX
//        // Problem with stall detection
////        motorUp.useStallDetector(0.2, getRestPows()[0], 200,0.03, 2);
////        motorDown.useStallDetector(0.2, getRestPows()[1],200,0.03, 2);
//    }
//
//    public double getVelocity() {
//        return positionHolder.getVelocity();
//    }
//
//    @Override
//    protected double[] getRestPows() {
//        return new double[]{0.12, -0.05};
//    }
//
//    @Override
//    protected double getAngle() {
//        return Math.PI / 4;
//    }
//
//    @Override
//    public double CM_PER_TICK() {
//        return 1 / Constants.LIFT_CM_TO_TICKS;
//    }
//
//    public Stage liftTime(double power, double time) {
//        return new Stage(
//                usePart(),
//                main(power),
//                exitTime(time),
//                stop(),
//                returnPart()
//        );
//    }
//
//
//    public Stage liftPow(double power) {
//        return new Stage(
//                usePart(),
//                main(power),
//                exitAlways(),
//                returnPart()
//        );
//    }
//
//    /**
//     * Lift to a certain position
//     *
//     * @param power
//     * @param height
//     * @return
//     */
//    public Stage liftEncoderUp(double power, double height) {
//        return new Stage(
//                usePart(),
//                initialSetTargetUp(height * UP_SCALE_CONST),
//                main(power),
//                exitReachedTargetUp(),
//                stopEncoder(),
//                returnPart()
//        );
//    }
//
//
//    public Stage liftEncoderDown(double power, double height) {
//        return new Stage(
//                usePart(),
//                initialSetTargetDown(height),
//                mainDown(power),
//                exitReachedTargetDown(),
//                stopEncoder(),
//                returnPart()
//        );
//    }
//
//    public double getPositionUp() {
//        return motorUp.getPosition() * CM_PER_TICK();
//    }
//
//    public double getPositionDown() {
//        return motorDown.getPosition() * CM_PER_TICK();
//    }
//
//
//    public void holdPosition() {
//        if (getPositionUp() > 10) {
//            if (motorUp.isAllowed() && motorDown.isAllowed()) {
//                positionHolder.update();
//                motorUp.setPower(positionHolder.getOutput() + getRestPows()[0]);
//                motorDown.setPower(getRestPows()[1]);
//            }
//        } else {
//            motorUp.setPower(0.08);
//            motorDown.setPower(-0.1);
//        }
////        motorUp.setPower(getRestPows()[0]);
//    }
//
//
//    public void cycleLevelUp() {
//        levelMode = nextLevel();
//    }
//
//    public void cycleLevelDown() {
//        cycleLevelUp();
//        cycleLevelUp();
//    }
//
//    public Level getLevelMode() {
//        return levelMode;
//    }
//
//    public void setLevelMode(Level level) {
//        levelMode = level;
//    }
//
//    private Level nextLevel() {
//        switch (levelMode) {
//            case TOP:
//                return Level.BOTTOM;
//            case MIDDLE:
//                return Level.TOP;
//            case BOTTOM:
//                return Level.MIDDLE;
//        }
//        return null;
//    }
//
//
//    @Override
//    public double getOverallTarget(double in) {
//        return in/Math.sin(getAngle());
//    }
//
//    @Override
//    public double CM_PER_TICK() {
//        return 1 / Constants.LIFT_CM_TO_TICKS;
//    }
//
//    /**
//     * Set the power of the lift in a main
//     * @param power
//     * @return
//     */
//    public Main main(double power){
//        return new Main(() -> move(power));
//    }
//
////    /**
////     * Exit when the lift is down
////     * NOTE: Uses the touch sensor
////     * @return
////     */
////    public Exit exitDown(){return new Exit(() -> bot.touchSensors.isOuttakePressingTouchSensor());}
//
//    /**
//     * Lift for a certain time
//     * @param power
//     * @param time
//     * @return
//     */
//    public Stage liftTime(double power, double time){return new Stage(
//            usePart(),
//            main(power),
//            exitTime(time),
//            stop(),
//            returnPart()
//    );}
//
//    /**
//     * Lift to a certain position
//     * @param power
//     * @param height
//     * @return
//     */
//    public Stage liftEncoder(double power, double height){return new Stage(
//            usePart(),
//            initialSetTarget(height),
//            main(power),
//            exitReachedTarget(),
//            stopEncoder(),
//            returnPart()
//    );}
//
//
//    public Stage liftEncoderCustom(double power, double height, CodeSeg custom){return new Stage(
//            usePart(),
//            initialSetTarget(height),
//            new Initial(custom),
//            main(power),
//            exitReachedTarget(),
//            stopEncoder(),
//            returnPart()
//    );}
//
//}
//
//
//
//@Deprecated
//public abstract class PMotorRobotPart extends RobotPart {
//
//    public PMotor[] motors;
//
//    protected ReturnParameterCodeSeg<Double, Double>[] getTargets;
//
//    protected boolean[] disabled = new boolean[3000];
//
//    protected abstract double[] getRestPows();
//
//    /**
//     * Gets angle of the lift (overwritten by child classes)
//     * Vertical lift is PI/2
//     * @return angle in radians
//     */
//    protected abstract double getAngle();
//
//    /**
//     * Init method creates the lift motor and resets the encoder (done internally)
//     */
//    @Override
//    public void init() {
//        motors = getMotors();
//        getTargets = getTargetConvertors();
//        resetEncoder();
//
//        Arrays.fill(disabled, false);
//    }
//
//    public abstract PMotor[] getMotors();
//
//    public abstract ReturnParameterCodeSeg<Double, Double>[] getTargetConvertors();
//
//    public abstract void move(double pow);
//
//    /**
//     * Gets the position of the lift (in ticks)
//     * @return ticks
//     */
//    public double[] getPos(){
//        double[] positions = new double[motors.length];
//        for (int i = 0; i < motors.length; i++) {
//            positions[i] = motors[i].getPosition();
//        }
//        return positions;
//    }
//
//    /**
//     * Gets the current power of the motor
//     * @return power
//     */
//    public double[] getPower() {
//        double[] pows = new double[motors.length];
//        for (int i = 0; i < motors.length; i++) {
//            pows[i] = motors[i].getPower();
//        }
//        return pows;
//    }
//
//    /**
//     * Resets the lift encoder
//     */
//    public void resetEncoder(){
//        for (int i = 0; i < motors.length; i++) if (!disabled[i]) {
//            motors[i].resetPosition();
//        }
//    }
//
//    /**
//     * Sets the lift target in cm
//     * @param h
//     */
//    public void setTarget(double h){
//        for (int i = 0; i < motors.length; i++) if (!disabled[i]) {
//            motors[i].setPosition(getTargets[i].run(h));
//        }
//    }
//
//    /**
//     * Initial to set the target
//     * @param height
//     * @return
//     */
//    public Initial initialSetTarget(double height){
//        return new Initial(() -> setTarget(getOverallTarget(height/ CM_PER_TICK())));
//    }
//
//    public double getOverallTarget(double in) { return in; }
//
//    public abstract double CM_PER_TICK();
//
//    /**
//     * Gets the target of the lift (in cm)
//     * @return
//     */
//    public double[] getTarget() {
//        double[] targets = new double[motors.length];
//        for (int i = 0; i < motors.length; i++) {
//            targets[i] = motors[i].getTarget() * CM_PER_TICK();
//        }
//        return targets;
//    }
//
//    /**
//     * Stops and resets the mode of the positional motor
//     */
//    public void stopAndResetMode() {
//        for (PMotor li : motors) li.stopAndReset();
//    }
//
//    public boolean hasReachedTarget() {
//        boolean[] hasReachedEach = hasReachedTargetEach();
//        for (int i = 0; i < motors.length; i++) if (!disabled[i]) {
//            if (!hasReachedEach[i]) return false;
//        }
//        return true;
//    }
//
//    /**
//     * Has the positional motor reached the target position?
//     * @return hasReachedTarget
//     */
//    public boolean[] hasReachedTargetEach(){
//        boolean[] reachedTarget = new boolean[motors.length];
//        for (int i = 0; i < motors.length; i++) {
//            reachedTarget[i] = motors[i].hasReachedPosition();
//        }
//        return reachedTarget;
//    }
//
//    /**
//     * Exit when the motors all reached the target
//     * @return
//     */
//    public Exit exitReachedTarget(){return new Exit(this::hasReachedTarget);}
//
//
//    /**
//     * Stop the motors
//     * @return
//     */
//    public Stop stop(){return new Stop(() -> move(0));}
//
//    /**
//     * Stops the motors and reset the mode
//     * @return
//     */
//    public Stop stopEncoder(){return new Stop(this::stopAndResetMode);}
//
//
//
//    public PMotor motorUp;
//    public PMotor motorDown;
//
//    protected abstract double[] getRestPows();
//
//    protected abstract double getAngle();
//
//    public abstract double CM_PER_TICK();
//
//    public abstract void move(double pow);
//    public abstract void moveDown(double pow);
//
//    @Override
//    public void init() {
//        motorUp = create("lil", ElectronicType.PMOTOR_REVERSE);
//        motorDown = create("lir", ElectronicType.PMOTOR_REVERSE_FLOAT);
//        resetEncoder();
//    }
//
//    public void resetEncoder(){
//        motorUp.resetPosition();
//        motorDown.resetPosition();
//    }
//
//    public Initial initialSetTargetUp(double height){
//        return new Initial(() -> motorUp.setPosition(height/ CM_PER_TICK()));
//    }
//
//    public Initial initialSetTargetDown(double height){
//        return new Initial(() -> motorDown.setPosition(height/ CM_PER_TICK()));
//    }
//
//    public void stopAndResetMode() {
//        motorUp.stopAndReset();
//        motorDown.stopAndReset();
//    }
//
//    public boolean hasReachedTargetUp() {
//        return motorUp.hasReachedPosition();
//    }
//
//    public boolean hasReachedTargetDown() {
//        return motorDown.hasReachedPosition();
//    }
//
//    public Main mainDown(double power){
//        return new Main(() -> moveDown(power));
//    }
//    public Main main(double power){
//        return new Main(() -> move(power));
//    }
//
//    public Exit exitReachedTargetUp(){return new Exit(this::hasReachedTargetUp);}
//    public Exit exitReachedTargetDown(){return new Exit(this::hasReachedTargetDown);}
//
//    public Stop stop(){return new Stop(() -> move(0));}
//
//    public Stop stopEncoder(){return new Stop(this::stopAndResetMode);}

}


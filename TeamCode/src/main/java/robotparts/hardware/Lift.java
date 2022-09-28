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

    // TODO 4 FIX Finish this

    public PositionHolder positionHolder;
    private final double UP_SCALE_CONST = 0.96;

    public PMotor motorUp;
    public PMotor motorDown;

    public double restPowUp = 0.12;
    public double restPowDown = -0.05;

    public double angle = Math.PI / 4.0;
    public double cm_per_tick = 1.0 / Constants.LIFT_CM_TO_TICKS;;


    @Override
    public void init() {
        motorUp = create("lil", ElectronicType.PMOTOR_REVERSE);
        motorDown = create("lir", ElectronicType.PMOTOR_REVERSE_FLOAT);
        positionHolder = new PositionHolder(0.0, 0.007, 0.003, 0.1);
//        positionHolder.setProcessVariable(this::getPositionUp);
    }


    @Override
    public void move(double p) {
        motorUp.setPower(restPowUp + p>0?p:(p<0?-restPowUp:0));
        motorDown.setPower(restPowDown + p<0?p:(p>0?-restPowDown:0));
    }

    @Override
    protected Stage moveTime(double p, double t) {
        return super.moveTime(p, t);
    }

    @Override
    protected Stage moveNow(double p) {
        return super.moveNow(p);
    }

//
//
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
//    @Override
//    public double getOverallTarget(double in) {
//        return in/Math.sin(angle);
//    }



//
//
//    /**
//     * Initial to set the target
//     * @param height
//     * @return
//     */
//    public Initial initialSetTarget(double height){
//        return new Initial(() -> setTarget(getOverallTarget(height/ CM_PER_TICK())));
//    }

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
//    public void resetEncoder(){
//
//        motorUp.resetPosition();
//        motorDown.resetPosition();
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


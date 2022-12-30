package robotparts.hardware;

import automodules.stage.Main;
import automodules.stage.Stage;
import global.Constants;
import global.Modes;
import robot.BackgroundTask;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;
import robotparts.electronics.positional.PServo;
import util.User;

import static global.Modes.gameplayMode;
import static global.Modes.heightMode;

public class Lift extends RobotPart {

    public PMotor motorRight;
    public PMotor motorLeft;

    public static final double maxPosition = 61;
    public int stackedMode = 0;
    public volatile double cutoffPosition = 10;
    public boolean circuitMode = false;

    @Override
    public void init() {
        motorRight = create("lil", ElectronicType.PMOTOR_FORWARD);
        motorLeft = create("lir", ElectronicType.PMOTOR_REVERSE);
        motorRight.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.79, 0.25, 5);
        motorLeft.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.79, 0.25, 5);
        motorRight.usePositionHolder(0.18, 0.18);
        motorLeft.usePositionHolder(0.18, 0.18);
        heightMode.set(Modes.Height.HIGH);
        stackedMode = 0;
        circuitMode = false;
        gameplayMode.set(Modes.GameplayMode.CYCLE);
    }


    public Stage changeCutoff(double cutoffPosition){
        return customTime(() -> this.cutoffPosition = cutoffPosition, 0.01);
    }


    @Override
    public void move(double p) {
        motorRight.moveWithPositionHolder(p, cutoffPosition, 0.15);
        motorLeft.moveWithPositionHolder(p, cutoffPosition, 0.15);
    }

    public void setHolderTarget(Modes.Height height){
        Modes.heightMode.set(height);
        setHolderTarget(heightMode.getValue());
    }

    public void setHolderTarget(double height){
        motorRight.setPositionHolderTarget(height); motorLeft.setPositionHolderTarget(height);
    }

    public void adjustHolderTarget(double delta){
        motorRight.setPositionHolderTarget(motorRight.getPositionHolder().getTarget()+delta); motorLeft.setPositionHolderTarget(motorRight.getPositionHolder().getTarget()+delta);
    }

    @Override
    public Stage moveTime(double p, double t) {
        return super.moveTime(p, t);
    }

    public Stage stageLift(double power, double target) { return moveTarget(() -> motorRight, () -> motorLeft, power, power, target); }

    @Override
    public void maintain() { super.maintain(); }


    public Stage resetLift(){
        return new Stage(usePart(), new Main(() -> {motorRight.resetPosition(); motorLeft.resetPosition();}), exitTime(0.1), stop(), returnPart());
    }
}


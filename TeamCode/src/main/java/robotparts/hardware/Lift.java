package robotparts.hardware;

import automodules.AutoModule;
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
import util.codeseg.CodeSeg;
import util.template.Precision;

import static global.General.bot;
import static global.Modes.AttackMode.ON_BY_DEFAULT;
import static global.Modes.AttackMode.PRESS_TO_ENABLE;
import static global.Modes.Height.GROUND;
import static global.Modes.Height.LOW;
import static global.Modes.attackMode;
import static global.Modes.gameplayMode;
import static global.Modes.heightMode;

public class Lift extends RobotPart {

    public PMotor motorRight;
    public PMotor motorLeft;

    public static final double maxPosition = 61;
    public final double cutoffPosition = 10;
    public volatile double currentCutoffPosition = 10;
    public int stackedMode = 0;
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
        circuitMode = false;
        gameplayMode.set(Modes.GameplayMode.CYCLE);
        stackedMode = 0;
        attackMode.set(PRESS_TO_ENABLE);
    }


    public Stage changeCutoff(double cutoffPosition){
        return customTime(() -> currentCutoffPosition = cutoffPosition, 0.01);
    }
    public Stage resetCutoff(){ return customTime( () -> {if(!heightMode.modeIs(GROUND) && !heightMode.modeIs(LOW)){ currentCutoffPosition = cutoffPosition; }}, 0.01); }


    @Override
    public void move(double p) {
        motorRight.moveWithPositionHolder(p, currentCutoffPosition, 0.2);
        motorLeft.moveWithPositionHolder(p, currentCutoffPosition, 0.2);
    }

    public void adjustHolderTarget(double delta){
        currentCutoffPosition = 0;
        motorRight.holdPositionExact();
        motorLeft.holdPositionExact();
        double target = Precision.clip(motorRight.getPositionHolder().getTarget()+delta, 0, maxPosition);
        motorRight.setPositionHolderTarget(target);
        motorLeft.setPositionHolderTarget(target);
    }

    @Override
    public Stage moveTime(double p, double t) {
        return super.moveTime(p, t);
    }

    public Stage stageLift(double power, double target) { return moveTarget(() -> motorRight, () -> motorLeft, power, power, target); }

    @Override
    public void maintain() { super.maintain(); }

    public Stage resetLift(){ return new Stage(usePart(), new Main(() -> {motorRight.resetPosition(); motorLeft.resetPosition();}), exitTime(0.1), stop(), returnPart()); }
}


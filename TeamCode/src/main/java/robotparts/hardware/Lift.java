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

import static global.Modes.HeightMode.Height.HIGH;

public class Lift extends RobotPart {

    public PMotor motorRight;
    public PMotor motorLeft;

    public static final double maxPosition = 66;

    @Override
    public void init() {
        motorRight = create("lil", ElectronicType.PMOTOR_REVERSE);
        motorLeft = create("lir", ElectronicType.PMOTOR_FORWARD);
        motorRight.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.79, 0.25, 5);
        motorLeft.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.79, 0.25, 5);
        motorRight.usePositionHolder(0.0, 0.05);
        motorLeft.usePositionHolder(0.0, 0.05);
        Modes.heightMode.set(HIGH);
    }


    @Override
    public void move(double p) {
        motorRight.moveWithPositionHolder(p, 6, 0.1);
        motorLeft.moveWithPositionHolder(p, 6, 0.1);
    }

    @Override
    protected Stage moveTime(double p, double t) {
        return super.moveTime(p, t);
    }

    public Stage stageLift(double power, double target) { return moveTarget(() -> motorRight, () -> motorLeft, power, power, target); }

    @Override
    public void maintain() { super.maintain(); }
}


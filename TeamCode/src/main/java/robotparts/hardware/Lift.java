package robotparts.hardware;

import automodules.stage.Stage;
import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;

public class Lift extends RobotPart {

    public PMotor motorRight;
    public PMotor motorLeft;

    public double restPowUp = 0.01;

    @Override
    public void init() {
        motorRight = create("lil", ElectronicType.PMOTOR_REVERSE);
        motorLeft = create("lir", ElectronicType.PMOTOR_FORWARD);
        motorRight.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.5, 0.25, 5);
        motorLeft.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.5, 0.25, 5);
        motorRight.usePositionHolder(restPowUp);
    }


    @Override
    public void move(double p) {
        if (p != 0) {
            motorRight.releasePosition();
            motorRight.move(p+restPowUp);
        } else {
            motorRight.holdPosition();
            motorLeft.move(0);
        }
    }

    @Override
    protected Stage moveTime(double p, double t) {
        return super.moveTime(p, t);
    }

    @Override
    protected Stage moveNow(double p) {
        return super.moveNow(p);
    }

    public Stage stageLift(double power, double target) { return moveTarget(() -> motorRight, () -> motorLeft, power, power, target); }

}


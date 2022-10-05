package robotparts.hardware;

import automodules.stage.Stage;
import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;

public class Lift extends RobotPart {

    public PMotor motorUp;
    public PMotor motorDown;

    public double restPowUp = 0.12;
    public double restPowDown = -0.05;

    // TODO 4 FIX Rest pow here

    ;
    @Override
    public void init() {
        motorUp = create("lil", ElectronicType.PMOTOR_REVERSE);
        motorDown = create("lir", ElectronicType.PMOTOR_REVERSE_FLOAT);
        motorUp.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.5, 0.333, 45);
        motorDown.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.5, 0.333, 45);
//        motorUp.setPositionHolder(new PositionHolder(0.0, 0.007, 0.003, 0.1));
    }


    @Override
    public void move(double p) {
        if (p != 0) {
            motorUp.move(p > 0 ? p + restPowUp : 0);
            motorDown.move(p < 0 ? p + restPowDown : 0);
        } else {
            if (motorUp.getPosition() > 10) {
                motorUp.move(restPowUp);
                motorDown.move(restPowDown);
//                motorUp.setPowerAdjusted(restPowUp);
            } else {
                motorUp.move(0.08);
                motorDown.move(-0.1);
            }
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

    public Stage stageLift(double power, double target) {
        return moveTarget(() -> motorUp, () -> motorDown, power, power>0?power:power*0.5, target);
    }

}


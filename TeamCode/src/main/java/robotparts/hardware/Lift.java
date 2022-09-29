package robotparts.hardware;

import automodules.stage.Stage;
import autoutil.controllers.PositionHolder;
import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;

public class Lift extends RobotPart {

    public PMotor motorUp;
    public PMotor motorDown;

    public double restPowUp = 0.12;
    public double restPowDown = -0.05;

    ;
    @Override
    public void init() {
        motorUp = create("lil", ElectronicType.PMOTOR_REVERSE);
        motorDown = create("lir", ElectronicType.PMOTOR_REVERSE_FLOAT);
        motorUp.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.5, 3, 45);
        motorDown.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.5, 3, 45);
        motorUp.setPositionHolder(new PositionHolder(0.0, 0.007, 0.003, 0.1));
    }


    @Override
    public void move(double p) {
        if (p != 0) {
            motorUp.setPower(p > 0 ? p + restPowUp : 0);
            motorDown.setPower(p < 0 ? p + restPowDown : 0);
        } else {
            if (motorUp.getPosition() > 10) {
                if (motorUp.isAllowed() && motorDown.isAllowed()) {
                    motorUp.setPowerAdjusted(restPowUp);
                    motorDown.setPower(restPowDown);
                }
            } else {
                motorUp.setPower(0.08);
                motorDown.setPower(-0.1);
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

    @Override
    public Stage moveTarget(double power, double target) {
        if(power > 0) {
            return motorUp.moveTarget(power, target);
        }else{
            return motorDown.moveTarget(power, target);
        }
    }

}


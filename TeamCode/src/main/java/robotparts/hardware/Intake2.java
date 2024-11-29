package robotparts.hardware;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;

public class Intake2 extends RobotPart {
    public CMotor intake;
    public CMotor intake2;
    @Override
    public void init() {
        intake = create("in", ElectronicType.CMOTOR_REVERSE_FLOAT);
        intake2 = create("in2", ElectronicType.CMOTOR_REVERSE_FLOAT);
    }

    @Override
    public void move(double intakePower) {
        intake.setPower(intakePower);
        intake2.setPower(intakePower);
    }


    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }
}
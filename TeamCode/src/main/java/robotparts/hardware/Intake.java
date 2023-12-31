package robotparts.hardware;

import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;

public class Intake extends RobotPart {
    public CMotor intake;
    @Override
    public void init() {
        intake = create("in", ElectronicType.CMOTOR_REVERSE_FLOAT);

    }
    @Override
    public void move(double intakePower) {
        intake.setPower(intakePower);
    }
}

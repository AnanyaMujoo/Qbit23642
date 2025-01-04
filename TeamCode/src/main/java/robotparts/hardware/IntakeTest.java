package robotparts.hardware;

import java.util.concurrent.*;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.positional.PServo;

public class IntakeTest extends RobotPart {
    public CServo intake;
    public CServo intake2;
    public PServo intake3;
    public PServo intake4;
    @Override
    public void init() {
        intake = create("ils", ElectronicType.CSERVO_REVERSE);
        intake2 = create("irs", ElectronicType.CSERVO_FORWARD);
        intake3 = create("idl", ElectronicType.PSERVO_REVERSE);
        intake4 = create("idr", ElectronicType.PSERVO_FORWARD);
        //not configured

    }



    @Override
    public void move(double intakePower) {
        intake.setPower(intakePower);
        intake2.setPower(intakePower);
    }

    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }
}

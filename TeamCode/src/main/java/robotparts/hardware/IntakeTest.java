package robotparts.hardware;

import java.util.concurrent.*;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.continuous.CServo;

public class IntakeTest extends RobotPart {
    public CServo intake;
    public CServo intake2;
    @Override
    public void init() {
        intake = create("in", ElectronicType.CSERVO_REVERSE);
        intake2 = create("in2", ElectronicType.CSERVO_FORWARD);


    }
    private boolean didThePixelGoIn(){
        return false;
    }



    @Override
    public void move(double intakePower) {
        intake.setPower(intakePower);
        intake2.setPower(intakePower);
    }


    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }
}

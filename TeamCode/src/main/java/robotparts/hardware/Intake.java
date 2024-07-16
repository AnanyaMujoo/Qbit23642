package robotparts.hardware;

import java.util.concurrent.*;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;

public class Intake extends RobotPart {
    public CMotor intake;
    @Override
    public void init() {
        intake = create("in", ElectronicType.CMOTOR_REVERSE_FLOAT);

    }
    private boolean didThePixelGoIn(){
        return false;
    }



    @Override
    public void move(double intakePower) {
                intake.setPower(intakePower);
        }


    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }
}

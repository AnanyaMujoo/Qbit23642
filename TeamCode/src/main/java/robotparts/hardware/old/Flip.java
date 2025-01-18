package robotparts.hardware.old;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.positional.PServo;

public class Flip extends RobotPart {
    public CServo flipr;
    public CServo flipl;
    @Override
    public void init() {
        flipr = create("idr", ElectronicType.CSERVO_REVERSE);
        flipl = create("idl", ElectronicType.CSERVO_FORWARD);

        //not configured

    }



    @Override
    public void move(double intakePower) {
        flipr.setPower(intakePower);
        flipl.setPower(intakePower);
    }

    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }

}

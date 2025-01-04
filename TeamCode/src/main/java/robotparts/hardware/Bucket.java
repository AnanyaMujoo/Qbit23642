package robotparts.hardware;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.continuous.CServo;

public class Bucket extends RobotPart {
    public CServo bucket1;
    public CServo bucket2;
    @Override
    public void init() {
        bucket1 = create("rs", ElectronicType.CSERVO_REVERSE);
        bucket2 = create("ls", ElectronicType.CSERVO_FORWARD);


    }



    @Override
    public void move(double outtakePower) {
        bucket1.setPower(outtakePower);
    }


    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }
}

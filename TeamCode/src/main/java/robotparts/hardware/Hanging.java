package robotparts.hardware;

import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.positional.PMotor;
import robotparts.electronics.positional.PServo;
public class Hanging extends RobotPart {

    public PMotor hang;
    public PServo hangServo;


    @Override
    public void init() {
        hang = create("hang", ElectronicType.PMOTOR_FORWARD);
        hang.setToRotational(Constants.ORBITAL_ENCODER_TICKS_PER_REVOLUTION, 7);
        hangServo = create("hangservo", ElectronicType.PSERVO_FORWARD);
        hangServo.setPosition("start", 0);
        hangServo.setPosition("end", 0.65);
    }

    @Override
    public void move(double hangPower) {
        hang.setPower(hangPower);
    }

    public void moveHangStart() {
        hangServo.moveToPosition("start");
    }
    public void moveHangEnd() {
        hangServo.moveToPosition("end");
    }


}
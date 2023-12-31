package robotparts.hardware;

import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.positional.PMotor;

public class Hanging extends RobotPart {

    public PMotor hang;

    @Override
    public void init() {
        hang = create("hang", ElectronicType.PMOTOR_FORWARD);
        hang.setToRotational(Constants.ORBITAL_ENCODER_TICKS_PER_REVOLUTION, 7);
    }

    @Override
    public void move(double hangPower) {
        hang.setPower(hangPower);
    }

}

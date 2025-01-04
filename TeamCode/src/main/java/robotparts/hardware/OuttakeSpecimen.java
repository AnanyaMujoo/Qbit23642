package robotparts.hardware;

import java.util.concurrent.*;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.positional.PServo;

public class OuttakeSpecimen extends RobotPart {
    public PServo outtake1;
    public PServo outtake2;

    @Override
    public void init() {
        outtake1 = create("rc", ElectronicType.PSERVO_REVERSE);
        outtake1 = create("lc", ElectronicType.PSERVO_REVERSE);
        //outtake 2 not configured yet

    }



}

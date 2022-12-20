package unittests.tele.framework.movement;

import debugging.StallDetector;
import robotparts.electronics.continuous.CMotor;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;

import static global.General.gph1;
import static global.General.log;

public class StallDetectorTest extends TeleUnitTest {

//    private final MecanumLift part = mecanumLift;
//    private final PMotor motor = part.motorUp;
//    private final StallDetector detector = motor.getStallDetector();

//    private final MecanumCarousel part = MECANUM_CAROUSEL;
//    private final CMotor motor = part.car;
//    private final StallDetector detector = motor.getStallDetector();

    @Override
    public void init() {
//        detector.setCustomThresholds(10, 3);
//        motor.useStallDetector();
//        gph1.link(Button.B, part.MoveTime(1.0,5.0));
    }

    @Override
    protected void loop() {
//        part.move(gph1.ry);
//        log.show("Speed (deg/s)", detector.getMotorSpeed());
//        log.show("Current (amps)", detector.getMotorCurrent());
    }
}

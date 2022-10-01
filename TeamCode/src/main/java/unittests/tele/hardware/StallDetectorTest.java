package unittests.tele.hardware;

import debugging.StallDetector;
import robotparts.RobotPart;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.positional.PMotor;
import robotparts.hardware.Carousel;
import robotparts.hardware.Lift;
import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.gph1;
import static global.General.log;

public class StallDetectorTest extends TeleUnitTest {


    private final Lift part = lift;
    private final StallDetector detector = part.motorUp.getStallDetector();

    @Override
    public void init() {
        detector.setCustomThresholds(10, 3);
    }

    @Override
    protected void loop() {
        part.move(gph1.ry);
        log.show("Speed (deg/s)", detector.getMotorSpeed());
        log.show("Current (amps)", detector.getMotorCurrent());
        log.show("Stalling", detector.isStalling());
    }
}

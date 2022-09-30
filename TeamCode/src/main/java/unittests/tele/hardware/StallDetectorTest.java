package unittests.tele.hardware;

import robotparts.RobotPart;
import robotparts.electronics.positional.PMotor;
import robotparts.hardware.Carousel;
import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.gph1;
import static global.General.log;

public class StallDetectorTest extends TeleUnitTest {


    private final Carousel part = carousel;

    @Override
    protected void loop() {
        carousel.move(gph1.ry);
        log.show("Stalling", carousel.car.detector.isStalling());
    }
}

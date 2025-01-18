package unittests.tele.sensor;

import unittests.tele.TeleUnitTest;
import static global.General.*;

public class GyroTest extends TeleUnitTest {
    /**
     * Tests gyro sensors
     */
    @Override
    protected void loop() {
        /**
         * These should range from -180 to 180 and are in degrees
         */
        log.show("Right gyro reading", gyro.getHeading());

        drive.move(0, 0, 0.5*gph1.lx);
//        log.show("Left gyro reading", bot.gyro.getLeftHeadingDeg());
    }
}

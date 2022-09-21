package unittests.tele.sensor;

import unittests.tele.TeleUnitTest;
import static global.General.*;

public class TouchTest extends TeleUnitTest {
    /**
     * Test the touch sensors
     */
    @Override
    protected void loop() {
        /**
         * This should change when the touch sensor is touched
         */
        log.show("Touch sensor reading, is pressed?", bot.touchSensors.isOuttakePressingTouchSensor());
    }
}

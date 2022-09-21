package unittests.tele.sensor;


import unittests.tele.TeleUnitTest;
import static global.General.*;


public class DistanceTest extends TeleUnitTest {
    /**
     * Tests distance sensors
     */

    @Override
    protected void loop() {
        /**
         * These should all change when something goes infront of the distance sensors
         * NOTE: The readings are in centimeters
         */
        log.show("Left distance sensor reading", bot.distanceSensors.getLeftDistance());
        log.show("Right distance sensor reading", bot.distanceSensors.getRightDistance());
        log.show("Front left distance sensor reading", bot.distanceSensors.getFrontLeftDistance());
        log.show("Front right distance sensor reading", bot.distanceSensors.getFrontRightDistance());
        log.show("Intake distance sensor reading", bot.distanceSensors.getOuttakeDistance());
    }
}

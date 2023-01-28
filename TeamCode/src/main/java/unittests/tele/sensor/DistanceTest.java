package unittests.tele.sensor;


import teleutil.button.Button;
import unittests.tele.TeleUnitTest;
import static global.General.*;


public class DistanceTest extends TeleUnitTest {
    @Override
    protected void start() {
        distanceSensors.ready();
        gph1.link(Button.A, distanceSensors::ready);
    }

    /**
     * Tests distance sensors
     */



    @Override
    protected void loop() {
        /**
         * These should all change when something goes infront of the distance sensors
         * NOTE: The readings are in centimeters
         */
        log.show("Right distance sensor reading", distanceSensors.getRightDistance());
        log.show("Front distance sensor reading", distanceSensors.getFrontDistance());

        log.show("Odometry Pose", odometry.getPose());


    }
}

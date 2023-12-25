package unittests.tele.framework.movement;
import automodules.stage.Stage;
import robotparts.RobotPart;
import unittests.tele.TeleUnitTest;

import static global.General.bot;

public class StageTest extends TeleUnitTest {

    /**
     * Tests custom stages (or modules)
     */

    @Override
    protected void start() {
        /**
         * The robot should go backward (1s) then forward (1s) then backward (1s)
         * NOTE: The robotfunction waits for 1 second and then moves the robot forward at 0.3 power for 1 second
         */
        bot.rfsHandler.addToQueue(new Stage(
                RobotPart.exitTime(1)
        ));
        bot.rfsHandler.addToQueue(drive.moveTime(0.3, 0,0, 1.0));
    }

    /**
     * Try to move the robot the other way in the normal loop method
     * NOTE: This results in the robot going backward, forward, and then backward again
     */
    @Override
    protected void loop() {
        drive.move(-0.3, 0, 0);
    }

    @Override
    public void stop() {
        drive.halt();
    }
}

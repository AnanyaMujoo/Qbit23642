package unittests.tele.framework.movement;
import automodules.stage.Stage;
import automodules.stage.Stop;
import robotparts.RobotPart;
import robotparts.hardware.mecanum.MecanumDrive;
import robotparts.unused.CustomTestPart;
import unittests.tele.TeleUnitTest;

import static global.General.bot;

public class StageTest extends TeleUnitTest {
    /**
     * Tests custom stages (or modules)
     */
    private final MecanumDrive part = drive;

    @Override
    protected void start() {
        /**
         * The robot should go backward (1s) then forward (1s) then backward (1s)
         * NOTE: The robotfunction waits for 1 second and then moves the robot forward at 0.3 power for 1 second
         */
        bot.rfsHandler.addToQueue(new Stage(
                RobotPart.exitTime(1)
        ));
        bot.rfsHandler.addToQueue(new Stage(
                part.usePart(),
                part.mainMove(0.3, 0.0, 0.0),
                RobotPart.exitTime(1),
                part.stopMove(),
                part.returnPart()
        ));
    }

    /**
     * Try to move the robot the other way in the normal loop method
     * NOTE: This results in the robot going backward, forward, and then backward again
     */
    @Override
    protected void loop() {
        part.move(-0.3, 0, 0);
    }

    @Override
    public void stop() {
        part.move(0,0, 0);
    }
}

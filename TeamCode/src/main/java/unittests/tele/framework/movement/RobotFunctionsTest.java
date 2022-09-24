package unittests.tele.framework.movement;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import robotparts.hardware.mecanum.MecanumDrive;
import robotparts.unused.CustomTestPart;
import unittests.tele.TeleUnitTest;
import util.User;

import static global.General.bot;
import static global.General.mainUser;

public class RobotFunctionsTest extends TeleUnitTest {
    /**
     * Tests robotfunctions directly
     */

    private final MecanumDrive part = drive;


    @Override
    public void start() {
        /**
         * Move the robot forward (1s) , then backward (1s), then turn clockwise (1s)
         * NOTE: This uses custom stage components
         */
        bot.rfsHandler.addToQueue(new Stage(
                new Initial(() -> part.switchUser(User.ROFU)),
                new Main(() -> part.move(0.3, 0, 0)),
                new Exit(() -> bot.rfsHandler.timer.seconds() > 1),
                new Stop(() -> part.move(0,0, 0)),
                new Stop(() -> part.switchUser(mainUser))
        ));
        bot.rfsHandler.addToQueue(new Stage(
                new Initial(() -> part.switchUser(User.ROFU)),
                new Main(() -> part.move(-0.3, 0, 0)),
                new Exit(() -> bot.rfsHandler.timer.seconds() > 1),
                new Stop(() -> part.move(0,0, 0)),
                new Stop(() -> part.switchUser(mainUser))
        ));
        bot.rfsHandler.addToQueue(new Stage(
                new Initial(() -> part.switchUser(User.ROFU)),
                new Main(() -> part.move(0, 0, 0.5)),
                new Exit(() -> bot.rfsHandler.timer.seconds() > 1),
                new Stop(() -> part.move(0,0, 0)),
                new Stop(() -> part.switchUser(mainUser))
        ));
    }
}

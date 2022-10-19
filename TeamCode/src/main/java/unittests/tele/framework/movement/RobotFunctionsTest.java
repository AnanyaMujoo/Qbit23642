package unittests.tele.framework.movement;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import unused.mecanumold.MecanumDrive;
import unittests.tele.TeleUnitTest;
import util.User;

import static global.General.bot;
import static global.General.mainUser;

public class RobotFunctionsTest extends TeleUnitTest {
    /**
     * Tests robotfunctions directly
     */

    @Override
    protected MecanumDrive getTestPart() {
        return mecanumDrive;
    }

    @Override
    public void start() {
        /**
         * Move the robot forward (1s) , then backward (1s)
         * NOTE: This uses custom stage components
         */
        bot.rfsHandler.addToQueue(new Stage(
                new Initial(() -> getTestPart().switchUser(User.ROFU)),
                new Main(() -> getTestPart().move(0.3, 0, 0)),
                new Exit(() -> bot.rfsHandler.getTimer().seconds() > 1),
                new Stop(() -> getTestPart().move(0,0, 0)),
                new Stop(() -> getTestPart().switchUser(mainUser))
        ));
        bot.rfsHandler.addToQueue(new Stage(
                new Initial(() -> getTestPart().switchUser(User.ROFU)),
                new Main(() -> getTestPart().move(-0.3, 0, 0)),
                new Exit(() -> bot.rfsHandler.getTimer().seconds() > 1),
                new Stop(() -> getTestPart().move(0,0, 0)),
                new Stop(() -> getTestPart().switchUser(mainUser))
        ));
    }

    @Override
    public void stop() {
        mecanumDrive.halt();
    }
}

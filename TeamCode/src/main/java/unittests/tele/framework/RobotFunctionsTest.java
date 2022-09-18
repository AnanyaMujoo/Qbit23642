package unittests.tele.framework;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import unittests.tele.TeleUnitTest;
import util.User;

import static global.General.*;

public class RobotFunctionsTest extends TeleUnitTest {
    /**
     * Tests robotfunctions directly
     */
    @Override
    public void start() {
        /**
         * Move the robot forward (1s) , then backward (1s), then turn clockwise (1s)
         * NOTE: This uses custom stage components
         */
        // TODO 4 FIX Make this work
//        bot.rfsHandler.addToQueue(new Stage(
//                new Initial(() -> bot.tankDrive.switchUser(User.ROFU)),
//                new Main(() -> bot.tankDrive.move(0.3, 0)),
//                new Exit(() -> bot.rfsHandler.timer.seconds() > 1),
//                new Stop(() -> bot.tankDrive.move(0,0)),
//                new Stop(() -> bot.tankDrive.switchUser(mainUser))
//        ));
//        bot.rfsHandler.addToQueue(new Stage(
//                new Initial(() -> bot.tankDrive.switchUser(User.ROFU)),
//                new Main(() -> bot.tankDrive.move(-0.3, 0)),
//                new Exit(() -> bot.rfsHandler.timer.seconds() > 1),
//                new Stop(() -> bot.tankDrive.move(0,0)),
//                new Stop(() -> bot.tankDrive.switchUser(mainUser))
//        ));
//        bot.rfsHandler.addToQueue(new Stage(
//                new Initial(() -> bot.tankDrive.switchUser(User.ROFU)),
//                new Main(() -> bot.tankDrive.move(0, 0.5)),
//                new Exit(() -> bot.rfsHandler.timer.seconds() > 1),
//                new Stop(() -> bot.tankDrive.move(0,0)),
//                new Stop(() -> bot.tankDrive.switchUser(mainUser))
//        ));
    }
}

package unittests.tele.hardware;

import robotparts.electronics.continuous.CMotor;
import unittests.tele.TeleUnitTest;

import static global.General.*;

public class RobotPartTest extends TeleUnitTest {
    /**
     * Class that tests robot part by creating a test robot part
     */

    // TODO 4 FIX Make this work for any part

    /**
     * Uses the intake robotpart as the test part
     * NOTE: The robot must have an intake robotpart for this to work
     */
    @Override
    protected void loop() {
        /**
         * Should show config of test part properly (i.e. directions correct, run without encoder, etc.)
         */
//        showConfig(bot.tankIntake);
//        /**
//         * Should be 1
//         */
//        log.show("Test part electronics size", bot.tankIntake.getElectronicsOfType(CMotor.class).size());
//        /**
//         * Should be TELE
//         */
//        log.show("Test part current user", bot.tankIntake.getUser());
    }

    /**
     * When the program stops the logs should have the configuration of the intake
     */
}

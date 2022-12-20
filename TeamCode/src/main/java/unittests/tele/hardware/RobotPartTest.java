package unittests.tele.hardware;

import robot.RobotUser;
import robotparts.RobotPart;
import robotparts.electronics.continuous.CMotor;
import unittests.tele.TeleUnitTest;

import static global.General.*;

public class RobotPartTest extends TeleUnitTest {
    /**
     * Class that tests the test robot part
     */

    /**
     * Uses the test robot part
     */
    @Override
    protected void loop() {
        /**
         * Should show config of test part properly (i.e. directions correct, run without encoder, etc.)
         */

        showConfig(drive);
        /**
         * Should be 4
         */
        log.show("Test part electronics size {1}", drive.getElectronicsOfType(CMotor.class).size());
        /**
         * Should be TELE
         */
        log.show("Test part current user", drive.getUser());
    }

    /**
     * When the program stops the logs should have the configuration of the intake
     */
}

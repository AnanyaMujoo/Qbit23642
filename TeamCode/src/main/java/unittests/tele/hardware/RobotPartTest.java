package unittests.tele.hardware;

import robotparts.RobotPart;
import robotparts.electronics.continuous.CMotor;
import unittests.tele.TeleUnitTest;

import static global.General.*;

public class RobotPartTest extends TeleUnitTest {
    /**
     * Class that tests the test robot part
     */

    private final RobotPart part = mecanumIntake;

    /**
     * Uses the test robot part
     */
    @Override
    protected void loop() {
        /**
         * Should show config of test part properly (i.e. directions correct, run without encoder, etc.)
         */

        showConfig(part);
        /**
         * Should be 1
         */
        log.show("Test part electronics size {1}", part.getElectronicsOfType(CMotor.class).size());
        /**
         * Should be TELE
         */
        log.show("Test part current user", part.getUser());
    }

    /**
     * When the program stops the logs should have the configuration of the intake
     */
}

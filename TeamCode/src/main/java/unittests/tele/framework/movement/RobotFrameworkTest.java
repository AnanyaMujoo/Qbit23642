package unittests.tele.framework.movement;

import robot.RobotFramework;
import robotparts.RobotPart;
import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.log;

public class RobotFrameworkTest extends TeleUnitTest {
    /**
     * Class that tests robotframework by running methods
     */

    private final RobotPart part = intake;

    /**
     * Test robot framework (not directly but through Terrabot)
     * NOTE: Since allrobotparts is a static arraylist all of the parts terrabot should be here
     */
    @Override
    protected void loop() {
        /**
         * Should be about 9 (or count how many robotparts are defined in terrabot)
         */
        log.show("Robot Parts Size", RobotFramework.allRobotParts.size());
        /**
         * Should be TELE
         */
        log.show("Test part current user", part.getUser());
    }
}

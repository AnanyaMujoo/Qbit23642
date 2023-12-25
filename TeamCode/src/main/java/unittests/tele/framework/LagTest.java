package unittests.tele.framework;

import unittests.tele.TeleUnitTest;

import static global.General.*;


public class LagTest extends TeleUnitTest {
    /**
     * Shows the lag on the robot
     */
    @Override
    protected void loop() {
        /**
         * Shows the delay (i.e. time between updates)
         * This should be a reasonable value ~1-30 ms
         */
        log.show("Delay: ", sync.getDelay());
    }
}

package unittests.tele.framework;

import unittests.tele.TeleUnitTest;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.*;



public class FaultTest extends TeleUnitTest {
    /**
     * Tests if fault is working by using warn (switch to fault.check if you want to test that functionality)
     */
    @Override
    public void loop() {
        /**
         * Should all be displayed properly
         */
        fault.warn("Is fault working?");
        fault.warn("Is fault really working?", Expectation.INCONCEIVABLE, Magnitude.CATASTROPHIC);
        fault.warn("Is fault really really really working?", false, true);
        fault.warn("Is fault really really really really working?", Expectation.INCONCEIVABLE, Magnitude.CATASTROPHIC, false, true);
    }
}

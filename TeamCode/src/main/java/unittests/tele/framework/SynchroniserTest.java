package unittests.tele.framework;
import static global.General.*;

import debugging.Synchroniser;
import teleutil.button.Button;
import teleutil.button.ButtonEventHandler;
import unittests.tele.TeleUnitTest;

public class SynchroniserTest extends TeleUnitTest {
    /**
     * Test sychroniser by creating one and testing delay
     */


    /**
     * Synchroniser object
     */
    Synchroniser synchroniser = new Synchroniser();

    @Override
    protected void start() {
        /**
         * Reset the delay and update once (so that number of updates is not 0 which would make delay infinity)
         */
        synchroniser.resetDelay();
        synchroniser.update();
        /**
         * Link the a button to update
         */
        gph1.link(Button.A, ButtonEventHandler.class, () -> synchroniser.update());
    }

    @Override
    protected void loop() {
        /**
         * Should change while A button is is pressed
         * When A is not pressed it should keep going up
         * Hold down A button and delay should eventually stabilise to something around 1ms - 30ms
         */
        log.show("Synchroniser delay, press A to update", synchroniser.getDelay());
    }

    @Override
    public void stop() {
        /**
         * Log the delay to the logcat and check if all methods have been run properly
         */
        synchroniser.logDelay();
    }
}

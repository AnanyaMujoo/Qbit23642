package unittests.auto.framework;

import unittests.auto.AutoUnitTest;
import util.Timer;
import static global.General.log;

import static global.General.bot;

import android.app.ActionBar;

public class IteratorTest extends AutoUnitTest {
    /**
     * Tests the iterator interface by running different methods
     */


    /**
     * Timer to test while active
     */
    Timer timer = new Timer();

    @Override
    public void init() {
        /**
         * Reset the timer
         */
        timer.reset();
    }

    @Override
    protected void run() {
        /**
         * Display the time for 1 second
         */
        whileTime(() -> log.show("While time for 1 s", timer.seconds()), 1);

        /**
         * Display the time for another second
         */
        whileActive(() -> timer.seconds() < 2, () -> log.show("While active for 1 s", timer.seconds()));

        /**
         * Pause for a second
         */
        log.show("Pausing for one second");
        pause(1);
    }
}
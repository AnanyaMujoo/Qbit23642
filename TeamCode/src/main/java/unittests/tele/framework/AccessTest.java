package unittests.tele.framework;

import unittests.tele.TeleUnitTest;
import util.Access;
import util.Timer;

import static global.General.*;

public class AccessTest extends TeleUnitTest {
    /**
     * Test the access class
     * NOTE: This test should not be run more than once
     */

    /**
     * Access object to be tested
     */
    private final Access access = new Access();
    /**
     * Thread to test different thread using access
     */
    private Thread thread;
    /**
     * Does the new thread have access?
     */
    private volatile boolean threadAccess = true;

    @Override
    public void init() {
        /**
         * Give the main thread access
         */
        access.allow();
        thread = new Thread(() -> {
            access.allow();
            /**
             * After two seconds deny the thread access
             */
            timer.reset();
            while (timer.seconds() < 2){}
            access.deny();
            threadAccess = access.isAllowed();
            /**
             * After 2 more seconds give back access
             */
            timer.reset();
            while (timer.seconds() < 2){}
            access.allow();
            threadAccess = access.isAllowed();
        });
    }

    @Override
    protected void start() {
        /**
         * Start the new thread
         */
        thread.start();
    }

    @Override
    protected void loop() {
        /**
         * The main thread should always have access
         */
        fault.warn("The main thread didn't have access", access.isAllowed(), true);
        log.showAndRecord("Access is allowed here?", access.isAllowed());
        /**
         * Access in the new thread starts at true, wait 2 secs, false, wait 2 secs, true
         */
        log.showAndRecord("Access is allowed in thread? (True, 2 secs,  False, 2 secs, True)", threadAccess);
    }
}

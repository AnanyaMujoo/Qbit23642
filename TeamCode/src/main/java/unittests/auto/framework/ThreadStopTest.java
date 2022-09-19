package unittests.auto.framework;

import unittests.auto.AutoUnitTest;

import static global.General.log;

public class ThreadStopTest extends AutoUnitTest {


    /**
     * To use this test, stop the test while in init
     * Alternatively bypass the test by clicking start
     */
    @Override
    public void init() {
        whileActive(() -> log.show("Stop this test, or move on"));
    }

    /**
     * Wait two seconds, if bypassing test
     */
    @Override
    protected void run() {
        whileActive(() -> timer.seconds() < 2, () -> log.show("Wait two seconds"));
    }
}

package unittests.tele.framework;

import unittests.tele.TeleUnitTest;
import util.TerraThread;

import static global.General.*;

public class ThreadTest extends TeleUnitTest {
    /**
     * Tests two new two threads on the robot by setting their execution code
     */


    /**
     * Test threads
     */
    TerraThread testThread;
    TerraThread testThread2;


    /**
     * Create and start threads in init
     */
    @Override
    public void init() {
        testThread = new TerraThread("TestThread");
        testThread.start();
        testThread2 = new TerraThread("TestThread2");
        testThread2.start();
    }



    @Override
    public void loop() {
        /**
         * One thread will crash, so a fault message should appear
         */
        testThread.setExecutionCode(() -> {
            throw new RuntimeException();
        });
        /**
         * Should display this
         * NOTE: This may clash with telemetry in the main thread so it might appear on an off
         */
        testThread2.setExecutionCode(() -> {
            log.show("Number of started threads {4}", TerraThread.getNumberOfStartedThreads());
            log.show("Number of active threads {3}", TerraThread.getNumberOfActiveThreads());
        });
    }

    /**
     * Stop updating the threads
     */
    @Override
    public void stop() {
        log.clearTelemetry();
    }
}

package unittests.auto.framework.movement;

import unittests.auto.AutoUnitTest;


public class MoveTimeTest extends AutoUnitTest {
    /**
     * Tests moving for some time
     */

    /**
     * Run method uses while time (Robot moves forward at .3 power for 1 s)
     */
    @Override
    protected void run() {
        whileTime(() -> drive.move(0.3, 0, 0), 1);
    }

}

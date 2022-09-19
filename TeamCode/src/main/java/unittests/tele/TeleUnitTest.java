package unittests.tele;

import unittests.UnitTest;
import util.condition.Status;

public class TeleUnitTest extends UnitTest {
    /**
     * Unit test based on tele
     * For init and stop see UnitTest
     * @link UnitTest
     */


    /**
     * Start method runs once
     */
    protected void start() {}

    /**
     * Loop runs over and over
     */
    protected void loop() {}

    /**
     * Run by running start, setting the status to active, and then running loop
     * This ensures that start runs once and then loop runs over and over
     */
    @Override
    public final void test(){
        if(status.equals(Status.IDLE)){
            timer.reset();
            start();
            status = Status.ACTIVE;
        }else{
            loop();
        }
    }
}

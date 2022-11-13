package unittests.auto;

import static global.General.bot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import autoutil.AutoUser;
import global.Common;
import unittests.UnitTest;
import util.template.Iterator;
import util.condition.Status;
import util.template.Precision;

public class AutoUnitTest extends UnitTest implements Iterator, Common, AutoUser {
    /**
     * Unit test based on auto
     * For init and stop see UnitTest
     * @link UnitTest
     */

    // TOD 5 Make this work with autoframework

    /**
     * Static linear opmode
     */
    public static LinearOpMode linearOpMode;

    /**
     * Start method runs once
     */
    protected void start(){}

    /**
     * Run runs once after start
     * NOTE: This is equivalent to loop in TeleUnitTest except it runs once
     */
    protected void run(){}

    /**
     * Test runs the test
     */
    @Override
    public final void test() {
        if(status.equals(Status.IDLE)){
            timer.reset();
            start();
            run();
            status = Status.ACTIVE;
            bot.halt();
        }
    }

    /**
     * Condition to not exit
     * @return not isStopRequested
     */
    @Override
    public boolean condition() {
        return !Precision.runOnCondition(linearOpMode.isStopRequested(), this::end);
    }
}
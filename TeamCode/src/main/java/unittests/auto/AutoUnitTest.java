package unittests.auto;

import static global.General.bot;
import static global.General.fault;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import automodules.StageList;
import autoutil.executors.Executor;
import geometry.circles.AngleType;
import global.Common;
import robotparts.RobotPart;
import unittests.UnitTest;
import util.codeseg.CodeSeg;
import util.template.Iterator;
import util.condition.Status;
import util.template.Precision;

public class AutoUnitTest extends UnitTest implements Iterator, Common {
    /**
     * Unit test based on auto
     * For init and stop see UnitTest
     * @link UnitTest
     */

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
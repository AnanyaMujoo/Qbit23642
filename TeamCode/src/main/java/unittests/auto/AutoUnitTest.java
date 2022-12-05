package unittests.auto;

import static global.General.bot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import autoutil.AutoFramework;
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

    /**
     * Static linear opmode
     */
    public static LinearOpMode linearOpMode;
    private AutoFramework autoFramework;

    protected void setAuto(AutoFramework auto){ autoFramework = auto; }

    /**
     * Start method runs once
     */
    protected void start(){}

    /**
     * Run runs once after start
     * NOTE: This is equivalent to loop in TeleUnitTest except it runs once
     */
    protected void run(){ if(autoFramework != null){ autoFramework.initAuto(); autoFramework.runAuto(); autoFramework.stopAuto(); } }

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
            if(autoFramework != null){
                odometry.reset();
            }
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
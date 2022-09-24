package unittests.auto;

import static global.General.bot;
import static global.General.fault;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import automodules.StageList;
import autoutil.executors.Executor;
import geometry.circles.AngleType;
import global.Common;
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

    @Override
    public boolean condition() {
        return !Precision.runOnCondition(linearOpMode.isStopRequested(), this::end);
    }
}

//public class AutoUnitTest extends UnitTest implements Iterator, Common {
//    /**
//     * Unit test based on auto
//     * For init and stop see UnitTest
//     * @link UnitTest
//     */
//
//    /**
//     * Static linear opmode
//     */
//    public static LinearOpMode linearOpMode;
//
//    protected static Executor executor;
//
//    private static boolean hasTelemetry = true;
//
//    public void defineExecutorAndAddPoints() {}
//
//    @Override
//    public void init() {
//        defineExecutorAndAddPoints();
//        if (executor != null) executor.complete();
//    }
//
//
//    protected void start() {}
//
//
//    public void onStart() {}
//
//    public void duringLoop() {}
//
//    public void addTelemetry() { hasTelemetry = false; }
//
//    public void onEnd() {}
//
//    private void checkHeadingWrong(double h) {
//        fault.check("Using degrees or wrong range of heading ( correct is (-PI, PI] ) in default point?",
//                Math.abs(h) > Math.PI || h == -Math.PI, false);
//    }
//
//    public CodeSeg wayPoint(double x, double y, double h) {
//        checkHeadingWrong(h);
//        return () -> executor.addWaypoint(x, y, h, AngleType.RADIANS);
//    }
//
//    public CodeSeg setPoint(double x, double y, double h) {
//        checkHeadingWrong(h);
//        return () -> executor.addSetpoint(x, y, h, AngleType.RADIANS);
//    }
//
//    public CodeSeg unsyncedRF(StageList rf) {
//        return () -> executor.addUnsynchronizedRF(rf);
//    }
//
//    public CodeSeg syncedRF(StageList rf) {
//        return () -> executor.addSynchronizedRF(rf);
//    }
//
//    public CodeSeg custom(CodeSeg in) {
//        return in;
//    }
//
//    public void addExecutorFuncs(@NonNull CodeSeg... funcs) {
//        for (CodeSeg func : funcs) {
//            func.run();
//        }
//    }
//
//    /**
//     * Run runs once after start
//     * NOTE: This is equivalent to loop in TeleUnitTest except it runs once
//     */
//    // TODO 4 FIX broken?
//    protected void run() {
//        if (executor != null) {
//            onStart();
//
//            executor.resumeMove();
//
//            whileActive(() -> {
//                executor.update();
//                addTelemetry();
//                duringLoop();
//                update(hasTelemetry);
//            });
//
//            onEnd();
//        }
//    }
//
//    /**
//     * Test runs the test
//     */
//    @Override
//    public final void test() {
//        if(status.equals(Status.IDLE)){
//            start();
//            run();
//            status = Status.ACTIVE;
//        }
//    }
//
//    @Override
//    public boolean condition() {
//        return !Precision.runOnCondition(linearOpMode.isStopRequested(), this::end);
//        // TODO 4 FIX Figure out if we need this?
//        // linearOpMode.opModeIsActive() && (executor == null || !executor.finished()
//    }
//}

package unittests.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static global.General.log;

import auton.Auto;
import elements.FieldSide;
import unittests.UnitTester;
import unittests.auto.framework.IteratorTest;
import unittests.auto.framework.ThreadStopTest;
import unittests.auto.framework.movement.MoveTimeTest;
import unittests.auto.framework.movement.PurePursuitTest;
import util.condition.Status;

@SuppressWarnings("ALL")
@Autonomous(name = "AutoUnitTester", group = "UnitTests")
public class AutoUnitTester extends Auto implements UnitTester {
    /**
     * Status of this unit tester
     * Used for stopping once all the tests are done
     */
    Status status = Status.ACTIVE;

    /**
     * Type of testing mode
     * @link TestType
     */
    TestingMode testingMode = TestingMode.CONTROL;

    @Override
    public void createUnitTests(){
        /**
         * Framework
         */

//        Works as of 3.29.16
//        add(new IteratorTest());
//        add(new ThreadStopTest());

        // TODO 4 TEST

        /**
         * Framework (needs hardware, in movement package)
         */
//        Works as of ??
        add(new MoveTimeTest());



        /**
         * Other
         */
        addAll(
//                new ArcTest()
//                new OdometryTest()
//                new IteratorTest(),
//                new PIDTest(),
//                new MoveTest()
//                new InternalCameraTest()
//                new ParameterConstructorTest()
//                new PurePursuitTest()
//                new AutoModuleInitialTest()
//                new OdometryTest()
        );
    }

    /**
     * Initialize by setting the linear op mode, and reading the selector
     */
    @Override
    public void initAuto() {
        AutoUnitTest.linearOpMode = this;
        readyTestsAndSelector(testingMode);
        activate(FieldSide.UNKNOWN);
        log.showTelemetry();
    }

    /**
     * Run the tests
     */
    @Override
    public void runAuto() {
        /**
         * Reset the update timer and set update on show to true so that logs dont need to be updated manualy
         */
        selector.resetUpdateTimer();
        log.setShouldUpdateOnShow(true);
        while (opModeIsActive()){
            if(!isDoneWithAllTests(testingMode)) {
                selector.update();
                runCurrentTest(testingMode);
            }else{
                log.show("Done With All Tests");
            }
        }
    }

    /**
     * Reset the selector
     */
    @Override
    public void stopAuto() {
        selector.reset();
    }
}

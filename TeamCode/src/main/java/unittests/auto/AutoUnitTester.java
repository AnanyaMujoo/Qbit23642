package unittests.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;

import auton.Auto;
import elements.FieldPlacement;
import elements.FieldSide;
import unittests.UnitTester;
import unittests.auto.framework.VuforiaTest;
import unittests.auto.framework.calibration.OdometryCalib;
import unittests.auto.framework.calibration.RestPowerCalib;
import unittests.auto.framework.calibration.VoltageScaleCalib;
import unittests.auto.framework.movement.DistanceSensorStraightenTest;
import unittests.auto.framework.movement.OdometryTest;
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
//    TestingMode testingMode = TestingMode.SELECTION;

    @Override
    public void createUnitTests(){
        /**
         * Framework
         */

//        Works as of 4.2.5
//        add(new IteratorTest());
//        add(new ThreadStopTest());


//        add(new VoltageScaleCalib());
//        add(new VuforiaTest());
//        add(new OdometryCalib());
//        add(new RestPowerCalib());

        /**
         * Framework (needs hardware, in movement package)
         */
//        TOD 5 Test
//        Works as of 4.3.11
//        add(new MoveTimeTest());
//        add(new PIDTest());
//        add(new PARTest());

//        add(new PurePursuitTest());
//        add(new DRPTest());
//
//
//        add(new OdometryTest.ForwardTest());
//        add(new OdometryTest.StrafeTest());
//        add(new OdometryTest.TurnTest());

        add(new DistanceSensorStraightenTest());

    }

    /**
     * Initialize by setting the linear op mode, and reading the selector
     */
    @Override
    public void initAuto() {
        fieldSide = FieldSide.BLUE;
        fieldPlacement = FieldPlacement.LOWER;
        AutoUnitTest.linearOpMode = this;
        readyTestsAndSelector(testingMode);
        activate();
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

package unittests.tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import elements.FieldSide;
import teleop.Tele;

import unittests.UnitTester;
import unittests.tele.framework.AccessTest;
import unittests.tele.framework.AutoModuleTest;
import unittests.tele.framework.CommonTest;
import unittests.tele.framework.FaultTest;
import unittests.tele.framework.GamepadTest;
import unittests.tele.framework.LagTest;
import unittests.tele.framework.LoggerTest;
import unittests.tele.framework.RobotFrameworkTest;
import unittests.tele.framework.RobotFunctionsTest;
import unittests.tele.framework.SelectorTest;
import unittests.tele.framework.StageTest;
import unittests.tele.framework.StorageTest;
import unittests.tele.framework.SynchroniserTest;
import unittests.tele.framework.ThreadTest;
import unittests.tele.hardware.Mecanum.MecanumCarouselTest;
import unittests.tele.hardware.Mecanum.MecanumDriveTest;
import unittests.tele.hardware.Mecanum.MecanumIntakeTest;
import unittests.tele.hardware.Mecanum.MecanumLiftAndOuttakeTest;
import unittests.tele.hardware.Mecanum.MecanumLiftTest;
import unittests.tele.hardware.Mecanum.MecanumOuttakeTest;
import unittests.tele.sensor.ColorTest;
import unittests.tele.sensor.GyroTest;

import static global.General.*;

@SuppressWarnings("ALL")
@TeleOp(name = "TeleUnitTester", group = "UnitTests")
public class TeleUnitTester extends Tele implements UnitTester{

    /**
     * Type of testing mode
     * @link TestType
     */
    private TestingMode testingMode = TestingMode.CONTROL;

    @Override
    public void createUnitTests(){
        /**
         * Framework
         */

        add(new AccessTest());
        add(new AutoModuleTest());
        add(new CommonTest());
        add(new FaultTest());
        add(new GamepadTest());
        add(new LagTest());
        add(new LoggerTest());
        add(new RobotFrameworkTest());
        add(new RobotFunctionsTest());
        add(new SelectorTest());
        add(new StageTest());
        add(new StorageTest());
        add(new SynchroniserTest());
        add(new ThreadTest());


        /**
         * Hardware
         */
        addAll(
//            new TankDriveTest(),
//            new IntakeTest(),
//            new LiftTest(),
//            new TurretTest(),
//            new OuttakeTest(),
//            new CarouselTest()
        );
        /**
         * Sensors
         */
        addAll(
//            new ColorTest()
//            new DistanceTest(),
//            new GyroTest(),
//            new OdometryTest(),
//            new TouchTest()
        );
    }

    /**
     * Init method
     */
    @Override
    public void initTele() {
        readyTestsAndSelector(testingMode);
        activate(FieldSide.UNKNOWN);
    }

    /**
     * Start method, reset the selectors update timer
     */
    @Override
    public void startTele() {
        selector.resetUpdateTimer();
    }

    /**
     * Loop method, update the selector, and run the current test
     */
    @Override
    public void loopTele() {
        if(!isDoneWithAllTests(testingMode)) {
            selector.update();
            runCurrentTest(testingMode);
        }else{
            log.show("Done With All Tests");
        }
    }

    /**
     * Reset the selector
     */
    @Override
    public void stopTele() {
        selector.reset();
    }
}

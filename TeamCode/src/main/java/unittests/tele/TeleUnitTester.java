package unittests.tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import elements.FieldSide;
import teleop.Tele;

import unittests.UnitTester;
import unittests.tele.framework.LagTest;
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
//        add(new GyroTest());
//        add(new ThreadTest());
//        add(new MecanumCarouselTest());

        addAll(
//              new GyroTest(),
//            new ThreadTest(),
//            new MecanumCarouselTest(),
//            new MecanumDriveTest(),
//            new MecanumIntakeTest(),
//            new MecanumLiftAndOuttakeTest(),
//            new MecanumLiftTest()
//            new MecanumOuttakeTest()
//            new OdometryServoTest(),
//            new AccessTest(),
//            new CommonTest(),
//            new CoordinatePlaneTest(),
//            new LoggerTest(),
//            new FaultTest(),
//            new GamepadTest(),
//            new RobotFunctionsTest(),
//            new ThreadTest(),
//            new StorageTest(),
//            new AutoModuleTest(),
//            new LagTest()
//            new SelectorTest(),
//            new SynchroniserTest(),
//            new RobotPartTest(),
//            new RobotFrameworkTest(),
//            new ElectronicsTest()
        );
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

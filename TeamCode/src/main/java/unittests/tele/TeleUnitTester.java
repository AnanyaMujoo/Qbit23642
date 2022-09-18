package unittests.tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import elements.FieldSide;
import teleop.Tele;

import unittests.UnitTester;

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

//        Works as of 3.29.5
//        add(new AccessTest());
//        add(new AutoModuleTest());
//        add(new CommonTest());
//        add(new FaultTest());
//        add(new GamepadTest());
//        add(new LagTest());
//        add(new LoggerTest());
//        add(new SelectorTest());
//        add(new StageTest());
//        add(new StorageTest());
//        add(new SynchroniserTest());
//        add(new ThreadTest());


        /**
         * Framework (needs hardware)
         */
//        add(new RobotFrameworkTest());
//        add(new RobotFunctionsTest());
//        add(new RobotPartTest());


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

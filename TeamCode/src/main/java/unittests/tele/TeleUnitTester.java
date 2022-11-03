package unittests.tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import elements.FieldSide;
import teleop.Tele;

import unittests.UnitTester;
import unittests.tele.framework.movement.TwoOdometryTest;

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

//        Works as of 4.2.5
//        add(new AccessTest());
//        add(new AutoModuleInitialTest());
//        add(new CommonTest());
//        add(new DecisionTest());
//        add(new FaultTest());
//        add(new GamepadTest());
//        add(new GeometryTest());
//        add(new LagTest());
//        add(new LoggerTest());
//        add(new MathTest());
//        add(new ParameterConstructorTest());
//        add(new PhysicsTest());
//        add(new SelectorTest());
//        add(new StorageTest());
//        add(new SynchroniserTest());
//        add(new ThreadTest());
//        add(new BackgroundTest());


        /**
         * Framework (needs hardware, in movement package)
         */

//        Works as of 4.2.5
//        add(new AutoModuleTest());
        add(new TwoOdometryTest());
//        add(new RobotFrameworkTest());
//        add(new RobotFunctionsTest());
//        add(new StageTest());
//        add(new StallDetectorTest());
//        add(new CustomPMotorPIDTest());
//        add(new PositionHolderTest());
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

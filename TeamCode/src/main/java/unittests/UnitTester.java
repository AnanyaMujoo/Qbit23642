package unittests;

import teleutil.Selector;

import teleutil.button.Button;
import util.User;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.store.Item;

import static global.General.*;

public interface UnitTester {

    /**
     * Selector object to select the unit test for selection test type
     */
    Selector<UnitTest> selector = new Selector<>(true);

    /**
     * Creates all of the unit tests, comment in the ones you want
     * Use add(new <your unit test>) to add your own unit test
     */
    void createUnitTests();

    default void readyTestsAndSelector(TestingMode testingMode){
        /**
         * initialize the selector depending on the testing mode
         */
        switch (testingMode){
            case TIME:
                /**
                 * Change the time between unit tests here
                 */
                selector.init(5);
                break;
            case CONTROL:
                /**
                 * Change the button to move to the next test here
                 */
                selector.init(() -> gamepad1.x, () -> false);
                break;
            case SELECTION:
                /**
                 * In selection mode, use the dpad to move up and down through the list of unit tests
                 */
                selector.init(gph1, Button.DPAD_DOWN, Button.DPAD_UP, Button.X);
                break;
        }


        /**
         * Run on the next test
         * stop the current test, halt the robot, idle the selector, unlink gamepadhandlers, and clear telemetry
         */
        selector.runOnNext(() -> {
            getCurrentTest().stop();
            getCurrentTest().reset();
            bot.halt();
            selector.idle();
            gph1.unlinkAll();
            gph2.unlinkAll();
            log.clearTelemetry();
        });

        /**
         * Create all the unit tests and initialize them
         */
        createUnitTests();

        /**
         * Check if there are no unit tests to be run
         */
        fault.warn("No unit tests to be run", Expectation.OBVIOUS, Magnitude.NEGLIGIBLE, selector.getNumberOfItems() == 0, false);
        selector.runToAll(UnitTest::init);

        /**
         * Display the testing mode
         */
        log.show("Testing Mode: " + testingMode.toString());
    }

    /**
     * Adds a new test to the selector
     * NOTE: This also checks if the same test has been entered more than once
     * @param test
     */
    default void add(UnitTest test){
        checkForDuplicates(test);
        selector.addItem(new Item<>(test.getClass().getSimpleName(), test));
    }

    /**
     * Checks for duplicate tests in the selector
     * @param test
     */
    default void checkForDuplicates(UnitTest test){
        for(String testName: selector.getItemClassNames()){
            if(test.getClass().getSimpleName().equals(testName)){
                fault.warn("Duplicate unit test found", Expectation.OBVIOUS, Magnitude.NEGLIGIBLE);
            }
        }
    }

    /**
     * Add all the unit tests
     * @param tests
     */
    default void addAll(UnitTest... tests){
        for(UnitTest test : tests){
            add(test);
        }
    }

    /**
     * Get the current test name
     * @return test name
     */
    default String getCurrentTestName(){
        return getCurrentTest().getClass().getSimpleName();
    }

    /**
     * Get the current test
     * @return current test
     */
    default UnitTest getCurrentTest(){
        return selector.getCurrentItem().getValue();
    }

    /**
     * Run the current test
     * NOTE: Selector mode will only run the test if its active
     * and when you are choosing it is idle
     */
    default void runCurrentTest(TestingMode testingMode){
        if(testingMode.equals(TestingMode.SELECTION) && selector.isInActive()){
            /**
             * In selector mode list all the different options and wait for the selector to activate
             */
            log.list(selector.getItemClassNames(), selector.getCurrentIndex());
        }else if(selector.isActive()){
            /**
             * Add telemetry to say testing the current test if not in autonomous mode
             */
            if(!mainUser.equals(User.AUTO)) {
                log.showAndRecord("Testing", getCurrentTestName());
            }
            /**
             * If the selector is active then run the current test
             */
            selector.runToCurrentItem(UnitTest::test);
            /**
             * If in autonomous mode tell when the test is done
             */
            if(mainUser.equals(User.AUTO)){
                log.showAndRecord("Done testing", getCurrentTestName());
            }
        }
    }

    /**
     * Is the tester done with all of the test?
     * If the testing mode is not selection (only ends when program stops) and the selector is done with last
     * @param testingMode
     * @return done
     */
    default boolean isDoneWithAllTests(TestingMode testingMode){
        return !testingMode.equals(TestingMode.SELECTION) && selector.isDoneWithLast();
    }

    /**
     * Type of testing mode
     */
    enum TestingMode {
        /**
         * Move to the next test by clicking x so you can control when the next test occurs
         */
        CONTROL,

        /**
         * The next test will automatically run after a certain interval (set in init)
         */
        TIME,

        /**
         * A screen will pop up to show you all of the available tests and you can select the ones you want from there
         */
        SELECTION
    }
}

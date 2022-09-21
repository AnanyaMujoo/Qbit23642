package unittests.tele.framework;

import automodules.StageList;
import automodules.stage.Initial;
import automodules.stage.Stage;
import robotparts.RobotPart;
import unittests.tele.TeleUnitTest;
import util.Timer;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.bot;
import static global.General.fault;
import static global.General.log;

public class AutoModuleInitialTest extends TeleUnitTest {
    /**
     * Tests automodule intials
     */

    /**
     * Last initial number
     */
    int lastInitial = 0;

    /**
     * Test automodule
     */
    StageList testAutoModule = new StageList(
            new Stage(
                    new Initial(() -> {if(lastInitial == 0){lastInitial++;}}),
                    new Initial(() -> {if(lastInitial == 1){lastInitial++;}}),
                    new Initial(() -> {if(lastInitial == 2){lastInitial++;}}),
                    new Initial(() -> {if(lastInitial == 3){lastInitial++;}}),
                    new Initial(() -> {if(lastInitial == 4){lastInitial++;}})
            )
    );

    /**
     * Number of trials to execute (2 secs between trials)
     */
    private final int numTrials = 5;

    /**
     * Current trial number
     */
    private int trialNum = 0;

    /**
     * Start with one autoModule
     */
    @Override
    protected void start() {
        bot.addAutoModule(testAutoModule);
    }

    /**
     * Loop through all the initials for some trials, checking order
     */
    @Override
    protected void loop() {
        log.showAndRecord("Trial #" + trialNum + " of " + numTrials + " Last Initial Number", lastInitial);
        if(trialNum < numTrials) {
            if (timer.seconds() > 2) {
                timer.reset();
                lastInitial = 0;
                bot.addAutoModule(testAutoModule);
                trialNum++;
            } else if (timer.seconds() > 1) {
                fault.check("Initials failed to be in order", Expectation.SURPRISING, Magnitude.CRITICAL, lastInitial == 5, true);
            }
        }
    }
}

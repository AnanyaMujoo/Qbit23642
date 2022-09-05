package unittests.auto.framework;

import android.annotation.SuppressLint;

import automodules.StageList;
import automodules.stage.Initial;
import automodules.stage.Stage;
import unittests.auto.AutoUnitTest;

import static global.General.bot;
import static global.General.log;

public class AutoModuleInitialTest extends AutoUnitTest {

   int lastInitial = 0;

    StageList testAutoModule = new StageList(
            new Stage(
                    new Initial(() -> lastInitial = 1),
                    new Initial(() -> lastInitial = 2),
                    new Initial(() -> lastInitial = 3),
                    new Initial(() -> lastInitial = 4),
                    new Initial(() -> lastInitial = 5)
            )
    );

    @Override
    protected void run() {
        lastInitial = 0;
        int numTrials = 5;
        for (int i = 0; i < numTrials; i++) {
            bot.addAutoModule(testAutoModule);
            pause(2);
            log.show("Trial #" + (i+1) + " of " + numTrials + " Last Initial Number {5}", lastInitial);
        }
        pause(2);
    }
}

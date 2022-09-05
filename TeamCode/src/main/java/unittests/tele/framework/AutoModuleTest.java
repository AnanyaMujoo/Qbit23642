package unittests.tele.framework;
import teleutil.button.Button;
import teleutil.button.OnPressEventHandler;
import unittests.tele.TeleUnitTest;

import static global.General.*;

public class AutoModuleTest extends TeleUnitTest {
    /**
     * Test automodules using intake as an example
     */

    @Override
    protected void start() {
        /**
         * Link gamepad handlers
         */
        gph1.link(Button.A, OnPressEventHandler.class,() -> bot.addAutoModule(tankAutoModules.IntakeAuto));
        gph1.link(Button.B, OnPressEventHandler.class, bot::cancelAutoModules);
        gph1.link(Button.RIGHT_BUMPER, OnPressEventHandler.class, bot::pauseAutoModules);
        gph1.link(Button.LEFT_BUMPER, OnPressEventHandler.class, bot::resumeAutoModules);
    }

    @Override
    protected void loop() {
        /**
         * Should run automodules
         */
        log.show("Click a to start intake");
        log.show("Click b to cancel the AutoModules");
        log.show("Click right bumper to pause the AutoModules");
        log.show("Click left bumper to resume the AutoModules");
    }
}

package unittests.unused.tank;

import static global.General.*;

import teleutil.button.Button;
import teleutil.button.OnTurnOffEventHandler;
import teleutil.button.OnTurnOnEventHandler;
import unittests.tele.TeleUnitTest;

public class TankIntakeTest extends TeleUnitTest {
    /**
     * Tests intake
     */
    @Override
    public void start() {
        /**
         * Link the gamepad handlers
         */
        gph1.link(Button.RIGHT_BUMPER, OnTurnOnEventHandler.class, () -> bot.tankIntake.move(1));
        gph1.link(Button.RIGHT_BUMPER, OnTurnOffEventHandler.class, () -> bot.tankIntake.move(0));
    }

    @Override
    protected void loop() {
        showConfig(bot.tankIntake);
        /**
         * Intake should move when right bumper is pressed (toggle control)
         */
        log.show("Use right bumper");
    }
}

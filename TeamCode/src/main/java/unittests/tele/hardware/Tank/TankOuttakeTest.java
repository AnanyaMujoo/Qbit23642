package unittests.tele.hardware.Tank;

import teleutil.button.Button;
import teleutil.button.OnPressEventHandler;
import unittests.tele.TeleUnitTest;
import static global.General.*;

public class TankOuttakeTest extends TeleUnitTest {
    /**
     * Tests the outtake
     */
    @Override
    protected void start() {
        /**
         * Link the gamepad handlers
         */
        gph1.link(Button.B, OnPressEventHandler.class, () -> bot.tankOuttake.lockCube());
        gph1.link(Button.DPAD_UP, OnPressEventHandler.class, () -> bot.tankOuttake.lockBall());
        gph1.link(Button.DPAD_RIGHT, OnPressEventHandler.class, () -> bot.tankOuttake.align());
        gph1.link(Button.DPAD_DOWN, OnPressEventHandler.class, () -> bot.tankOuttake.start());
        gph1.link(Button.DPAD_LEFT, OnPressEventHandler.class, () -> bot.tankOuttake.open());
    }

    @Override
    protected void loop() {
        showConfig(bot.tankOuttake);
        /**
         * Should move the outtake servo
         */
        log.show("B for lock cube");
        log.show("Dpad up for lock ball");
        log.show("Dpad right for align");
        log.show("Dpad down for start");
        log.show("Dpad left for open");
    }
}

package unittests.tele.sensor;

import robotparts.electronics.output.OLed;
import teleutil.button.Button;
import teleutil.button.OnPressEventHandler;
import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gph1;
import static global.General.log;

public class LedTest extends TeleUnitTest {
    /**
     * Tests LEDs
     */
    @Override
    protected void start() {
        /**
         * Link gamepad handlers
         */
        gph1.link(Button.DPAD_UP, OnPressEventHandler.class, () -> bot.leds.setColorOfLEDs(OLed.LEDColor.OFF));
        gph1.link(Button.DPAD_RIGHT, OnPressEventHandler.class, () -> bot.leds.setColorOfLEDs(OLed.LEDColor.GREEN));
        gph1.link(Button.DPAD_DOWN, OnPressEventHandler.class, () -> bot.leds.setColorOfLEDs(OLed.LEDColor.RED));
        gph1.link(Button.DPAD_LEFT, OnPressEventHandler.class, () -> bot.leds.setColorOfLEDs(OLed.LEDColor.ORANGE));
    }

    @Override
    protected void loop() {
        /**
         * Sets the color of the leds
         * NOTE: All the leds should turn the specified color
         */
        log.show("Press dpad up for off");
        log.show("Press dpad right for green");
        log.show("Press dpad down for red");
        log.show("Press dpad left for orange");
    }
}

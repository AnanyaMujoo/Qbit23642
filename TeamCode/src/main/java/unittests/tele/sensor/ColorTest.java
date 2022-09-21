package unittests.tele.sensor;


import java.util.Arrays;

import unittests.tele.TeleUnitTest;
import static global.General.*;

public class ColorTest extends TeleUnitTest {
    /**
     * Tests color sensors
     */

    // TOD4 NEW ARNAV
    // Make sensor calibration code
    // For all the values of a distance sensor should be recorded, the actual values passed in, and a line of best fit created

    @Override
    protected void loop() {
        /**
         * In HSV, should seem reasonable,
         * h is the hue or color
         * s is the saturation
         * v is the value or brightness
         */
        log.show("Color sensor reading", Arrays.toString(bot.color.getOuttakeColorHSV()));
        /**
         * These should turn true depending on the type of freight
         */
        log.show("Is a ball in the intake?", bot.color.isBall());
        log.show("Is a cube in the intake?", bot.color.isCube());
        log.show("Is a freight in the intake?", bot.color.isFreight());
    }
}

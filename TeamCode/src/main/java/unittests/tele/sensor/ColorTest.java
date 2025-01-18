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
        log.show("Color sensor color", Arrays.toString(colorSensors.getOuttakeColorHSV()));
        log.show("Color sensor distance", colorSensors.getDistance());
        log.show("SampleColor", colorSensors.getSampleColor().toString());
        log.show("IsSampleLoaded", colorSensors.isSampleLoaded());
    }
}

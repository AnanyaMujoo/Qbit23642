package robotparts.unused;


import robotparts.RobotPart;
import robotparts.electronics.output.OLed;
import robotparts.electronics.output.OLed.*;

// TODO IMPLEMENT

public class Leds extends RobotPart {
    /**
     * Leds
     */
    private OLed ledfr;
    private OLed ledfl;
    private OLed ledbr;
    private OLed ledbl;

    @Override
    public void init() {
        ledfr = createLED("ledfr");
        ledbr = createLED("ledbr");
        ledfl = createLED("ledfl");
        ledbl = createLED("ledbl");
    }

    /**
     * Sets the color of all the leds at once
     * @param color
     */
    public void setColorOfLEDs(LEDColor color){
        ledfr.setColor(color);
        ledbr.setColor(color);
        ledfl.setColor(color);
        ledbl.setColor(color);
    }
}

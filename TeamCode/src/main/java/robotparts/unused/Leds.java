package robotparts.unused;


import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.output.OLed;
import robotparts.electronics.output.OLed.*;

// TODO 4 NEW Implement LEDS

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
        ledfr = create("ledfr", ElectronicType.OLED);
        ledbr = create("ledbr", ElectronicType.OLED);
        ledfl = create("ledfl", ElectronicType.OLED);
        ledbl = create("ledbl", ElectronicType.OLED);
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

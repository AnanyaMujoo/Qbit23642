package robotparts.electronics.output;

import com.qualcomm.robotcore.hardware.DigitalChannel;

import robotparts.Electronic;

/**
 * NOTE: Uncommented
 */

public class OLed extends Electronic {
    /**
     * Should be configured as the name of the LED plus r or g for the red and green channels respectively
     */
    private final DigitalChannel red;
    private final DigitalChannel green;
    public OLed(DigitalChannel r, DigitalChannel g){
        red = r;
        green = g;
        red.setMode(DigitalChannel.Mode.OUTPUT);
        green.setMode(DigitalChannel.Mode.OUTPUT);
    }
    public void setColor(LEDColor color){
        switch (color) {
            case ORANGE: red.setState(false); green.setState(false); break;
            case RED: red.setState(true); green.setState(false); break;
            case GREEN: red.setState(false); green.setState(true); break;
            case OFF: red.setState(true); green.setState(true); break;
        }
    }
    public enum LEDColor{
        OFF,
        RED,
        GREEN,
        ORANGE
    }
}

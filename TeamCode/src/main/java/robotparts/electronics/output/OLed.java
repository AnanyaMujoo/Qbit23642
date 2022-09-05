package robotparts.electronics.output;

import com.qualcomm.robotcore.hardware.DigitalChannel;

import robotparts.Electronic;

/**
 * NOTE: Uncommented
 */

public class OLed extends Electronic {
    private DigitalChannel red;
    private DigitalChannel green;
    public OLed(DigitalChannel r, DigitalChannel g){
        red = r;
        green = g;
        red.setMode(DigitalChannel.Mode.OUTPUT);
        green.setMode(DigitalChannel.Mode.OUTPUT);
    }
    public void setColor(LEDColor color){
        if(access.isAllowed()) {
            switch (color) {
                case OFF:
                    red.setState(false);
                    green.setState(false);
                    break;
                case RED:
                    red.setState(true);
                    green.setState(false);
                    break;
                case GREEN:
                    red.setState(false);
                    green.setState(true);
                    break;
                case ORANGE:
                    red.setState(true);
                    green.setState(true);
                    break;
            }
        }
    }
    public enum LEDColor{
        OFF,
        RED,
        GREEN,
        ORANGE
    }
}

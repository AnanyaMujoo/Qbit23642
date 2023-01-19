package robotparts.unused;


import automodules.AutoModule;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.output.OLed;
import robotparts.electronics.output.OLed.*;
import util.Timer;

// TOD 5 NEW Implement LEDS

public class Leds extends RobotPart {
    /**
     * Leds
     */
//    private OLed ledBack;
    private OLed ledFront;

    private boolean active = false;
    private final Timer timer = new Timer();
//    private OLed ledbr;
//    private OLed ledbl;

    @Override
    public void init() {
        ledFront = create("ledf", ElectronicType.OLED);
//        ledBack = create("ledb", ElectronicType.OLED);
//        ledfl = create("ledfl", ElectronicType.OLED);
//        ledbl = create("ledbl", ElectronicType.OLED);
        setColor(LEDColor.OFF);
        timer.reset();
    }

    /**
     * Sets the color of all the leds at once
     * @param color
     */
    public void setColor(LEDColor color){
//        ledBack.setColor(color);
        ledFront.setColor(color);
    }

    public Stage stageColor(LEDColor color){
        return customTime(() -> setColor(color), 0.0);
    }

    public AutoModule autoModuleColor(LEDColor color){ return new AutoModule(stageColor(color)); }


    public void pulse(LEDColor on, LEDColor off, double rate){
        if(timer.seconds() > (0.5/rate)){
            timer.reset();
            active = !active;
            setColor(active ? on : off);
        }
    }
    public void pulse(LEDColor on, double rate){
        pulse(on, LEDColor.OFF, rate);
    }
}

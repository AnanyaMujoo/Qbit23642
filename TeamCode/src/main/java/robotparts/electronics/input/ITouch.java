package robotparts.electronics.input;

import com.qualcomm.robotcore.hardware.TouchSensor;

import robotparts.Electronic;

/**
 * NOTE: Uncommented
 */

public class ITouch extends Electronic {
    private final TouchSensor touchSensor;
    public ITouch(TouchSensor touchSensor){
        this.touchSensor = touchSensor;
    }
    public boolean isPressed(){
        return touchSensor.isPressed();
    }
}

package robotparts.unused;

import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.ITouch;

public class TouchSensors extends RobotPart {
    /**
     * Touch sensor for outtake
     */
    private ITouch tso;

    @Override
    public void init() {
        tso = create("tso", ElectronicType.ITOUCH);
    }

    /**
     * Is the outtake pressing the touch sensor (i.e. all the way down)
     * @return pressed
     */
    public boolean isOuttakePressingTouchSensor(){
        return tso.isPressed();
    }
}

package global;

import robotparts.hardware.Drive;
import robotparts.hardware.Lift;
import util.template.Mode;


public interface Modes {
    /**
     * Interface to hold modes (different types of movement/control)
     */


    /**
     * List of mode types
     */

    enum GamepadMode implements Mode.ModeType { NORMAL, AUTOMATED }

    enum OuttakeStatus implements Mode.ModeType { DRIVING, PLACING}
    Mode outtakeStatus = new Mode(OuttakeStatus.class);

    enum Height implements Mode.ModeType {HIGH, MIDDLE, LOW, GROUND}
    Mode heightMode = new Mode(Height.class)
            .set(Height.HIGH, Lift.maxPosition-9)
            .set(Height.MIDDLE, 30)
            .set(Height.LOW, 9)
            .set(Height.GROUND, 9);

    // TOD5 MULTI DIMENSIONAL MODE
    enum Drive implements Mode.ModeType {FAST, MEDIUM, SLOW}
    Mode driveMode = new Mode(Drive.class);

}

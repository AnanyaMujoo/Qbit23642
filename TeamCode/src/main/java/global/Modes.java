package global;

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

    enum GameplayMode implements Mode.ModeType { CYCLE,  CIRCUIT_PICK,  CIRCUIT_PLACE }
    Mode gameplayMode = new Mode(GameplayMode.class);

    enum AttackMode implements Mode.ModeType { NORMAL, STICKY }
    Mode attackMode = new Mode(AttackMode.class);

    enum Height implements Mode.ModeType {HIGH, MIDDLE, LOW, GROUND}
    Mode heightMode = new Mode(Height.class)
            .set(Height.HIGH, Lift.maxPosition-9)
            .set(Height.MIDDLE, 28)
            .set(Height.LOW, 9)
            .set(Height.GROUND, 9);
    enum Drive implements Mode.ModeType{ FAST, MEDIUM, SLOW }
    // TOD5 MULTI DIMENSIONAL MODE
    Mode driveMode = new Mode(Drive.class)
            .set(Drive.FAST, 1.0)
            .set(Drive.MEDIUM, 0.75)
            .set(Drive.SLOW, 0.35);

}

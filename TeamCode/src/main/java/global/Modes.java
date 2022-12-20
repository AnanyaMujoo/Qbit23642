package global;

import robotparts.hardware.Lift;
import util.template.Mode;


public class Modes {
    /**
     * Class to hold modes (different types of movement/control)
     */


    /**
     * List of mode types
     */

    public enum GamepadMode implements Mode.ModeType { NORMAL, AUTOMATED }

    public enum GameplayMode implements Mode.ModeType { CYCLE,  CIRCUIT_PICK,  CIRCUIT_PLACE }
    public static final Mode gameplayMode = new Mode(GameplayMode.class);

    public enum Height implements Mode.ModeType {HIGH, MIDDLE, LOW, GROUND}
    public static final Mode heightMode = new Mode(Height.class)
            .set(Height.HIGH, Lift.maxPosition-9)
            .set(Height.MIDDLE, 28)
            .set(Height.LOW, 9)
            .set(Height.GROUND, 9);

    public enum Drive implements Mode.ModeType{ FAST, MEDIUM, SLOW }
    public static final Mode driveMode = new Mode(Drive.class)
            .set(Drive.FAST, 1.0)
            .set(Drive.MEDIUM, 0.75)
            .set(Drive.SLOW, 0.35);

}

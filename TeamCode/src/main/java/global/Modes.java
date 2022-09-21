package global;

import util.condition.Decision;

public class Modes {
    /**
     * Class to hold modes (different types of movement/control)
     */


    /**
     * List of mode types
     */

    public enum OuttakeMode implements Decision {
        SHARED,
        ALLIANCE
    }

    public enum SharedMode implements Decision {
        NORMAL,
        CENTER
    }

    public enum DriveMode implements Decision {
        FAST,
        MEDIUM,
        SLOW
    }

    public enum IndependentMode implements Decision {
        MANUAL,
        USING
    }
}

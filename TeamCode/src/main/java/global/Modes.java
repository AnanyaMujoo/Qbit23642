package global;

import automodules.AutoModule;
import automodules.stage.Main;
import automodules.stage.Stage;
import util.condition.Decision;

import static automodules.StageBuilder.exitAlways;


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

    public enum IndependentMode implements Decision {
        MANUAL,
        USING
    }

    public enum DriveMode implements Decision {
        FAST,
        MEDIUM,
        SLOW
    }
    private static Modes.DriveMode driveMode = Modes.DriveMode.FAST;
    public static void nextDrive() { switch (driveMode) { case FAST:  driveMode = DriveMode.SLOW; break;  case MEDIUM:  driveMode = DriveMode.FAST; break;  case SLOW:  driveMode = DriveMode.MEDIUM; break; } }
    public static boolean driveModeIs(DriveMode driveMode1){ return driveMode.equals(driveMode1); }
    public static DriveMode getDriveMode(){ return driveMode; }
    public static AutoModule CycleDrive() {return new AutoModule(new Stage(new Main(Modes::nextDrive), exitAlways())); }
    public static AutoModule ChangeDrive(DriveMode mode){return new AutoModule(new Stage(new Main(() -> driveMode = mode), exitAlways())); }
}

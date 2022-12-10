package global;

import automodules.AutoModule;
import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.hardware.Drive;
import robotparts.hardware.Lift;
import util.codeseg.CodeSeg;
import util.condition.Decision;
import util.template.ValueMode;

import static automodules.StageBuilder.exitAlways;
import static global.Modes.DriveMode.Drive.FAST;
import static global.Modes.DriveMode.Drive.MEDIUM;
import static global.Modes.DriveMode.Drive.SLOW;
import static global.Modes.HeightMode.Height.LOW;
import static global.Modes.HeightMode.Height.MIDDLE;


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


    public static class HeightMode extends ValueMode{ public enum Height implements ModeType{
        HIGH{@Override public double getValue() {return Lift.maxPosition-1;}},
        MIDDLE {@Override public double getValue() {return 40;}},
        LOW {@Override public double getValue() {return 19;}};
    }}

    public static class DriveMode extends ValueMode{ public enum Drive implements ModeType{
        FAST{@Override public double getValue(){return 1.0;}},
        MEDIUM{@Override public double getValue(){return 0.6;}},
        SLOW{@Override public double getValue(){return 0.3;}};
    }}

    public static final HeightMode heightMode = new HeightMode();
    public static final DriveMode driveMode = new DriveMode().enableCycling(SLOW, MEDIUM, FAST);

}

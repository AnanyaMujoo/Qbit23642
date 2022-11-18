package global;

import automodules.AutoModule;
import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.hardware.Lift;
import util.codeseg.CodeSeg;
import util.condition.Decision;
import util.template.ValueMode;

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

    // TOD 5 Make this better


    public interface Valuable extends Decision { double getValue(); }


//    public static class HeightMode1 extends ValueMode{
//
//        public void cycle(){
//            Height.values()
//        }
//        public enum Height implements ModeType{
//            HIGH(Lift.maxPosition),
//            MEDIUM(40),
//            LOW(20);
//            private final double value;
//            Height(double value){ this.value = value; }
//            public double getValue(){ return value; }
//        }
//    };
//
//    public HeightMode1 heightMode1 = new HeightMode1();


    public enum HeightMode implements Valuable {
        HIGH(Lift.maxPosition),
        MEDIUM(40),
        LOW(20);
        private final double value;
        HeightMode(double value){ this.value = value; }
        public double getValue(){ return value; }
    }

    private static HeightMode heightMode = HeightMode.HIGH;
    public static void cycleHeight() { switch (heightMode) { case HIGH:  heightMode = HeightMode.LOW; break;  case MEDIUM: heightMode = HeightMode.HIGH; break;  case LOW:  heightMode = HeightMode.MEDIUM; break; } }
    public static boolean heightModeIs(HeightMode heightMode1){ return heightMode.equals(heightMode1); }
    public static HeightMode getHeightMode(){ return heightMode; }
    public static Stage ChangeHeight(HeightMode mode){return new Stage(new Main(() -> heightMode = mode), exitAlways()); }

    public enum DriveMode implements Decision {
        FAST(1.0),
        MEDIUM(0.6),
        SLOW(0.3);
        private final double scale;
        DriveMode(double scale){ this.scale = scale; }
        public double getScale(){ return scale; }
    }
    private static DriveMode driveMode = DriveMode.MEDIUM;
    public static void cycleDrive() { switch (driveMode) { case FAST:  driveMode = DriveMode.SLOW; break;  case MEDIUM:  driveMode = DriveMode.FAST; break;  case SLOW:  driveMode = DriveMode.MEDIUM; break; } }
    public static boolean driveModeIs(DriveMode driveMode1){ return driveMode.equals(driveMode1); }
    public static DriveMode getDriveMode(){ return driveMode; }
    public static Stage ChangeDrive(DriveMode mode){return new Stage(new Main(() -> driveMode = mode), exitAlways()); }
}

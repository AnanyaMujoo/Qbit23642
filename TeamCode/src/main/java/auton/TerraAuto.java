package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import elements.Case;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import geometry.position.Pose;
import global.Modes;
import util.template.Mode;

import static global.General.fieldPlacement;
import static global.General.fieldSide;



@Autonomous(name = "TerraAutoMega", group = "auto")
public class TerraAuto extends AutoFramework {

    protected enum AutoMode implements Mode.ModeType {MEGA, NORMAL, SIMPLE}
    protected AutoMode autoMode;

    private double x, s, y;
    private final double pauseBetweenCycles = 0.15;
    private final double timeToPlace = 0.5;

    // TODO COMMUNCATE BETWEEN AUTOMODULES AND MOVING
    // TODO JUNCTION SCANNER BETTER

    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);
        lift.maintain();
        outtake.readyStart(); outtake.closeClaw();
//        scan(false);
//        setScannerAfterInit(MecanumJunctionReactor2.junctionScanner);
        x = 0; s = 0; y = 0;
    }

    @Override
    public void preProcess() {
        caseDetected = Case.SECOND;
    }

    @Override
    public void define() {

        // Pre-loaded cone move
        addConcurrentAutoModule(BackwardAutoReadyFirst);
        addWaypoint(1.0, 6, 122, 0);
        addCancelAutoModules();
        addConcurrentAutoModule(BackwardAuto2First);
        // Pre-loaded cone place
        addTimedSetpoint(1.0, 0.7, timeToPlace+0.5, -6.5, 138, 45);
        addConcurrentAutoModule(ForwardAuto2(0));
        addPause(pauseBetweenCycles);
        // Start 5 cycle
        customNumber(5, i -> {
            switch (i){
                case 0: x = -0.2; s = -0.2;  break;
                case 1: x = 0.6;  s = 1.0;  break;
                case 2: x = 1.1;  s = 1.5;  break;
                case 3: x = 1.7;  s = 2.0;  break;
                case 4: x = 2.4;  s = 2.5;   break;
            }
            // Move to pick
            addSegment(0.7, mecanumDefaultWayPoint, 18-x, 128 + s, 65);
            addSegment(0.7, mecanumDefaultWayPoint, 55-x, 122 + s, 87);
            // Pick
            addTimedWaypoint( 0.2, 0.3, 65-x, 124 + s, 87);
            addConcurrentAutoModule(GrabAuto2);
            addTimedWaypoint( 0.2, 0.3, 61-x, 124 + s, 87);
            // Move to place
            addCancelAutoModules();
            addConcurrentAutoModule(BackwardAutoReady);
            addSegment(0.7, mecanumDefaultWayPoint, 30-x, 124 + s, 85);
            addSegment(0.7, mecanumDefaultWayPoint, 11-x, 135 + s, 52);
            addCancelAutoModules();
            addConcurrentAutoModule(BackwardAuto2);
            // Place
            addTimedSetpoint(1.0, 0.6, timeToPlace, -7.3-x, 136 + s, 53);
            addCancelAutoModules();
            addConcurrentAutoModule(ForwardAuto2(i+1));
            addPause(pauseBetweenCycles);
            addBreakpoint(() -> autoMode.equals(AutoMode.SIMPLE) && i+1 == 3);
        });
        addBreakpoint(() -> autoMode.equals(AutoMode.NORMAL));
        y = 125;
        // Move to other side
        addConcurrentAutoModule(ForwardAuto2(0));
        addTimedWaypoint(0.2, 0.5, -8.5, y, -90);
        addSegment(1.0, mecanumDefaultWayPoint,  -50.0, y, -93);
        addSegment(1.0, mecanumDefaultWayPoint, -220.0, y, -93);
        addSegment(0.2, mecanumDefaultWayPoint,  -232.0, y, -93);
        // First cone pick up
        addTimedWaypoint(0.2, 0.3, -240, y+1, -93);
        addConcurrentAutoModule(GrabAuto2);
        addTimedWaypoint(0.2, 0.3, -235, y+1, -93);
        // First cone move to place
        addCancelAutoModules();
        addConcurrentAutoModule(BackwardAutoReady);
        addSegment(0.7, mecanumDefaultWayPoint, -200, y, -85);
        addSegment(0.7, mecanumDefaultWayPoint, -183, y+10, -52);
        addCancelAutoModules();
        addConcurrentAutoModule(BackwardAuto2);
        // First cone place
        addTimedSetpoint(1.0, 0.5, timeToPlace, -167, y+11.5, -52);
        addCancelAutoModules();
        addConcurrentAutoModule(ForwardAuto2(0));
        addPause(pauseBetweenCycles);
        // Start 2 cycle
        customNumber(2, i -> {
            addBreakpoint(() -> timer.seconds() > 29);
            switch (i){
                case 0: x = 0.0; s = -0.0;  break;
                case 1: x = 0.0;  s = 0.0;  break;
            }
            addSegment(0.7, mecanumDefaultWayPoint,  -186, y+10, -65);
            addSegment(0.7, mecanumDefaultWayPoint, -230, y, -93);
            addTimedWaypoint(0.2, 0.3, -239, y, -93);
            addConcurrentAutoModule(GrabAuto2);
            addTimedWaypoint(0.2, 0.3, -235, y, -93);
            addCancelAutoModules();
            addConcurrentAutoModule(BackwardAutoReady);
            addSegment(0.7, mecanumDefaultWayPoint, -200, y, -85);
            addSegment(0.7, mecanumDefaultWayPoint, -183, y+10, -52);
            addCancelAutoModules();
            addConcurrentAutoModule(BackwardAuto2);
            addTimedSetpoint(1.0, 0.5, timeToPlace, -165, y+11.5, -52);
            addCancelAutoModules();
            addConcurrentAutoModule(ForwardAuto2(i+1));
            addPause(pauseBetweenCycles);
        });

        addBreakpointReturn();
        // Start park
        customIf(autoMode.equals(AutoMode.MEGA), () -> {
            // Park other side
            customCase(() -> {
                addTimedSetpoint(1.0, 1.0, 0.5, -220.0, y, 90);
            }, () -> {
                addTimedSetpoint(1.0, 0.8, 0.5, -176.0, y, 90);
            }, () -> {
                addTimedSetpoint(1.0, 1.0, 0.5, -150.0, y, 90);
            });
        }, () -> {
            // Park

        });
        // End





//        customCase(() -> {
//            addWaypoint(-7, 124, 90);
//            addScaledWaypoint(0.8, -10, 124, 90);
//            addScaledWaypoint(0.8, -45, 122, 60);
//            addScaledWaypoint(0.8, -50, 123, 25);
//            addScaledSetpoint(0.9, -62, 75, 0);
//        }, () -> {
//            addWaypoint(0, 130, 35);
//            addWaypoint(0, 105, 0);
//            addScaledSetpoint(0.9, 0, 75, 0);
//        }, () -> {
//            addWaypoint(7, 128, 90);
//            addScaledWaypoint(0.8, 16, 128, 90);
//            addScaledWaypoint(0.8, 39, 122, 58);
//            addScaledWaypoint(0.8, 51, 111, 32);
//            addScaledWaypoint(0.8, 56, 95, 0);
//            addScaledSetpoint(0.9, 58, 75, 0);
//        });
    }


    @Override
    public void postProcess() {
        autoPlane.reflectY(); autoPlane.reflectX();
    }

    @Override
    public void stopAuto() {
//        bot.savePose(odometry.getPose());
//        bot.saveLocationOnField();
        super.stopAuto();
    }

    @Autonomous(name = "TerraAutoMegaRight", group = "auto", preselectTeleOp = "TerraOp")
    public static class TerraAutoMegaRight extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); autoMode = AutoMode.MEGA;}}
    @Autonomous(name = "TerraAutoMegaLeft", group = "auto", preselectTeleOp = "TerraOp")
    public static class TerraAutoMegaLeft extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); autoMode = AutoMode.MEGA; }}

    @Autonomous(name = "TerraAutoNormalRight", group = "auto", preselectTeleOp = "TerraOp")
    public static class TerraAutoNormalRight extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); autoMode = AutoMode.NORMAL;}}
    @Autonomous(name = "TerraAutoNormalLeft", group = "auto", preselectTeleOp = "TerraOp")
    public static class TerraAutoNormalLeft extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); autoMode = AutoMode.NORMAL;}}

    @Autonomous(name = "TerraAutoSimpleRight", group = "auto", preselectTeleOp = "TerraOp")
    public static class TerraAutoSimpleRight extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); autoMode = AutoMode.SIMPLE;}}
    @Autonomous(name = "TerraAutoSimpleLeft", group = "auto", preselectTeleOp = "TerraOp")
    public static class TerraAutoSimpleLeft extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); autoMode = AutoMode.SIMPLE;}}
}

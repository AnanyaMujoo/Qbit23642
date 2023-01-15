package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.AutoFramework;
import autoutil.reactors.MecanumJunctionReactor2;
import autoutil.reactors.Reactor;
import elements.Case;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.template.Mode;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;

public class TerraAutoNormal extends AutoFramework {

    protected enum AutoMode implements Mode.ModeType {NORMAL, SIMPLE}
    protected AutoMode autoMode;


    private double x, s, y;
    private final double pauseBetweenCycles = 0.2;

    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);
        lift.maintain();
        outtake.readyStart(); outtake.closeClaw();
        scan(false);
        setScannerAfterInit(MecanumJunctionReactor2.junctionScanner);
        x = 0; s = 0; y = 0;
    }

    @Override
    public void preProcess() {
        caseDetected = Case.THIRD;
    }

    Stage junctionStop(){ return new Stage(new Main(() -> MecanumJunctionReactor2.stop = true), RobotPart.exitAlways()); }

    AutoModule BackwardFirst = new AutoModule(
            outtake.stageMiddle(0.0),
            RobotPart.pause(1.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)-2).attach(outtake.stageReadyEndAfter(0.45)),
            junctionStop()
    );

    AutoModule Backward = new AutoModule(
            RobotPart.pause(1.0),
            outtake.stageReadyEnd(0.35),
            outtake.stageOpen(0.0),
            junctionStop()
    );

    AutoModule BackwardReady = new AutoModule(
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)-2)
    );

    AutoModule Forward(int i){return new AutoModule(
            Reactor.forceExit(),
            outtake.stageOpen(0.0),
            lift.stageLift(1.0,  i == 0 ? 14.5 : Math.max(15.0 - (i*15.0/5.0), 0)).attach(outtake.stageStartAfter(0.15))
    );}

    AutoModule Grab = new AutoModule(
            outtake.stageClose(0.15),
            lift.moveTime(1,0.2).attach(outtake.stageReadyStartAfter(0.1))
    );

    @Override
    public void define() {

        // Pre-loaded cone move
        addWaypoint(1.0, 6, 118, 10);
        addConcurrentAutoModuleWithCancel(BackwardFirst);
        // Pre-loaded cone place
        addTimedSetpoint(1.0, 0.7, 0.8, -4.5, 136, 45);
        addSegment(mecanumJunctionSetpoint2, 0, 0, 0);
        addConcurrentAutoModule(Forward(0));
        addPause(pauseBetweenCycles);

        // Start 5 cycle
        customNumber(5, i -> {
            switch (i){
                case 0: x = -0.7; s = -0.2;  break;
                case 1: x = 0.6;  s = 1.0;  break;
                case 2: x = 1.1;  s = 1.5;  break;
                case 3: x = 1.7;  s = 2.0;  break;
                case 4: x = 2.4;  s = 2.5;   break;
            }
            // Move to pick
            addSegment(0.7, mecanumDefaultWayPoint, 18-x, 128 + s,  i == 0 ? 80 : 65);
            addSegment(0.7, mecanumDefaultWayPoint, 56-x, i == 0 ? 128 : 124 + s, 87);
            // Pick
            addTimedWaypoint( 0.2, 0.3, 66-x, 126 + s, 87);
            addConcurrentAutoModuleWithCancel(Grab);
            addTimedWaypoint( 0.2, 0.3, 62-x, 126 + s, 89);
            // Move to place
            addConcurrentAutoModuleWithCancel(BackwardReady);
            addSegment(0.7, mecanumDefaultWayPoint, 30-x, 124 + s, 80);
            addSegment(0.65, mecanumDefaultWayPoint, 11-x, 135 + s, 50);
            addConcurrentAutoModuleWithCancel(Backward);
            // Place
            addTimedSetpoint(1.0, 0.6, 0.5, -5.3-x, 134 + s, 53);
            addSegment(mecanumJunctionSetpoint2, 0, 0, 0);
            addConcurrentAutoModule(Forward(i+1));
            addPause(pauseBetweenCycles);
//            addBreakpoint(() -> autoMode.equals(TerraAuto.AutoMode.SIMPLE) && i+1 == 3);
        });
//        addConcurrentAutoModule(ForwardAuto3(0));
        // Park
//        customCase(() -> {
//            addTimedWaypoint(1.0, 0.2, -170.0, y, -93);
//            addTimedSetpoint(1.0, 1.0,0.9, -227.0, y, -93);
//        }, () -> {
//            addTimedSetpoint(1.0, 0.8, 0.4, -170.0, y, -93);
//        }, () -> {
//            addTimedWaypoint(1.0, 0.2, -170.0, y, -93);
//            addTimedSetpoint(1.0, 1.0, 0.9, -120.0, y, -93);
//        });
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


    @Autonomous(name = "TerraAutoNormalRight", group = "auto")
    public static class TerraAutoNormalRight extends TerraAutoNormal {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); autoMode = AutoMode.NORMAL;}}
    @Autonomous(name = "TerraAutoNormalLeft", group = "auto")
    public static class TerraAutoNormalLeft extends TerraAutoNormal {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); autoMode = AutoMode.NORMAL;}}
//
//    @Autonomous(name = "TerraAutoSimpleRight", group = "auto")
//    public static class TerraAutoSimpleRight extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); autoMode = AutoMode.SIMPLE;}}
//    @Autonomous(name = "TerraAutoSimpleLeft", group = "auto")
//    public static class TerraAutoSimpleLeft extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); autoMode = AutoMode.SIMPLE;}}
}

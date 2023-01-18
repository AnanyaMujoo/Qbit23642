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
import static global.General.log;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;

public class TerraAutoNormal extends AutoFramework {

    protected enum AutoMode implements Mode.ModeType {NORMAL, SIMPLE}
    protected AutoMode autoMode;

    private double x, s;

    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);
        lift.maintain();
        outtake.readyStart(); outtake.closeClaw();
        scan(false);
        x = 0; s = 0;
    }

    AutoModule BackwardFirst = new AutoModule(
            lift.changeCutoff(1.0),
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5).attach(outtake.stageReadyEndAfter(0.4))
    );

    AutoModule Backward = new AutoModule(
            outtake.stageMiddle(0.2),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+4.0).attach(outtake.stageReadyEndAfter(0.4))
    );

    AutoModule Forward(int i){return new AutoModule(
            outtake.stageEnd(0.1),
            outtake.stageOpen(0.1),
            lift.moveTime(-0.5, 0.1),
            lift.stageLift(1.0,  i == 0 ? 14.5 : Math.max(15.0 - (i*15.0/5.0), 0)).attach(outtake.stageStartAfter(0.1))
    );}

    AutoModule Grab = new AutoModule(
            outtake.stageClose(0.15),
            lift.moveTime(1,0.2).attach(outtake.stageReadyStartAfter(0.1))
    );

    @Override
    public void define() {

        // Pre-loaded cone move
        customFlipped(() -> {
            addWaypoint(1.0, 0, 126, 0);
            addWaypoint(0.7, 4, 118, 10);
        }, () -> {
            addWaypoint(1.0, -4, 126, 0);
            addWaypoint(0.7, 0, 118, 10);
        });
        // Pre-loaded cone place
        addConcurrentAutoModuleWithCancel(BackwardFirst);
        customFlipped(() -> {
            addTimedSetpoint(1.0, 0.7, 0.9, -1.5, 132, 45);
            addTimedSetpoint(1.0, 0.7, 0.5, -9.5, 140, 45);
        }, () -> {
            addTimedSetpoint(1.0, 0.7, 0.9, -1.5, 122, 45);
            addTimedSetpoint(1.0, 0.7, 0.5, -9.5, 130, 45);
        });

        addConcurrentAutoModule(Forward(0));
        addPause(0.2);

        // Start 5 cycle
        customNumber(5, i -> {
            customFlipped(() -> {
                switch (i){
                    case 0: x = -0.5; s = -0.2;  break;
                    case 1: x = 0.6;  s = 1.0;  break;
                    case 2: x = 0.6;  s = 1.5;  break;
                    case 3: x = 1.0;  s = 2.0;  break;
                    case 4: x = 1.0;  s = 2.5;   break;
                }
            }, () -> {
                switch (i){
                    case 0: x = 2.0; s = -0.2;  break;
                    case 1: x = 3.0;  s = 1.0;  break;
                    case 2: x = 3.0;  s = 1.5;  break;
                    case 3: x = 4.0;  s = 2.0;  break;
                    case 4: x = 4.0;  s = 2.5;   break;
                }
            });
            // Move to pick
            addSegment(0.7, mecanumDefaultWayPoint, 18-x, 128 + s, 80);
            addSegment(0.6, mecanumDefaultWayPoint, 59-x, 125 + s, 87);
            // Pick
            addTimedWaypoint( 0.2, 0.3, 66-x, 126 + s, 87);
            addConcurrentAutoModuleWithCancel(Grab);
            addTimedWaypoint( 0.2, 0.3, 62-x, 126 + s, 89);
            // Move to place
            addConcurrentAutoModuleWithCancel(Backward);
            addSegment(0.7, mecanumDefaultWayPoint, 30-x, 124 + s, 80);
            addSegment(0.65, mecanumDefaultWayPoint, 11-x, 132 + s, 50);
            // Place
            customFlipped(() -> {
                addTimedSetpoint(1.0, 0.6, 0.4, -1.3 - x, 134 + s, 53);
                addTimedSetpoint(1.0, 0.6, 0.8, -9 - x, 143 + s, 53);
            }, () -> {
                addTimedSetpoint(1.0, 0.6, 0.4, -0.3 - x, 126 + s, 53);
                addTimedSetpoint(1.0, 0.6, 0.8, -7.3 - x, 134 + s, 53);
            });
            addConcurrentAutoModuleWithCancel(Forward(i+1));
//            addBreakpoint(() -> autoMode.equals(TerraAuto.AutoMode.SIMPLE) && i+1 == 3);
        });
        addTimedWaypoint(0.8, 0.5, 2.4, 125, 53);
//        // Park
        customCase(() -> {
            addSegment(0.7, mecanumDefaultWayPoint, -7, 128, 90);
            addSegment(0.7, mecanumDefaultWayPoint, -10, 128, 90);
            addSegment(0.7, mecanumDefaultWayPoint, -45, 125, 60);
            addSegment(0.7, mecanumDefaultWayPoint, -50, 126, 25);
            addTimedSetpoint(1.0, 0.8, 2.0, -58, 78, 0);
        }, () -> {
            addTimedWaypoint(0.7, 0.5, 3.0, 122, 0);
            addTimedSetpoint(1.0, 0.8, 2.0, 3.0, 78, 0);
        }, () -> {
            addSegment(0.7, mecanumDefaultWayPoint, 7, 130, 90);
            addSegment(0.7, mecanumDefaultWayPoint, 39, 130, 58);
            addSegment(0.7, mecanumDefaultWayPoint, 51, 114, 32);
            addSegment(0.7, mecanumDefaultWayPoint,  56, 98, 0);
            addTimedSetpoint(1.0, 0.8, 2.0, 65, 78, 0);
        });
        addPause(0.1);
        // End
    }


    @Override
    public void postProcess() {
        autoPlane.reflectY(); autoPlane.reflectX();
    }
    @Autonomous(name = "TerraAutoNormalRight", group = "auto", preselectTeleOp = "TerraOp")
    public static class TerraAutoNormalRight extends TerraAutoNormal {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); autoMode = AutoMode.NORMAL;}}
    @Autonomous(name = "TerraAutoNormalLeft", group = "auto", preselectTeleOp = "TerraOp")
    public static class TerraAutoNormalLeft extends TerraAutoNormal {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); autoMode = AutoMode.NORMAL;}}

//    @Autonomous(name = "TerraAutoSimpleRight", group = "auto")
//    public static class TerraAutoSimpleRight extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); autoMode = AutoMode.SIMPLE;}}
//    @Autonomous(name = "TerraAutoSimpleLeft", group = "auto")
//    public static class TerraAutoSimpleLeft extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); autoMode = AutoMode.SIMPLE;}}
}

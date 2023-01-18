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
import static global.Modes.Height.MIDDLE;

public class TerraAutoMega extends AutoFramework {

    private double x, s, y;

    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);
        lift.maintain();
        outtake.readyStart(); outtake.closeClaw();
        scan(false);
        x = 0; s = 0; y = 125;
    }

    AutoModule BackwardReadyFirst = new AutoModule(
            outtake.stageMiddle(0.0),
            lift.changeCutoff(1),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE))
    );

    AutoModule BackwardFirst = new AutoModule(
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5)
    );

    AutoModule Backward = new AutoModule(
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+4.0)
    );

    AutoModule Forward(int i){return new AutoModule(
            outtake.stageEnd(0.05),
            outtake.stageOpen(0.0),
            lift.moveTime(-0.6, 0.15),
            lift.stageLift(1.0,  i == 0 ? 14.5 : Math.max(15.0 - (i*15.0/5.0), 0)).attach(outtake.stageStartAfter(0.1))
    );}

    AutoModule Grab = new AutoModule(
            outtake.stageClose(0.15),
            lift.moveTime(1,0.2).attach(outtake.stageReadyStartAfter(0.1))
    );

    @Override
    public void define() {

        // Pre-loaded cone move
        addConcurrentAutoModule(BackwardReadyFirst);
        addWaypoint(1.0, 6, 118, 10);
        addConcurrentAutoModuleWithCancel(BackwardFirst);
        // Pre-loaded cone place
        addTimedSetpoint(1.0, 0.7, 0.9, -6.5, 138, 45);
        addConcurrentAutoModule(Forward(0));
        // Start 5 cycle
        customNumber(5, i -> {
            switch (i){
                case 0: x = -0.5; s = -0.2;  break;
                case 1: x = 0.6;  s = 1.0;  break;
                case 2: x = 0.6;  s = 1.5;  break;
                case 3: x = 1.0;  s = 2.0;  break;
                case 4: x = 1.0;  s = 2.5;   break;
            }
            // Move to pick
            addSegment(0.8, mecanumDefaultWayPoint, 18-x, 128 + s, 80);
            addSegment(0.7, mecanumDefaultWayPoint, 55-x, 125 + s, 87);
            // Pick
            addTimedWaypoint( 0.2, 0.2, 66-x, 126 + s, 87);
            addConcurrentAutoModuleWithCancel(Grab);
            addTimedWaypoint( 0.2, 0.2, 62-x, 126 + s, 89);
            // Move to place
            addConcurrentAutoModuleWithCancel(Backward);
            addSegment(0.8, mecanumDefaultWayPoint, 30-x, 124 + s, 80);
            addSegment(0.7, mecanumDefaultWayPoint, 11-x, 132 + s, 50);
            // Place
            addTimedSetpoint(1.0, 0.6, 0.7, -9 - x, 143 + s, 53);
            addConcurrentAutoModuleWithCancel(Forward(i+1 == 5 ? 0 : i+1));
        });
        addTimedWaypoint(0.7, 0.5, 8.5, 125, -90);
        // Move to other side
        addSegment(1.0, mecanumDefaultWayPoint,  -225.0, y, -93);
        addSegment(0.2, mecanumDefaultWayPoint,  -237.0, y, -93);
        // First cone pick up
        addTimedWaypoint(0.2, 0.2, -240, y+1, -93);
        addConcurrentAutoModule(GrabAuto2);
        addTimedWaypoint(0.2, 0.2, -237, y+1, -93);
        // First cone move to place
        addConcurrentAutoModuleWithCancel(Backward);
        addSegment(0.8, mecanumDefaultWayPoint, -200, y, -85);
        addSegment(0.7, mecanumDefaultWayPoint, -183, y+10, -52);
        // First cone place
        addTimedSetpoint(1.0, 0.6, 0.7, -167, y+11.5, -52);
        addConcurrentAutoModuleWithCancel(Forward(0));
        // Start 2 cycle
        customNumber(2, i -> {
            // Check enough time?
            addBreakpoint(() -> timer.seconds() > 25.2);
            switch (i){
                case 0: x = 0.0; s = -0.0;  break;
                case 1: x = 0.0; s = 0.0;  break;
            }
            // Move to pick
            addSegment(1.0, mecanumDefaultWayPoint,  -186, y+10, -65);
            addSegment(0.5, mecanumDefaultWayPoint, -228, y, -93);
            // Pick
            addTimedWaypoint(0.2, 0.2, -239, y, -93);
            addConcurrentAutoModule(Grab);
            addTimedWaypoint(0.2, 0.2, -236, y, -93);
            // Move to place
            addConcurrentAutoModuleWithCancel(Backward);
            addSegment(0.8, mecanumDefaultWayPoint, -200, y, -85);
            addSegment(0.7, mecanumDefaultWayPoint, -183, y+10, -52);
            // Place
            addTimedSetpoint(1.0, 0.6, 0.7, -165, y+11.5, -52);
            addConcurrentAutoModuleWithCancel(Forward(i+1));
        });

        addBreakpointReturn();
        // Park other side
        customCase(() -> {
            addTimedWaypoint(1.0, 0.2, -170.0, y, -93);
            addTimedSetpoint(1.0, 1.0,0.9, -227.0, y, -93);
        }, () -> {
            addTimedSetpoint(1.0, 0.8, 0.4, -170.0, y, -93);
        }, () -> {
            addTimedWaypoint(1.0, 0.2, -170.0, y, -93);
            addTimedSetpoint(1.0, 1.0, 0.9, -120.0, y, -93);
        });
        addPause(0.1);
        // End
    }


    @Override
    public void postProcess() {
        autoPlane.reflectY(); autoPlane.reflectX();
    }
//    preselectTeleOp = "TerraOp"
    @Autonomous(name = "TerraAutoMegaRight", group = "auto")
    public static class TerraAutoMegaRight extends TerraAutoMega {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}
    @Autonomous(name = "TerraAutoMegaLeft", group = "auto")
    public static class TerraAutoMegaLeft extends TerraAutoMega {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}

}

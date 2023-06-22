package auton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import autoutil.AutoFramework;
import elements.Case;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.Timer;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;
import static global.Modes.Height.HIGH;

public class TerraAutoPark extends AutoFramework {


    @Override
    public void initialize() {
        TerraAutoRam.normalInit(this);
        caseDetected = Case.THIRD;
    }





    @Override
    public void define() {

        addSegment(0.5, mecanumDefaultWayPoint, 0, 90, 0);
        addSegment(0.5, mecanumDefaultWayPoint, 7, 80, 45);
        addSegment(0.5, mecanumDefaultWayPoint, -7, 70, 90);
        addSegment(0.5, mecanumDefaultWayPoint, -40, 70, 90);

        customFlipped(() -> {
            addTimedSetpoint(1.0,0.5,1.5, -68,81,56);
        }, () -> {
            addTimedSetpoint(1.0,0.5,1.5, -68,81,45);
        });
        addConcurrentAutoModuleWithCancel(TerraAutoRam.BackwardFirst, 2.0);
        addConcurrentAutoModuleWithCancel(TerraAutoRam.Forward, 1.2);
        addTimedSetpoint(1.0,0.6,1.0, -53,70,56);
        addTimedSetpoint(1.0, 0.5,0.6, -57, 70, 90);
        addConcurrentAutoModule(new AutoModule(outtake.stage(0.2, 0.1), outtake.stageOpenComp(0.0),  lift.stageLift(1.0,  -0.5)));
        customCase(() -> {
            addTimedSetpoint(1.0, 0.5,1.0, -57, 70, 0);
        }, () -> {
            addSegment(0.5, noStopNewSetPoint, 0, 70, 90);
            addTimedSetpoint(1.0, 0.5,1.0, 0, 70, 0);
        }, () -> {
            addSegment(0.5, noStopNewSetPoint, 58, 70, 90);
            addTimedSetpoint(1.0, 0.5,1.0, 58, 70, 0);
        });
        addPause(0.1);

    }


    @Override
    public void postProcess() { autoPlane.reflectY(); autoPlane.reflectX(); }


    @Autonomous(name = "I. RIGHT PARK + PRELOAD", group = "auto", preselectTeleOp = "TerraOp")
    public static class RIGHT extends TerraAutoPark {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}

    @Autonomous(name = "J. LEFT PARK + PRELOAD", group = "auto", preselectTeleOp = "TerraOp")
    public static class LEFT extends TerraAutoPark {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}

}

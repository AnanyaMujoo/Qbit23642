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

public class TerraAutoRam extends AutoFramework {
    boolean[] halt = {false};

    public static void normalInit(AutoFramework auto){
        auto.setConfig(mecanumNonstopConfig);
        bot.saveLocationOnField();
        lift.maintain();
        outtake.closeClaw();
        wait(0.5);
        outtake.readyStart();
        auto.scan(false);
    }

    static AutoModule BackwardFirst = new AutoModule(
            lift.stageLift(1.0, heightMode.getValue(HIGH)+1).attach(outtake.stageReadyEndContinuous(0.6))
    ).setStartCode(outtake::moveMiddle);

    AutoModule Forward = new AutoModule(
            RobotPart.pause(0.1),
            outtake.stageOpen(0.1),
            outtake.stageStart(0.0),
            lift.stageLift(1.0,0)
    ).setStartCode(outtake::moveEnd);

    @Override
    public void initialize() {
        normalInit(this);
        caseDetected = Case.FIRST;
    }

    @Override
    public void define() {

        customSide(() -> {
            addSegment(0.8, mecanumDefaultWayPoint, 0, 30, 0);
            addSegment(0.5, mecanumDefaultWayPoint, -3,90,-30);
            addSegment(0.9, mecanumDefaultWayPoint, 15, 130, -30);
            addSegment(0.5, mecanumDefaultWayPoint, 5, 115, -30);
        }, () -> {
            addSegment(0.8, mecanumDefaultWayPoint, 0, 30, 0);
            addSegment(0.5, mecanumDefaultWayPoint, -3,90,-30);
            addSegment(0.9, mecanumDefaultWayPoint, 15, 130, -30);
            addSegment(0.5, mecanumDefaultWayPoint, 5, 115, -30);
        });



        customNumber(5, i -> {
            addTimedSetpoint(1.0, 0.5,1.5,2, 70, 0);
            addSegment(1.5, 1.0, mecanumNonstopSetPoint, 2, 155, 0);
            addCustomCode(() -> {
                if (odometry.getY() < -155) {
                    halt[0] = true;
                }
//                log.show("HALT", halt[0]);
                drive.halt();
            });
            addBreakpoint(() -> halt[0]);
        });
        addBreakpointReturn();

        customSide(() -> {
            addTimedSetpoint(1.0, 0.5, 0.6, -12, 125, 30);
            addConcurrentAutoModuleWithCancel(BackwardFirst, 0.2);
            addTimedSetpoint(1.0, 0.5, 1.3, -16, 136, 30);
        }, () -> {
            addTimedSetpoint(1.0, 0.5, 0.6, -12, 125, 30);
            addConcurrentAutoModuleWithCancel(BackwardFirst, 0.2);
            addTimedSetpoint(1.0, 0.5, 1.3, -16, 136, 30);
        });
        addConcurrentAutoModuleWithCancel(Forward, 0.5);
        addSegment(0.4, mecanumDefaultWayPoint, 0, 120, 0);
        addSegment(0.5, noStopNewSetPoint, 0, 73, 0);
        customCase(() -> {
            addTimedSetpoint(1.0, 0.5,2.0,-58, 73, 0);
        }, () -> {

        }, () -> {
            addTimedSetpoint(1.0, 0.5,2.0,58, 73, 0);
        });
        addPause(0.1);
    }


    @Override
    public void postProcess() { autoPlane.reflectY(); autoPlane.reflectX(); }


    @Autonomous(name = "E. RIGHT RAM", group = "auto", preselectTeleOp = "TerraOp")
    public static class RIGHT extends TerraAutoRam {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}

    @Autonomous(name = "F. LEFT RAM", group = "auto", preselectTeleOp = "TerraOp")
    public static class LEFT extends TerraAutoRam {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}

}

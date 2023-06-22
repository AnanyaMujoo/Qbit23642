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
import util.User;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;
import static global.Modes.Height.HIGH;

public class TerraAutoFight extends AutoFramework {

    double y = 0;
    boolean normal = false;

    @Override
    public void initialize() {
        TerraAutoRam.normalInit(this);
        caseDetected = Case.SECOND;
        y = 0;
    }


    AutoModule ForwardFirst = new AutoModule(
            outtake.stageEnd(0.2),
            outtake.stageOpen(0.2),
            lift.stageLift(1.0,  11.5).attach(outtake.stageStartAndSignal())
    ).setStartCode(outtake::moveEnd);

    AutoModule GrabBack = new AutoModule(
            outtake.stageClose(0.1),
            drive.moveTime(-0.1, 0, 0, 0.1),
            lift.moveTime(1.0, 0.2),
            outtake.stageMiddleWithoutFlip(0.2),
            outtake.stageFlip(0.0)
    );

    AutoModule Forward(int i){return new AutoModule(
            outtake.stageEnd(0.2),
            outtake.stageOpen(0.2),
            lift.stageLift(1.0,  Math.max(11.5 - (i*11.5/4.6), -0.5)).attach(outtake.stageStartAndSignal())
    );}


    @Override
    public void define() {

        if(!normal) {
            TerraAutoRam.signal(this);
        }else{
            addTimedSetpoint(1.0, 0.4, 0.6, 0, 40, -90);
            addSegment(0.5, mecanumDefaultWayPoint, 0, 70, 0);
            addTimedSetpoint(1.0, 0.5, 0.6, 0, 100, 0);
        }


        if(!normal) {
            addConcurrentAutoModuleWithCancel(new AutoModule(outtake.stageFlip(0.0), outtake.stageMiddle(0.0)));
            addTimedSetpoint(1.0, 0.6, 0.35, 7, 126, -30);
            addConcurrentAutoModuleWithCancel(new AutoModule(lift.moveTime(1, 0.3)));
        }else{
            addConcurrentAutoModuleWithCancel(new AutoModule(outtake.stageFlip(0.0), outtake.stageMiddle(0.0), lift.moveTime(1, 0.3)));
        }

        customSide(() -> {
            addTimedSetpoint(1.0, 0.5, 0.4, -6, 134, 35);
            addConcurrentAutoModuleWithCancel(TerraAutoRam.BackwardFirst);
            addTimedSetpoint(1.0, 0.4, 0.8, -12, 140, 40);
        }, () -> {
            addTimedSetpoint(1.0, 0.5, 0.4, -8, 134, 35);
            addConcurrentAutoModuleWithCancel(TerraAutoRam.BackwardFirst);
            addTimedSetpoint(1.0, 0.4, 0.8, -13, 140, 40);
        });


        addConcurrentAutoModuleWithCancel(ForwardFirst, 0.4);
        customNumber(5, i -> {
            customFlipped(() -> {
                if(i > 1){
                    y += -1;
                }
            }, () -> {
                if(i > 1){
                    y += -0.5;
                }else{
                    y = -1;
                }
            });


            addSegment(0.7, mecanumDefaultWayPoint, 18, 129+y, 90);
            customFlipped(() -> {
                addSegment(0.6, mecanumDefaultWayPoint, 50, 127+y, 90);
                addSegment(0.4, slowDownStopSetPoint, 71, 127+y, 90);
            }, () -> {
                addSegment(0.6, mecanumDefaultWayPoint, 52, 127+y, 90);
                addSegment(0.4, slowDownStopSetPoint, 74, 127+y, 90);
            });
            addCustomCode(() -> whileTime(() -> {}, 0.05));
            customFlipped(() -> {
                addBreakpoint(() -> odometry.getX() > -63+(0.2*i));
            }, () -> {
                addBreakpoint(() -> odometry.getX() < 63-(0.2*i));
            });
            addCustomCode(() -> {
//                showForTime("Pos", odometry.getX(), 10);
                bot.addAutoModuleWithCancel(GrabBack);
                pause(0.45);
            });
            addSegment(0.6, mecanumDefaultWayPoint, 20, 129+y, 85);
            addTimedSetpoint(1.0, 0.4, 0.4, -9, 132+y, 55);
            addConcurrentAutoModuleWithCancel(TerraAutoRam.BackwardFirst);
            customFlipped(() -> {
                addTimedSetpoint(1.0, 0.4, 0.8, -13, 140+y, 40);
            }, () -> {
                addTimedSetpoint(1.0, 0.4, 0.8, -15, 140+y, 40);
            });
            addConcurrentAutoModuleWithCancel(Forward(i+1), 0.4);
        });
        addBreakpointReturn();
        addCustomCode(outtake::openClaw);
        addSegment(0.5, mecanumDefaultWayPoint, 18, 127, 90);
        addConcurrentAutoModule(new AutoModule(outtake.stage(0.2, 0.1), outtake.stageOpenComp(0.0),  lift.stageLift(1.0,  -0.5)));
        customCase(() -> {
            addSegment(0.8, noStopNewSetPoint, -57, 127, 90);
        }, () -> {
            addSegment(0.4, noStopNewSetPoint, 0, 127, 90);
        }, () -> {
            addSegment(0.8, noStopNewSetPoint, 58, 127, 90);
        });
        addPause(0.5);


    }


    @Override
    public void postProcess() { autoPlane.reflectY(); autoPlane.reflectX(); }



    @Autonomous(name = "A. RIGHT NORMAL (No SS)", group = "auto", preselectTeleOp = "TerraOp")
    public static class RIGHT extends TerraAutoFight {{ normal = true; fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}

    @Autonomous(name = "B. LEFT NORMAL (No SS)", group = "auto", preselectTeleOp = "TerraOp")
    public static class LEFT extends TerraAutoFight {{ normal = true; fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}




    @Autonomous(name = "C. RIGHT FIGHT (SS)", group = "auto", preselectTeleOp = "TerraOp")
    public static class RIGHT_FIGHT extends TerraAutoFight {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}

    @Autonomous(name = "D. LEFT FIGHT (SS)", group = "auto", preselectTeleOp = "TerraOp")
    public static class LEFT_FIGHT extends TerraAutoFight {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}




}

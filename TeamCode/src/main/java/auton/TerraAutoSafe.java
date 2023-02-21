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
import util.ExceptionCatcher;
import util.template.Mode;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.Modes.Height.HIGH;

public class TerraAutoSafe extends AutoFramework {

    private double x, s;

    @Override
    public void initialize() {
        TerraAutoNormal.normalInit(this);
        x = 0;
        s = 0;
    }

    // TODO FIX TURNING AT END (MAKE LIFT GO UP LATER)
    // TODO FIX PATH TAKEN
    // TODO CREATE OTHER SIDE
    // TODO FIX GRABBING FROM STACK

    // TODO FIX CYCLE MACHINE
    // TODO FIX Y MACHINE
    // TODO FIX CYCLE STACK MACHINE

    AutoModule BackwardFirst = new AutoModule(
            RobotPart.pause(0.1),
            lift.changeCutoff(1.0),
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH) + 1).attach(outtake.stageReadyEndAfter(0.3))
    );

    AutoModule Backward = new AutoModule(
            RobotPart.pause(0.15),
            outtake.stageFlip(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH) + 2).attach(outtake.stageReadyEndAfter(0.25))
    );

    AutoModule Forward(int i) { return new AutoModule(
            outtake.stageEnd(0.1),
            outtake.stageOpen(0.0),
            lift.moveTime(-0.7, 0.15),
            lift.stageLift(1.0, i == 0 ? 15.0 : Math.max(15.0 - (i * 15.0 / 4.6), 0)).attach(outtake.stageStartAfter(0.1))
    );}


    AutoModule GrabBack = new AutoModule(
            outtake.stageClose(0.25),
            lift.moveTimeBack(-0.75, 0.5, () -> 0.15).attach(outtake.stageReadyStart(0.0))
    );
//
//    @Override
//    public void preProcess() {
//        caseDetected = Case.FIRST;
//    }

    // TODO PROBLEM WHEN MOVING TOO FAST USE DISTANCE SENSOR
    // TODO FIX

    @Override
    public void define() {

        // Pre-loaded cone move
        customFlipped(() -> {
            addSegment(1.0, mecanumDefaultWayPoint, -2.0, 80, 0.0);
            addSegment(0.5, mecanumDefaultWayPoint, -6.0, 105, 80.0);
            addConcurrentAutoModuleWithCancel(BackwardFirst);
            addSegment(0.8, mecanumDefaultWayPoint, -29.5, 119, 89.0);
            addSegment(0.5, mecanumDefaultWayPoint, -42.0, 107, 110.0);
        }, () -> {

        });
        // Pre-loaded cone place
        customFlipped(() -> {
            addTimedSetpoint(1.0, 0.5, 0.9, -67.0, 107, 110.0);
        }, () -> {
        });
        addConcurrentAutoModuleWithCancel(Forward(0), 0.1);
//        // Start 5 cycle
        customNumber(5, i -> {
            customFlipped(() -> {
                switch (i){
                    case 0: x = 0.0; s = 0.0;  break;
                    case 1: x = 1.0; s = 1.5;  break;
                    case 2: x = 2.0; s = 3.0;  break;
                    case 3: x = 3.0; s = 4.5;  break;
                    case 4: x = 4.0; s = 6.0;   break;
                }
            }, () -> {
                switch (i){
//                    case 0: x = 2.0; s = -0.2;  break;
//                    case 1: x = 3.0;  s = -0.4;  break;
//                    case 2: x = 3.0;  s = -0.8;  break;
//                    case 3: x = 4.0;  s = -1.2;  break;
//                    case 4: x = 4.0;  s = -1.6;   break;
                }
            });
            // Move to pick

            addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 119 + s, 110);
            addSegment(1.0, mecanumDefaultWayPoint, 25 - x, 123 + s, 91);
            // Pick
            customFlipped(() -> {
                addSegment(0.3, mecanumDefaultWayPoint, 68 - x, 125 + s, 93);
            }, () -> {

            });
            addTimedWaypoint(0.1, 0.1, 74 - x, 125 + s, 93);
            addCustomCode(() -> {
                bot.cancelAutoModules();
                bot.addAutoModule(GrabBack);
                pause(0.4);
            });
            // Move to place
            addSegment(1.0, mecanumDefaultWayPoint, 0 - x, 121 + s, 88);
            addConcurrentAutoModuleWithCancel(Backward);
            addSegment(0.5, mecanumDefaultWayPoint, -43 - x, 121 + s, 88);
            // Place
            customFlipped(() -> {
                addTimedSetpoint(1.0, 0.5, 1.1, -69.0 - x, 107 + s, 110.0);
            }, () -> {

            });
            addConcurrentAutoModuleWithCancel(Forward(i + 1), 0.1);
        });
//        // Park
        customCase(() -> {
            addTimedWaypoint(0.7,0.4,  -58.5, 126.0, 129.0);
            addTimedWaypoint(0.7, 0.7, -61.5, 133.0, 0.0);
            addTimedSetpoint(1.0, 0.5, 1.2, -65, 74, 0);
        }, () -> {
            addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 123 + s, 110);
            addSegment(0.6, mecanumDefaultWayPoint, -11.0, 114.5, 35.0);
            addTimedSetpoint(1.0, 0.5, 1.2, -4.0, 74, 0);
        }, () -> {
            addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 123 + s, 110);
            addSegment(1.0, mecanumDefaultWayPoint, 15 - x, 123 + s, 91);
            addSegment(0.6, mecanumDefaultWayPoint, 45.0, 114.5, 35.0);
            addTimedSetpoint(1.0, 0.5, 1.3, 55, 74, 0);
        });
        addPause(0.1);
        // End
    }


    @Override
    public void postProcess() {
        autoPlane.reflectY();
        autoPlane.reflectX();
    }

//    @Autonomous(name = "C. RIGHT SAFE", group = "auto", preselectTeleOp = "TerraOp")
//    public static class RIGHT extends TerraAutoSafe {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}
//
//    @Autonomous(name = "D. LEFT SAFE", group = "auto", preselectTeleOp = "TerraOp")
//    public static class LEFT extends TerraAutoSafe {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}



    @Autonomous(name = "C. RIGHT SAFE", group = "auto")
    public static class RIGHT extends TerraAutoSafe {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}

    @Autonomous(name = "D. LEFT SAFE", group = "auto")
    public static class LEFT extends TerraAutoSafe {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}


}
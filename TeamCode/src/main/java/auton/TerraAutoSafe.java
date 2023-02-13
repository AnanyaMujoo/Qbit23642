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

    AutoModule BackwardFirst = new AutoModule(
            lift.changeCutoff(1.0),
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH) + 3.5).attach(outtake.stageReadyEndAfter(0.3))
    );

    AutoModule Backward = new AutoModule(
            RobotPart.pause(0.05),
            outtake.stageFlip(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH) + 4.8).attach(outtake.stageReadyEndAfter(0.25))
    );

    AutoModule Forward(int i) {
        return new AutoModule(
                outtake.stageEnd(0.15),
                outtake.stageOpen(0.0),
                lift.moveTime(-0.7, 0.15),
                lift.stageLift(1.0, i == 0 ? 16.4 : Math.max(16.4 - (i * 16.4 / 5.0), 0)).attach(outtake.stageStartAfter(0.1))
        );
    }


    AutoModule GrabBack = new AutoModule(
            outtake.stageClose(0.2),
            lift.moveTimeBack(-0.7, 0.5, () -> 0.3).attach(outtake.stageReadyStart(0.0))
    );

    @Override
    public void preProcess() {
        caseDetected = Case.THIRD;
    }

    @Override
    public void define() {

        // Pre-loaded cone move
        customFlipped(() -> {
            addSegment(1.0, mecanumDefaultWayPoint, 2.0, 87.0, 0.0);
            addSegment(0.8, mecanumDefaultWayPoint, -6.0, 114.5, 45.0);
            addSegment(0.7, mecanumDefaultWayPoint, -29.5 - x, 126 + s, 89.0);
        }, () -> {

        });
        // Pre-loaded cone place
        addConcurrentAutoModuleWithCancel(BackwardFirst);
        customFlipped(() -> {
            addTimedSetpoint(1.0, 0.6, 0.9, -66.5, 119.0, 129.0 );
        }, () -> {
        });
        addConcurrentAutoModuleWithCancel(Forward(0), 0.2);
//
//        // Start 5 cycle
//        customNumber(5, i -> {
//            customFlipped(() -> {
//                switch (i){
//                    case 0: x = -0.5; s = -1.2;  break;
//                    case 1: x = 0.6;  s = -0.5;  break;
//                    case 2: x = 0.6;  s = 0.0;  break;
//                    case 3: x = 1.0;  s = 0.5;  break;
//                    case 4: x = 1.0;  s = 1.0;   break;
//                }
//            }, () -> {
//                switch (i){
//                    case 0: x = 2.0; s = -0.2;  break;
//                    case 1: x = 3.0;  s = -0.4;  break;
//                    case 2: x = 3.0;  s = -0.8;  break;
//                    case 3: x = 4.0;  s = -1.2;  break;
//                    case 4: x = 4.0;  s = -1.6;   break;
//                }
//            });
//            // Move to pick
//            addSegment(0.7, mecanumDefaultWayPoint, -29.5 - x, 126 + s, 92);
//            addSegment(0.7, mecanumDefaultWayPoint, 18-x, 128 + s, 80);
//            // Pick
//            customFlipped(() -> {
//                addSegment(0.6, mecanumDefaultWayPoint, 60 - x, 125 + s, 87);
//            }, () -> {
//                addSegment(0.6, mecanumDefaultWayPoint, 61 - x, 125 + s, 87);
//            });
//            addTimedWaypoint(0.1, 0.2, 68 - x, 126 + s, 87);
//            addCustomCode(() -> {
//                bot.cancelAutoModules();
//                bot.addAutoModule(GrabBack);
//                pause(0.5);
//            });
//            // Move to place
//            addSegment(0.65, mecanumDefaultWayPoint, 30.5 - x, 128.8 + s, 86);
//            addConcurrentAutoModuleWithCancel(Backward);
//            addSegment(0.7, mecanumDefaultWayPoint, -29.5 - x, 126 + s, 89.0);
//            // Place
//            customFlipped(() -> {
//                addTimedSetpoint(1.0, 0.6, 0.9, -66.5 - x, 119.0 + s, 129.0 );
//            }, () -> {
//
//            });
//            addConcurrentAutoModuleWithCancel(Forward(i + 1));
//            addPause(0.15);
//        });
////        // Park
//        customCase(() -> {
//            addTimedWaypoint(0.8,0.4,  -58.5, 126.0, 129.0);
//            addTimedWaypoint(0.8, 0.7, -61.5, 133.0, 0.0);
//            addTimedSetpoint(1.0, 0.8, 1.2, -58, 78, 0);
//        }, () -> {
//            addSegment(0.8, mecanumDefaultWayPoint, -28.5, 129.5, 90.0);
//            addSegment(0.8, mecanumDefaultWayPoint, -6.0, 114.5, 45.0);
//            addTimedSetpoint(1.0, 0.8, 1.2, 3.0, 78, 0);
//        }, () -> {
//            addSegment(0.8, mecanumDefaultWayPoint, 13.5, 129.5, 90.0);
//            addSegment(0.8, mecanumDefaultWayPoint, 51.0, 114.5, 45.0);
//            addTimedSetpoint(1.0, 0.8, 1.2, 65, 78, 0);
//        });
//        addPause(0.1);
//        // End
    }


    @Override
    public void postProcess() {
        autoPlane.reflectY();
        autoPlane.reflectX();
    }

    @Autonomous(name = "C. RIGHT SAFE", group = "auto", preselectTeleOp = "TerraOp")
    public static class RIGHT extends TerraAutoSafe {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}

    @Autonomous(name = "D. LEFT SAFE", group = "auto", preselectTeleOp = "TerraOp")
    public static class LEFT extends TerraAutoSafe {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}


}
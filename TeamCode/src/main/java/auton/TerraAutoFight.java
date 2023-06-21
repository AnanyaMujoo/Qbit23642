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
import static global.Modes.Height.HIGH;

// , preselectTeleOp = "TerraOp"
public class TerraAutoFight extends AutoFramework {

    @Override
    public void initialize() {
        TerraAutoRam.normalInit(this);
        caseDetected = Case.FIRST;
    }

//    AutoModule BackwardFirst = new AutoModule(
//            lift.stageLift(1.0, heightMode.getValue(HIGH)+1).attach(outtake.stageReadyEndContinuous(0.6))
//    ).setStartCode(outtake::moveMiddle);

//


    AutoModule ForwardFirst = new AutoModule(
            outtake.stageEnd(0.4),
            outtake.stageOpen(0.1),
            lift.stageLift(1.0,  11.5).attach(outtake.stageStartAfter(0.2))
    ).setStartCode(outtake::moveEnd);

    AutoModule GrabBack = new AutoModule(
            outtake.stageClose(0.05),
            drive.moveTime(-0.1, 0, 0, 0.15),
            lift.moveTimeBack(-0.1, 1.0, () -> 0.2),
            outtake.stageMiddleWithoutFlip(0.2),
            outtake.stageFlip(0.0)
    );



    AutoModule Forward(int i){return new AutoModule(
            outtake.stageEnd(0.4),
            outtake.stageOpen(0.1),
            lift.stageLift(1.0,  Math.max(11.5 - (i*11.5/4.6), -0.5)).attach(outtake.stageStartAfter(0.2))
    );}

//
//
//    AutoModule GrabBackLast = new AutoModule(
//            drive.moveTime(-0.2, 0, 0,0.2),
//            lift.moveTimeBackOverride(-0.15, 1.0, 0.3)
//    );

    @Override
    public void define() {

        TerraAutoRam.signal(this);

        customSide(() -> {
            addConcurrentAutoModuleWithCancel(new AutoModule(outtake.stageFlip(0.0), outtake.stageMiddle(0.0)));
            addTimedSetpoint(1.0, 0.5, 0.4, 7, 126, -30);
            addConcurrentAutoModuleWithCancel(new AutoModule(lift.moveTime(1, 0.3)));
            addTimedSetpoint(1.0, 0.4, 0.4, -8, 134, 35);
            addConcurrentAutoModuleWithCancel(TerraAutoRam.BackwardFirst);
            addTimedSetpoint(1.0, 0.4, 0.8, -13, 140, 40);
        }, () -> {

        });
        addConcurrentAutoModuleWithCancel(ForwardFirst, 0.5);
        customNumber(5, i -> {

            addSegment(0.7, mecanumDefaultWayPoint, 18, 129, 90);
            customFlipped(() -> {
                addSegment(0.6, mecanumDefaultWayPoint, 35, 127.5, 90);
                addSegment(0.4, noStopNewNoHeadingSetPoint, 66, 127.5, 90);
            }, () -> {

            });
            addCustomCode(() -> {
                bot.addAutoModuleWithCancel(GrabBack);
                pause(0.6);
            });
            addSegment(0.6, mecanumDefaultWayPoint, 20, 129, 85);
            addTimedSetpoint(1.0, 0.4, 0.4, -8, 132, 55);
            addConcurrentAutoModuleWithCancel(TerraAutoRam.BackwardFirst);
            addTimedSetpoint(1.0, 0.4, 0.8, -13, 140, 40);
            addConcurrentAutoModuleWithCancel(Forward(i+1), 0.5);
        });

        addSegment(0.5, mecanumDefaultWayPoint, 18, 130, 90);
        addConcurrentAutoModuleWithCancel(new AutoModule());
        customCase(() -> {
            addConcurrentAutoModule(new AutoModule(outtake.stage(0.55, 0.0)));
            addSegment(0.5, noStopNewSetPoint, -58, 130, 90);
        }, () -> {
            addConcurrentAutoModule(new AutoModule(outtake.stage(0.55, 0.0)));
            addSegment(0.5, noStopNewSetPoint, 0, 130, 90);
        }, () -> {addConcurrentAutoModule(new AutoModule(outtake.stage(0.1, 0.0)));
            addSegment(0.5, noStopNewSetPoint, 58, 130, 90);
        });




//        addSetpoint(0.5, 5, 125, 40);



//        // Pre-loaded cone move
//        addConcurrentAutoModuleWithCancel(new AutoModule(outtake.stageMiddle(0.0), lift.stageLift(1.0, heightMode.getValue(LOW))));
//        customFlipped(() -> {
//            addSegment(1.0, mecanumDefaultWayPoint, 2, 110, 0);
//            addSegment(0.6, mecanumDefaultWayPoint, 3, 126, 0);
//            addSegment(0.36, mecanumDefaultWayPoint, 3, 130, 0);
//            addSegment(0.65, mecanumDefaultWayPoint, 4, 123, 0);
//        }, () -> {
//            addSegment(1.0, mecanumDefaultWayPoint, 0, 110, 0);
//            addSegment(0.6, mecanumDefaultWayPoint, -1, 126, 0);
//            addSegment(0.36, mecanumDefaultWayPoint, -1, 130, 0);
//            addSegment(0.65, mecanumDefaultWayPoint, -2, 120, 0);
//        });
//
//
//        addConcurrentAutoModuleWithCancel(BackwardFirst);
//
//        // Pre-loaded cone place
//        customFlipped(() -> {
//            addTimedSetpoint(1.0, 0.6, 0.5, 5, 125, 40);
//            addTimedSetpoint(1.0, 0.6, 0.9, -9.5, 137, 45);
//        }, () -> {
//            addTimedSetpoint(1.0, 0.6, 0.5, 5, 123, 40);
//            addTimedSetpoint(1.0, 0.6, 0.9, -10.0, 140, 45);
//        });
//        addConcurrentAutoModuleWithCancel(ForwardFirst, 0.2);
//
//        // Start 5 cycle
//        customNumber(5, i -> {
//            customFlipped(() -> {
//                x = 0.0;
//                s = -1.5-0.0*i;
//            }, () -> {
//                x = 2.0-0.2*i;
//                s = -0.3*i;
//            });
//            // Move to pick
//            addSegment(0.7, mecanumDefaultWayPoint, 18-x, 130 + s, 80);
//            // Pick
//            customFlipped(() -> {
//                addSegment(0.55, mecanumDefaultWayPoint, 46-x, 129 + s, 88);
//                addSegment(0.4, mecanumDefaultWayPoint, 64-x, 128 + s, 88);
//                addTimedSetpoint(1.0, 0.1, 0.3, 68-x, 128 + s , 88);
//            }, () -> {
//                addSegment(0.55, mecanumDefaultWayPoint, 47.5-x, 127 + s, 88);
//                addSegment(0.4, mecanumDefaultWayPoint, 65.5-x, 127 + s, 88);
//                addTimedSetpoint(1.0, 0.1, 0.3, 73.5-x, 127 + s, 88);
//            });
//            addCustomCode(() -> {
//                outtake.checkAccess(User.AUTO);
//                outtake.closeClaw();
//                bot.addAutoModuleWithCancel(i+1 != 5 ? GrabBack : GrabBackLast);
//                pause(0.5);
//                outtake.flip();
//                outtake.readyStart();
//            });
//            // Move to place
//            addConcurrentAutoModuleWithCancel(Backward);
//            // Place
//            customFlipped(() -> {
//                addSegment(0.55, mecanumDefaultWayPoint, 30-x, 124 + s, 80);
//                addSegment(0.5, mecanumDefaultWayPoint, 11-x, 132 + s, 50);
//                addTimedSetpoint(1.0, 0.5, 0.8, -10.5 - x, 140 + s, 51.5);
//            }, () -> {
//                addSegment(0.55, mecanumDefaultWayPoint, 30-x, 124 + s, 80);
//                addSegment(0.5, mecanumDefaultWayPoint, 11-x, 134 + s, 55);
//                addTimedSetpoint(1.0, 0.5, 0.8, -6 - x, 140.5 + s, 52);
//            });
//            addCustomCode(() -> {
//                drive.move(0,0,0);
//                outtake.moveEnd();
//                pause(0.05);
//                outtake.moveEnd();
//                bot.cancelAutoModules();
//                outtake.openClaw();
//                pause(0.05);
//                outtake.openClaw();
//            });
//            addConcurrentAutoModuleWithCancel(Forward(i+1), 0.1);
//        });
//        pause(0.1);
//        addTimedWaypoint(0.6, 0.5, 2.4, 125, 53);
////        // Park
//        customCase(() -> {
//            addSegment(0.7, mecanumDefaultWayPoint, -7, 128, 90);
//            addSegment(0.7, mecanumDefaultWayPoint, -10, 128, 90);
//            addSegment(0.7, mecanumDefaultWayPoint, -45, 125, 55);
//            addSegment(0.7, mecanumDefaultWayPoint, -54, 126, 5);
//            addTimedSetpoint(1.0, 0.7, 1.2, -58, 83, 0);
//        }, () -> {
//            addTimedWaypoint(0.7, 0.5, 3.0, 122, 0);
//            customFlipped(() -> {
//                addTimedSetpoint(1.0, 0.7, 1.2, 1.0, 83, 0);
//            }, () -> {
//                addTimedSetpoint(1.0, 0.7, 1.2, -3.5, 83, 0);
//            });
//        }, () -> {
//            addSegment(0.7, mecanumDefaultWayPoint, 7, 130, 90);
//            addSegment(0.7, mecanumDefaultWayPoint, 39, 130, 90);
//            addSegment(0.7, mecanumDefaultWayPoint, 51, 114, 32);
//            addSegment(0.7, mecanumDefaultWayPoint,  56, 98, 0);
//            addTimedSetpoint(1.0, 0.7, 1.2, 65, 83, 0);
//        });
//        addPause(0.1);
        // End
    }


    @Override
    public void postProcess() { autoPlane.reflectY(); autoPlane.reflectX(); }




//    @Autonomous(name = "A. RIGHT FIGHT", group = "auto", preselectTeleOp = "TerraOp")

    @Autonomous(name = "A. RIGHT FIGHT", group = "auto")
    public static class RIGHT extends TerraAutoFight {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}

    @Autonomous(name = "B. LEFT FIGHT", group = "auto", preselectTeleOp = "TerraOp")
    public static class LEFT extends TerraAutoFight {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}



}

package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import autoutil.AutoFramework;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.ExceptionCatcher;
import util.Timer;
import util.User;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;
import static global.Modes.OuttakeStatus.DRIVING;

// , preselectTeleOp = "TerraOp"
@Autonomous(name = "TerraAutoNormalRight", group = "auto")
public class TerraAutoNormalRight extends AutoFramework {

    @Override
    public void initialize() {
        TerraAutoRam.normalInit(this);
    }

    AutoModule BackwardFirst = new AutoModule(
            lift.stageLift(1.0, heightMode.getValue(HIGH)+1).attach(outtake.stageReadyEndContinuous(0.6))
    ).setStartCode(outtake::moveMiddle);

//
//    AutoModule Backward = new AutoModule(
//            lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5).attach(outtake.stageReadyEndContinuous(0.9))
//    ).setStartCode(outtake::flip);
//
    AutoModule ForwardFirst = new AutoModule(
            outtake.stageEnd(0.15),
            outtake.stageOpen(0.0),
            lift.moveTime(-1.0, 0.15),
            lift.stageLift(1.0,  13).attach(outtake.stageStartAfter(0.2))
    );

    AutoModule ForwardTeleHigh = new AutoModule(
        RobotPart.pause(0.1),
        outtake.stageOpen(0.1),
        outtake.stageStart(0.0),
        lift.stageLift(1.0,0)
    ).setStartCode(outtake::moveEnd);
//
//    AutoModule Forward(int i){return new AutoModule(
//            lift.moveTime(-0.8, 0.2),
//            lift.stageLift(1.0,  Math.max(13 - (i*13/4.6), -0.5)).attach(outtake.stageBack(i == 5 ? 0.3 : 0.2))
//    );}
//
//    AutoModule GrabBack = new AutoModule(
//            drive.moveTime(-0.02, 0, 0,0.1),
//            RobotPart.pause(0.1),
//            lift.moveTimeBackOverride(-0.1, 1.0, 0.4)
//    ).setStartCode(outtake::closeClaw);
//
//
//    AutoModule GrabBackLast = new AutoModule(
//            drive.moveTime(-0.2, 0, 0,0.2),
//            lift.moveTimeBackOverride(-0.15, 1.0, 0.3)
//    );

    @Override
    public void define() {


        addSegment(0.8, mecanumDefaultWayPoint, 0, 30, 0);

        addSegment(0.5, mecanumDefaultWayPoint, 2,80,-30);

        addSegment(0.9, mecanumDefaultWayPoint, 25, 118, -35);




        addSegment(0.5, mecanumDefaultWayPoint, 10, 110, -35);
        addConcurrentAutoModuleWithCancel(BackwardFirst);
        addSegment(0.5, 0.4, mecanumNonstopSetPoint, 8, 126, 40);





        addSegment(0.8, 0.5, mecanumNonstopSetPoint, -5, 138, 40);

        addConcurrentAutoModuleWithCancel(ForwardFirst, 0.2);

        addSegment(0.5, 0.5, mecanumNonstopSetPoint, 5, 125, 40);



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


}

//package auton.old;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//
//import autoutil.AutoFramework;
//import elements.Case;
//import elements.Field;
//import elements.FieldPlacement;
//import elements.FieldSide;
//import elements.GameItems;
//import geometry.position.Pose;
//
//import static global.General.fieldPlacement;
//import static global.General.fieldSide;
//
//public class TerraAutoMid extends AutoFramework {
//
//    double y = 0;
//
//    // TODO FIX AUTOS
//
//    @Override
//    public void initialize() {
//        TerraAutoRam.normalInit(this);
//        caseDetected = Case.SECOND;
//        y = 0;
//    }
//
////    AutoModule Backward = new AutoModule(
////            lift.stageLift(1.0, heightMode.getValue(MIDDLE)-2).attach(outtake.stageReadyEndContinuous(0.6))
////    ).setStartCode(outtake::moveMiddle);
////
////
////
////    AutoModule ForwardFirst = new AutoModule(
////            outtake.stageEnd(0.1),
////            outtake.stageOpen(0.2),
////            lift.stageLift(1.0,  11.5).attach(outtake.stageStartAndSignal2())
////    ).setStartCode(outtake::moveFlipEnd);
////
////
////
////
////    AutoModule GrabBack = new AutoModule(
////            outtake.stageClose(0.1),
////            drive.moveTime(-0.1, 0, 0, 0.1),
////            lift.moveTime(1.0, 0.2),
////            outtake.stageMiddleWithoutFlip(0.2),
////            outtake.stageFlip(0.0)
////    );
////
////    AutoModule Forward(int i){return new AutoModule(
////            outtake.stageEnd(0.05),
////            outtake.stageOpen(0.2),
////            outtake.stageUnFlip(0.0),
////            lift.stageLift(1.0,  Math.max(11.5 - (i*11.5/4.6), -0.5)).attach(outtake.stageStartAndSignal2())
////    );}
//
//
//    @Override
//    public void define() {
//
////        addSegment(0.7, mecanumDefaultWayPoint, 0, 40, 0);
////        addSegment(0.6, mecanumDefaultWayPoint, 0, 87, 17);
////        addSegment(0.55, mecanumDefaultWayPoint, -4, 105, 40);
////        addSegment(0.5, mecanumDefaultWayPoint, -12, 118, 66);
////
////        addTimedSetpoint(1.0, 0.5, 0.6, 4, 115, 113);
////        addConcurrentAutoModuleWithCancel(Backward);
////        addTimedSetpoint(1.0, 0.5, 0.8, -6, 110, 113);
////        addConcurrentAutoModuleWithCancel(ForwardFirst, 0.3);
////
////        customNumber(5, i -> {
////            customFlipped(() -> {
////                y = 0.5;
////                y += -0.2;
////
////            }, () -> {
////                y = 0.0;
////                y += -0.2;
////            });
////
////            addSegment(0.5, mecanumDefaultWayPoint, 18, 127+y, 108);
////            customFlipped(() -> {
////                addSegment(0.5, mecanumDefaultWayPoint, 49, 128+y, 90);
////                addSegment(0.4, slowDownStopSetPoint, 69, 128+y, 90);
////                addTimedSetpoint(1.0, 0.1,0.1, 72, 128+y, 91);
////            }, () -> {
////                addSegment(0.5, mecanumDefaultWayPoint, 51, 128+y, 90);
////                addSegment(0.4, slowDownStopSetPoint, 72, 128+y, 90);
////                addTimedSetpoint(1.0, 0.1,0.1, 74, 128+y, 89);
////            });
////            boolean[] exit = {false};
////            addCustomCode(() -> {
////                customFlipped(() -> {
////                    double end = -63.5+(0.2*i);
////                    exit[0] = Precision.range(odometry.getX(), end, end+10);
////                }, () -> {
////                    double end = 63.5-(0.2*i);
////                    exit[0] = Precision.range(odometry.getX(), end-10, end);
////                });
////                if (exit[0]) {
////                    double startX = odometry.getX();
////                    whileTime(() -> {
////                        drive.move(0.15, 0, 0);
////                    }, 0.2);
////                    double deltaX = odometry.getX() - startX;
////                    exit[0] = Math.abs(deltaX) < 3;
////                }
////                if(exit[0]) {
////                    customFlipped(() -> {
////                        double end = -63.5 + (0.2 * i);
////                        exit[0] = Precision.range(odometry.getX(), end, end + 10);
////                    }, () -> {
////                        double end = 63.5 - (0.2 * i);
////                        exit[0] = Precision.range(odometry.getX(), end - 10, end);
////                    });
////                }
////            });
////            addBreakpoint(() -> exit[0]);
////            addCustomCode(() -> {
////                bot.addAutoModuleWithCancel(GrabBack);
////                pause(0.45);
////            });
////            addSegment(0.4, mecanumDefaultWayPoint, 20, 127+y, 95);
////            addConcurrentAutoModuleWithCancel(Backward);
////            addTimedSetpoint(1.0, 0.15, 1.1, -8, 109, 113);
////            addConcurrentAutoModuleWithCancel(Forward(i+1), 0.3);
////        });
////        addTimedSetpoint(1.0, 0.5, 1.0, 4, 115, 113);
////        addBreakpointReturn();
////        addCustomCode(outtake::openClaw);
////        addConcurrentAutoModule(new AutoModule(outtake.stage(0.2, 0.1), outtake.stageOpenComp(0.0),  lift.stageLift(1.0,  -0.5)));
////        customCase(() -> {
////            addTimedSetpoint(1.0, 0.7, 0.7, -58, 127, 90);
////            addTimedSetpoint(1.0, 0.2, 1.0, -59, 127, 0);
////        }, () -> {
////            addTimedSetpoint(1.0, 0.7, 0.7, 0, 127, 90);
////            addTimedSetpoint(1.0, 0.2, 1.0, 0, 126, 0);
////        }, () -> {
////            addTimedSetpoint(1.0, 0.7, 0.7, 58, 127, 90);
////        });
////        addPause(0.5);
//
//
//    }
//
//
//    @Override
//    public void postProcess() { autoPlane.reflectY(); autoPlane.reflectX(); }
//
//
//
//    @Autonomous(name = "E. RIGHT MID", group = "auto", preselectTeleOp = "TerraOp")
//    public static class RIGHT extends TerraAutoMid {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; startPose = new Pose(20.5, Field.width/2.0 - Field.tileWidth - GameItems.Cone.height - 16,90); }}
//
//    @Autonomous(name = "F. LEFT MID ", group = "auto", preselectTeleOp = "TerraOp")
//    public static class LEFT extends TerraAutoMid {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; startPose = new Pose(20.5, Field.width/2.0 + Field.tileWidth + GameItems.Cone.height + 16,90); }}
//
//
//
//
//}

package auton.unused;

import automodules.AutoModule;
import autoutil.AutoFramework;
import elements.Field;
import geometry.framework.Point;
import robotparts.RobotPart;

import static global.General.bot;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;
import static global.Modes.Height.MIDDLE;

public class TerraAutoSafe extends AutoFramework {

    private double x, s;

    @Override
    public void initialize() {
        TerraAutoNormal.normalInit(this);
        x = 0;
        s = 0;
    }
//
//    AutoModule BackwardFirst = new AutoModule(
//            lift.stageLift(1.0, heightMode.getValue(MIDDLE) + 2.0).attach(outtake.stageReadyEndContinuous(0.6))
//    );
//
//    AutoModule Backward = new AutoModule(
//            lift.stageLift(0.8, heightMode.getValue(HIGH) + 2.5).attach(outtake.stageReadyEndContinuous(1.1))
//    ).setStartCode(outtake::flip);
//
//
//
//
//
//    AutoModule ForwardFirst = new AutoModule(
//        RobotPart.pause(0.1),
//        lift.stageLift(1.0,  13.5).attach(outtake.stageStartAfter(0.2))
//    ).setStartCode(() -> {
//        outtake.moveFlipEnd();
//        outtake.openClaw();
//    });
//
//    AutoModule Forward(int i){return new AutoModule(
//        RobotPart.pause(0.1),
//        lift.stageLift(1.0,  Math.max(13 - (i*13/4.6), -0.5)).attach(outtake.stageBack(0.5))
//    ).setStartCode(() -> {
//        outtake.openClaw();
//        outtake.moveFlipEnd();
//    });}
//
//    AutoModule GrabBack = new AutoModule(
//            RobotPart.pause(0.2),
//            lift.moveTimeBackOverride(-0.15, 1.0, 0.3)
//    ).setStartCode(outtake::closeClaw);
//
//    AutoModule GrabBackLast = new AutoModule(
//            drive.moveTime(-0.2, 0, 0,0.2),
//            lift.moveTimeBackOverride(-0.15, 1.0, 0.3)
//    ).setStartCode(outtake::closeClaw);

    @Override
    public void define() {

        // Pre-loaded cone move
//        addConcurrentAutoModuleWithCancel(new AutoModule(outtake.stageMiddle(0.0), lift.stageLift(1.0, heightMode.getValue(LOW))));
//        customFlipped(() -> {
//            addSegment(1.0, mecanumDefaultWayPoint, 2, 100, 0);
//            addSegment(0.6, mecanumDefaultWayPoint, 3, 109, 0);
//            addSegment(0.36, mecanumDefaultWayPoint, 3, 114, 0);
//        }, () -> {
//            addSegment(1.0, mecanumDefaultWayPoint, 0, 100, 0);
//            addSegment(0.6, mecanumDefaultWayPoint, -1, 109, 0);
//            addSegment(0.36, mecanumDefaultWayPoint, -1, 114, 0);
//        });
//        addConcurrentAutoModuleWithCancel(BackwardFirst);
//        // Pre-loaded cone place
//        customFlipped(() -> {
//            addTimedSetpoint(1.0, 0.7, 0.3, 5, 118, 120);
//            addTimedSetpoint(1.0, 0.3, 1.0, -5, 108, 120);
//        }, () -> {
//            addTimedSetpoint(1.0, 0.7, 0.3, 5, 118, 120);
//            addTimedSetpoint(1.0, 0.3, 1.0, -5, 107.5, 120);
//        });
//        addConcurrentAutoModuleWithCancel(ForwardFirst, 0.1);
//
//        addSegment(0.7, mecanumDefaultWayPoint, 18, 128, 80);
//        // Pick
//        customFlipped(() -> {
//            addSegment(0.6, mecanumDefaultWayPoint, 60-x, 127 + s, 87);
//            addTimedSetpoint(1.0, 0.1, 0.2, 67, 127 , 87);
//        }, () -> {
//            addSegment(0.6, mecanumDefaultWayPoint, 61-x, 125 + s, 87);
//            addTimedSetpoint(1.0, 0.1, 0.3, 72-x, 127 + s, 88);
//        });
//        addCustomCode(() -> {
//            Point point = new Point(odometry.getX(), isFlipped() ? Field.width - 27 : 27);
////            odometry.setPointUsingOffset(point);
//            outtake.closeClaw();
//            bot.addAutoModuleWithCancel(GrabBack);
//            pause(0.5);
//            outtake.flip();
//        });
//        addConcurrentAutoModuleWithCancel(Backward);
//        addSegment(1.0, mecanumDefaultWayPoint, 10 - x, 122 + s, 88);
//        addSegment(0.7, mecanumDefaultWayPoint, -20 - x, 122 + s, 88);
//        addSegment(0.5, mecanumDefaultWayPoint, -43 - x, 122 + s, 95);
//        customFlipped(() -> {
//            addTimedSetpoint(1.0, 0.3, 1.0, -67.0 - x, 108 + s, 113.0);
//        }, () -> {
//            addTimedSetpoint(1.0, 0.3, 1.0, -67.0 - x, 108 + s, 113.0);
//        });
//        addConcurrentAutoModuleWithCancel(Forward(1), 0.1);
//
//        // Start 4 cycle
//        customNumber(4, i -> {
//            customFlipped(() -> {
//                x = 0.0;
//                s = 1.0+0.4*i;
//            }, () -> {
//                x = 0.0;
//                s = 0.6*i;
//            });
//            // Move to pick
//            customFlipped(() -> {
//                addSegment(0.6, mecanumDefaultWayPoint, i == 0 ? -50 : -45 - x, 125 + s, 110);
//                addSegment(1.0, mecanumDefaultWayPoint, 20 - x, 124 + s, 90);
//                addSegment(0.55, mecanumDefaultWayPoint, 46-x, 127 + s, 88);
//                addSegment(0.4, mecanumDefaultWayPoint, 64-x, 127 + s, 88);
//                addTimedSetpoint(1.0, 0.1, 0.3, 68-x, 127 + s , 88);
//            }, () -> {
//                addSegment(0.6, mecanumDefaultWayPoint, i == 0 ? -52 : -42 - x, i == 0 ? 127 : 122 + s, 110);
//                addSegment(1.0, mecanumDefaultWayPoint, 20 - x, 125 + s, 90);
//                addSegment(0.55, mecanumDefaultWayPoint, 46-x, 126.5 + s, 88);
//                addSegment(0.4, mecanumDefaultWayPoint, 64-x, 126.5 + s, 88);
//                addTimedSetpoint(1.0, 0.1, 0.25, 72-x, 126.5 + s, 88);
//            });
//            // Pick
//            addCustomCode(() -> {
//                drive.move(0,0,0);
//                bot.cancelAutoModules();
//                outtake.closeClaw();
//                pause(0.05);
//                bot.addAutoModuleWithCancel(i+1 != 5 ? GrabBack : GrabBackLast);
//                Point point = new Point(odometry.getX(), isFlipped() ? Field.width - 27 : 27);
////                odometry.setPointUsingOffset(point);
//                pause(0.45);
//                outtake.flip();
//            });
//            customFlipped(() -> {
//                addConcurrentAutoModuleWithCancel(Backward);
//                addSegment(1.0, mecanumDefaultWayPoint, 10 - x, 123 + s, 88);
//                addSegment(0.7, mecanumDefaultWayPoint, -20 - x, 123 + s, 88);
//                addSegment(0.52, mecanumDefaultWayPoint, -43 - x, 123 + s, 95);
//                addTimedSetpoint(1.0, 0.42, 1.0, -67.0 - x, 108 + (0.7*s), 113.0);
//            }, () -> {
//                addConcurrentAutoModuleWithCancel(Backward);
//                addSegment(1.0, mecanumDefaultWayPoint, 10 - x, 123 + s, 88);
//                addSegment(0.7, mecanumDefaultWayPoint, -20 - x, 123 + s, 88);
//                addSegment(0.52, mecanumDefaultWayPoint, -43 - x, 123 + s, 95);
//                addTimedSetpoint(1.0, 0.42, 1.0, -67.0 - x, 108 + (0.8*s), 113.0);
//            });
//            // Place
//            addCustomCode(() -> {
//                bot.cancelAutoModules();
//                outtake.moveFlipEnd();
//                pause(0.05);
//                outtake.openClaw();
//            });
//            addConcurrentAutoModule(Forward(i + 2));
//            addPause(0.15);
//        });
//        addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 126 + s, 114);
//        customCase(() -> {
//            addConcurrentAutoModule(new AutoModule(outtake.stage(0.55, 0.0)));
//            addTimedSetpoint(1.0, 0.4, 1.2, -58, 126+s, 90);
//        }, () -> {
//            addConcurrentAutoModule(new AutoModule(outtake.stage(0.55, 0.0)));
//            addTimedSetpoint(1.0, 0.6, 1.2, 0, 126+s, 90);
//        }, () -> {
//            addConcurrentAutoModule(new AutoModule(outtake.stage(0.1, 0.0)));
//            addTimedSetpoint(1.0, 0.7, 1.2, 60, 123+s, 90);
//        });
//        addAutoModule(new AutoModule(outtake.stage(0.55, 1.0)));


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







//
//    private double x, s;
//
//    @Override
//    public void initialize() {
//        TerraAutoNormal.normalInit(this);
//        x = 0;
//        s = 0;
//    }
//
//    AutoModule BackwardFirst = new AutoModule(
//            RobotPart.pause(0.3),
//            outtake.stageMiddle(0.0),
//            lift.stageLift(1.0, heightMode.getValue(HIGH) + 3.0).attach(outtake.stageWithFlipAfter(0.72, 0.3))
//    );
//
//    AutoModule Backward = new AutoModule(
//            outtake.stageFlip(0.0),
//            lift.stageLift(1.0, heightMode.getValue(HIGH) + 4.0).attach(outtake.stageWithFlipAfter(0.72, 0.25))
//    );
//
//    AutoModule BackwardLeft = new AutoModule(
//            RobotPart.pause(0.3),
//            outtake.stageFlip(0.0),
//            lift.stageLift(1.0, heightMode.getValue(HIGH) + 4.0).attach(outtake.stageWithFlipAfter(0.72, 0.25))
//    );
//
//    AutoModule Forward(int i) { return new AutoModule(
//            outtake.stageEnd(0.1),
//            outtake.stageOpen(0.0),
//            lift.moveTime(-0.7, 0.15),
//            lift.stageLift(1.0, i == 0 ? 15.0 : Math.max(15.0 - (i * 15.0 / 4.5), 0)).attach(outtake.stageStartAfter(0.1))
//    );}
//
//
//    static AutoModule CancelAfter(double t){ return new AutoModule(
//            RobotPart.pause(t),
//            Reactor.forceExit()
//    );}
//
//    AutoModule GrabBack = new AutoModule(
//            outtake.stageClose(0.22),
//            lift.moveTimeBack(-0.8, 0.4, () -> 0.15).attach(outtake.stageReadyStart(0.0))
//    );

//    @Override
//    public void define() {
//
//        // Pre-loaded cone move
//        customFlipped(() -> {
//            addSegment(1.0, mecanumDefaultWayPoint, -2.0, 105, 0.0);
//            addSegment(0.5, mecanumDefaultWayPoint, -6.0, 110, 80.0);
//            addConcurrentAutoModuleWithCancel(BackwardFirst);
//            addSegment(0.6, mecanumDefaultWayPoint, -29.5, 122, 92.0);
//            addSegment(0.5, mecanumDefaultWayPoint, -46.0, 122, 110.0);
//        }, () -> {
//            addSegment(1.0, mecanumDefaultWayPoint, -4.0, 105, 0.0);
//            addSegment(0.5, mecanumDefaultWayPoint, -8.0, 110, 110.0);
//            addConcurrentAutoModuleWithCancel(BackwardFirst);
//            addSegment(0.6, mecanumDefaultWayPoint, -31.5, 119, 130.0);
//            addSegment(0.5, mecanumDefaultWayPoint, -43.0, 117, 120.0);
//        });
//        // Pre-loaded cone place
//        customFlipped(() -> {
//            addTimedSetpoint(1.0, 0.4, 1.0, -68.0, 107, 112.0);
//        }, () -> {
//            addTimedSetpoint(1.0, 0.3, 1.0, -67.0, 102, 110.0);
//        });
//        addConcurrentAutoModuleWithCancel(Forward(0), 0.1);
//        // Start 5 cycle
//        customNumber(5, i -> {
//            customFlipped(() -> {
//                x = 2.5;
//                s = 2.0 + 1.5*i;
//            }, () -> {
//                x = 0.5;
//                s = 0.0;
//            });
//            // Move to pick
//            customFlipped(() -> {
//                addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 119 + s, 110);
//                addSegment(1.0, mecanumDefaultWayPoint, 20 - x, 123 + s, 91);
//                addSegment(0.6, mecanumDefaultWayPoint, 60 - x, 123 + s, 93);
//                addConcurrentAutoModuleWithCancel(CancelAfter(0.3));
//                addSegment(i == 0 ? 0.35: 0.3, mecanumDefaultWayPoint, 80 - x, 125 + s, 93);
//            }, () -> {
//                addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 119 + s, 110);
//                addSegment(1.0, mecanumDefaultWayPoint, 20 - x, 124 + s, 91);
//                addSegment(0.6, mecanumDefaultWayPoint, 61 - x, 124 + s, 93);
//                addConcurrentAutoModuleWithCancel(CancelAfter(0.3));
//                addSegment(i == 0 ? 0.35: 0.3, mecanumDefaultWayPoint, 80 - x, 125 + s, 93);
//            });
//            // Pick
//            addCustomCode(() -> {
//                Point point = new Point(odometry.getX(), isFlipped() ? Field.width - 27 : 27);
//                odometry.setPointUsingOffset(point);
//                bot.cancelAutoModules();
//                bot.addAutoModule(GrabBack);
//                pause(0.35);
//            });
//            customFlipped(() -> {
//                addSegment(1.0, mecanumDefaultWayPoint, 10 - x, 122 + s, 88);
//                addSegment(0.7, mecanumDefaultWayPoint, -20 - x, 122 + s, 88);
//                addConcurrentAutoModuleWithCancel(Backward);
//                addSegment(0.55, mecanumDefaultWayPoint, -43 - x, 122 + s, 88);
//                addTimedSetpoint(1.0, 0.3, 1.0, -69.0 - x, 106 + s, 110.0);
//            }, () -> {
//                addSegment(1.0, mecanumDefaultWayPoint, 10 - x, 124 + s, 90);
//                addConcurrentAutoModuleWithCancel(BackwardLeft);
//                if(i % 2 == 0) {
//                    addCustomCode(() -> {
//                        ArrayList<Double> xs = new ArrayList<>();
//                        drive.move(-0.45, 0.0, 0.0);
//                        whileActive(() -> odometry.getY() > Field.width - 128, () -> {
//                            distanceSensors.ready();
//                            double dis = distanceSensors.getRightDistance();
//                            if (dis < 20) {
//                                xs.add(dis + 124);
//                            }
//                        });
//                        double avgDis = Iterator.forAllAverage(xs);
//                        Point point = new Point(avgDis, odometry.getY());
//                        if(odometry.getPose().getPoint().getDistanceTo(point) < 15) {
//                            odometry.setPointUsingOffset(point);
//                        }
//                    });
//                }else{
//                    s = 1.5;
//                    addSegment(0.7, mecanumDefaultWayPoint, -18 - x, 121 + s, 92);
//                }
//                addSegment(0.5, mecanumDefaultWayPoint, -43 - x, 116 + s, 125);
//                customFlipped(() -> {
//                    addTimedSetpoint(1.0, 0.3, 1.0, -69.0 - x, 102 + s, 115.0);
//                }, () -> {
//                    addTimedSetpoint(1.0, 0.3, 1.0, -69.0 - x, 102 + s, 112.0);
//                });
//            });
//            // Place
//            addConcurrentAutoModuleWithCancel(Forward(i + 1), 0.1);
//        });
//
//
//
//        addSegment(0.6, mecanumDefaultWayPoint, -37 - x, 126 + s, 114);
//        addConcurrentAutoModule(new AutoModule(outtake.stage(0.55, 0.0)));
//        customCase(() -> {
//            addTimedSetpoint(1.0, 0.4, 1.2, -60, 126+s, 90);
//        }, () -> {
//            addTimedSetpoint(1.0, 0.6, 1.2, 0, 126+s, 90);
//        }, () -> {
//            addTimedSetpoint(1.0, 0.8, 1.2, 60, 126+s, 90);
//        });
//        addAutoModule(new AutoModule(outtake.stage(0.55, 1.0)));
//
//
//        // End
//    }
//
//
//    @Override
//    public void postProcess() {
//        autoPlane.reflectY();
//        autoPlane.reflectX();
//    }
}
package automodules;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Objects;

import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.reactors.MecanumJunctionReactor2;
import autoutil.reactors.Reactor;
import autoutil.vision.JunctionScannerAll;
import elements.Field;
import elements.Robot;
import geometry.framework.Point;
import geometry.position.Pose;
import global.Modes;
import math.polynomial.Linear;
import robot.RobotUser;
import robotparts.RobotPart;
import robotparts.electronics.output.OLed;
import teleop.TerraOp;
import teleutil.TeleTrack;
import teleutil.independent.Independent;
import teleutil.independent.Machine;
import util.codeseg.CodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.condition.OutputList;
import util.template.Iterator;
import util.template.Precision;

import static global.General.bot;
import static global.General.fault;
import static global.General.log;
import static global.Modes.*;
import static global.Modes.Drive.MEDIUM;
import static global.Modes.Drive.SLOW;
import static global.Modes.Height.GROUND;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;
import static global.Modes.Height.MIDDLE;
import static global.Modes.OuttakeStatus.DRIVING;
import static global.Modes.OuttakeStatus.PLACING;


public interface AutoModuleUser extends RobotUser{

//
//    CodeSeg stack = () -> {bot.addAutoModule(AutoModuleUser.ForwardStackTele(lift.stackedMode)); lift.stackedMode++;};
//    TeleTrack kappaBefore = new TeleTrack(
//            new TeleTrack.Step(heightMode.setTo(LOW)), // LOW
//            new TeleTrack.Step(heightMode.setTo(LOW)), // TERMINAL
//            new TeleTrack.Step(heightMode.setTo(LOW)), // LOW
//            new TeleTrack.Step(heightMode.setTo(LOW)).add(stack), // LOW
//            new TeleTrack.Step(heightMode.setTo(HIGH)).add(stack), // HIGH
//            new TeleTrack.Step(heightMode.setTo(GROUND)).add(stack), // GROUND
//            new TeleTrack.Step(heightMode.setTo(GROUND)), // TERMINAL
//            new TeleTrack.Step(heightMode.setTo(GROUND)), // GROUND
//            new TeleTrack.Step(heightMode.setTo(LOW)) // LOW + CAP
//    );
//
//    static void enableKappa(){ kappaBefore.enable(); }
//    static void disableKappa(){ kappaBefore.disable(); }

    /**
     * Tele
     */


    /**
     * Forward
     */


    AutoModule ForwardTeleHigh = new AutoModule(
            outtakeStatus.ChangeMode(DRIVING),
            outtake.stageEnd(0.0),
            outtake.stageOpen(0.0),
            lift.resetCutoff(),
            lift.stageLift(0.7,0).attach(outtake.stageStartAfter(0.15))
    );

    AutoModule ForwardTeleMiddle = new AutoModule(
            outtakeStatus.ChangeMode(DRIVING),
            outtake.stageEnd(0.0),
            outtake.stageOpen(0.0),
            lift.resetCutoff(),
            lift.stageLift(0.6,0).attach(outtake.stageStartAfter(0.15))
    );

    AutoModule ForwardTeleLow = new AutoModule(
            outtakeStatus.ChangeMode(DRIVING),
            outtake.stageEnd(0.0),
            outtake.stageOpen(0.0),
            drive.moveTime(1.0, 0.0, 0.0, 0.2),
            outtake.stageStart(0.0),
            lift.resetCutoff(),
            lift.stageLift(0.8,0)
    );

    AutoModule ForwardTeleGround = new AutoModule(
            outtakeStatus.ChangeMode(DRIVING),
            outtake.stageOpen(0.0),
            driveMode.ChangeMode(MEDIUM),
            RobotPart.pause(0.6),
            lift.resetCutoff(),
            outtake.stageStart(0.0),
            lift.moveTime(-0.4, 0.4)
    );

    /**
     * Backward
     */

    AutoModule BackwardPlaceHighTele = new AutoModule(
            lift.changeHigh(false),
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+2)
    );

    AutoModule BackwardGrabHighTele = new AutoModule(
            heightMode.ChangeMode(HIGH),
            outtakeStatus.ChangeMode(PLACING),
            lift.changeHigh(true),
            outtake.stageClose(0.22),
            lift.moveTimeBack(-0.2, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.3;}else{return 0.0;}}),
            outtake.stageWithFlip(0.55,0.0),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+2)
    );

//    OutputList BackwardHighTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabHighTele).addOption(PLACING, ForwardTeleHigh);


    AutoModule BackwardPlaceMiddleTele = new AutoModule(
            lift.changeMid(false),
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+2)
    );

    AutoModule BackwardGrabMiddleTele = new AutoModule(
            outtakeStatus.ChangeMode(PLACING),
            heightMode.ChangeMode(MIDDLE),
            lift.changeMid(true),
            outtake.stageClose(0.22),
            lift.moveTimeBack(-0.2, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.3;}else{return 0.0;}}),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+2).attach(outtake.stageWithFlipAfter(0.55,0.1))
    );

//    OutputList BackwardMiddleTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabMiddleTele).addOption(PLACING, ForwardTeleMiddle);

    AutoModule BackwardPlaceLowTele = new AutoModule(
            lift.changeLow(false),
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(LOW)+2)
    );


    AutoModule BackwardGrabLowTele = new AutoModule(
            outtakeStatus.ChangeMode(PLACING),
            heightMode.ChangeMode(LOW),
            lift.changeLow(true),
            outtake.stageClose(0.22),
            lift.moveTimeBack(-0.2, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.3;}else{return 0.0;}}),
            lift.changeCutoff(0.0),
            outtake.stageWithFlip(0.55,0.0),
            lift.stageLift(1.0, heightMode.getValue(LOW)+2)
    );
//
//    OutputList BackwardLowTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabLowTele).addOption(PLACING, ForwardTeleLow);


    AutoModule BackwardGrabGroundTele = new AutoModule(
            outtakeStatus.ChangeMode(PLACING),
            heightMode.ChangeMode(GROUND),
            lift.changeGround(true),
            outtake.stageClose(0.22),
            outtake.stageStart(0.0),
            lift.moveTimeBack(-0.2, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.3;}else{return 0.0;}}),
            lift.changeCutoff(0.0),
            outtake.stage(0.3, 0.0),
            RobotPart.pause(0.5),
            lift.stageLift(1.0, heightMode.getValue(GROUND)+10),
            outtake.stage(0.1, 0.0)
    );

    AutoModule BackwardPlaceGroundTele = new AutoModule(
            lift.changeGround(false),
            outtakeStatus.ChangeMode(PLACING),
            heightMode.ChangeMode(GROUND),
            lift.moveTime(-0.4, 0.5)
    );

//    OutputList BackwardGroundTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabGroundTele).addOption(PLACING, ForwardTeleGround);

    AutoModule ForwardTeleBottom = new AutoModule(
            outtakeStatus.ChangeMode(DRIVING),
            lift.changeHigh(false),
            outtake.stageStart(0.0),
            outtake.stageOpen(0.0),
            lift.resetCutoff(),
            lift.stageLift(1.0,0)
    );

    AutoModule CapGrab = new AutoModule(outtake.stageClose(0.0));
    AutoModule CapPick = new AutoModule(lift.moveTime(1.0, 0.18));


    AutoModule ResetLift = new AutoModule(lift.moveTime(-0.3, 0.5),  lift.resetLift() );
    //    AutoModule RetractOdometry = new AutoModule(drive.stageRetract());
//    AutoModule EngageOdometry = new AutoModule(drive.stageEngage());
    AutoModule UprightCone = new AutoModule(lift.stageLift(1.0, 14), outtake.stage(0.08, 0.0), driveMode.ChangeMode(SLOW));
    AutoModule FixCone = new AutoModule(lift.moveTimeBack(0.1, -0.5, () -> 0.4), lift.moveTimeBack(-0.2, -0.3, () -> 0.8), outtake.stageStart(0.0), driveMode.ChangeMode(MEDIUM));
    AutoModule TakeOffCone = new AutoModule(heightMode.ChangeMode(HIGH), outtakeStatus.ChangeMode(PLACING), outtake.stageClose(0.0), lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5).attach(outtake.stageReadyStartAfter(0.5)),RobotPart.pause(0.1),outtake.stageFlip(0.0));

    static AutoModule ForwardStackTele(int i){return new AutoModule(
            lift.changeCutoff(2),
            outtake.stageOpen(0.0),
            outtake.stageStart(0.0),
            lift.stageLift(1.0,  i == 0 ? 13.5 : Math.max(13.5 - (i*13.5/4.6), 0)),
            driveMode.ChangeMode(SLOW)
    );}


    /**
     * Misc
     */
    Independent MoveToZero = new Independent() { @Override public void define() {addSetpoint(0.0, 0.01, 0.0); }};

    Point cyclePointStart = new Point(Field.halfWidth-19, -51);

    Independent MoveToCycleStart = new Independent() {
        @Override
        public void define() {
            addCustomCode(() -> {
                drive.noStrafeLock = true;
                ArrayList<Double> xs = new ArrayList<>(); ArrayList<Double> ys = new ArrayList<>();
                whileNotExit(() -> xs.size() > 3, () -> {
                    distanceSensors.ready();
                    xs.add(distanceSensors.getRightDistance() - cyclePointStart.getX());
                    ys.add(-distanceSensors.getFrontDistance() - cyclePointStart.getY());
                });
                Point point = new Point(Iterator.forAllAverage(xs), Iterator.forAllAverage(ys));
                odometry.setHeadingUsingOffset(0.0);
                odometry.setPointUsingOffset(point);
                fault.warn("ODOMETRY RESET FAILED at point: " + point + " with odometry pose: " + odometry.getPose(), Expectation.EXPECTED, Magnitude.CRITICAL, Precision.range(odometry.getPose().getAngle(), 4) && Double.isFinite(odometry.getPose().getY()) && Double.isFinite(odometry.getPose().getX()), true);
                pause(0.05);
            });
            addWaypoint(1.0, -13, -22, 0);
            addSegment(0.8, 0.7, mecanumNonstopSetPoint, 0.5, 1.0, 0);
            addCustomCode(DriveMode(true));
            addAutoModule(ForwardTeleBottom);
        }
    };


    /**
     * Cycle
     */

    static CodeSeg DriveMode(boolean slow){return () -> {
        if(slow){
            driveMode.set(SLOW);
        }else{
            driveMode.set(MEDIUM);
        }
    };}

    static AutoModule BackwardCycle(Height height, double offset) {return new AutoModule(
            outtake.stageClose(0.18),
            outtake.stageWithFlip(0.72, 0.0),
            lift.stageLift(1.0, heightMode.getValue(height)+offset)
    );}

    static AutoModule BackwardCycleMove(Height height, double offset) {return new AutoModule(
            drive.moveTime(0.6, 0, 0,0.3),
            outtake.stageClose(0.2),
            outtake.stageWithFlip(0.75, 0.0),
            lift.stageLift(1.0, heightMode.getValue(height)+offset)
    );}

    AutoModule BackwardCycleMoveHold = new AutoModule(
            drive.moveTime(0.6, 0, 0,0.6),
            outtake.stageClose(0.2),
            outtake.stageReadyStart(0.2),
            outtake.stageFlip(0.0),
            lift.stageLift(1.0, heightMode.getValue(LOW)+4)
    );


    AutoModule StageStart = new AutoModule(outtake.stageStart(0.0));
    AutoModule ReadyStart = new AutoModule(outtake.stageReadyStart(0.0));

    AutoModule HoldMiddle = new AutoModule(outtake.stageClose(0.18), outtake.stageMiddle(0.0));

    AutoModule ForwardCycleLow = new AutoModule(
            outtake.stageEnd(0.2),
            outtake.stageOpen(0.0),
            lift.stageLift(1.0,0).attach(outtake.stageStartAfter(0.4))
    );


    Point cyclePoint = new Point(46.5, -50);
    Independent CycleFirst = new Independent() {
        @Override
        public void define() {
            addCustomCode(() -> {
                drive.noStrafeLock = false;
                ArrayList<Double> xs = new ArrayList<>(); ArrayList<Double> ys = new ArrayList<>();
                whileNotExit(() -> xs.size() > 3, () -> {
                    distanceSensors.ready();
                    xs.add(distanceSensors.getRightDistance() - cyclePoint.getX());
                    ys.add(-distanceSensors.getFrontDistance() - cyclePoint.getY());
                });
                Point point = new Point(Iterator.forAllAverage(xs), Iterator.forAllAverage(ys));
                if(point.getDistanceTo(new Point()) < 15) {
                    odometry.setPointUsingOffset(point);
                }else{
                    fault.warn("Distance sensors could not lock on, hard resetting odometry at " + point, Expectation.EXPECTED, Magnitude.MODERATE);
                    odometry.reset();
                }
                odometry.setHeadingUsingOffset(0.0);
                pause(0.05);
                fault.warn("ODOMETRY RESET FAILED at point: " + point + " with odometry pose: " + odometry.getPose(), Expectation.EXPECTED, Magnitude.CRITICAL, Precision.range(odometry.getPose().getAngle(), 4) && odometry.getPose().getDistanceTo(new Pose()) < 15 && Double.isFinite(odometry.getPose().getY()) && Double.isFinite(odometry.getPose().getX()), true);
            });
        }
    };


    AutoModule ForwardCycle = new AutoModule(
            outtake.stageEnd(0.1),
            outtake.stageOpen(0.0),
            lift.moveTime(-0.7, 0.1),
            lift.stageLift(1.0,0).attach(outtake.stageStartAfter(0.0))
    );

    static Independent Cycle(int i) {return new Independent() {
        @Override
        public void define() {
            addCustomCode(() -> {lift.adjust = 0;});
            addSegment(0.35, 0.2, mecanumNonstopSetPoint, -0.3, 16.0, 0.0);
            addConcurrentAutoModuleWithCancel(BackwardCycle(HIGH, 3), 0.2);
            addWaypoint(0.52, -1.0, -26, 0.0);
            addSegment(0.3, 0.8, mecanumNonstopSetPoint, -1.5, -32.5, 0.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle);
            if (i == 1 || i == 4) {
                addSegment(0.8, 0.3, mecanumNonstopSetPoint, -0.3, -1.0, 0.0);
                addCustomCode(() -> {
                    ArrayList<Double> xs = new ArrayList<>();
                    ArrayList<Double> ys = new ArrayList<>();
                    whileNotExit(() -> xs.size() > 3, () -> {
                        distanceSensors.ready();
                        xs.add(distanceSensors.getRightDistance() - cyclePoint.getX());
                        ys.add(-distanceSensors.getFrontDistance() - cyclePoint.getY());
                    });
                    Point point = new Point(Iterator.forAllAverage(xs), Iterator.forAllAverage(ys));
                    if(point.getDistanceTo(new Point()) < 10){
                        odometry.setPointUsingOffset(point);
                    }
                });
            }else{
                addWaypoint(0.4, -0.3, 8.0, 0.0);
            }
        }

        @Override
        public void flip() {}
    };}

    Independent CycleLast = new Independent() {
        @Override
        public void define() {
            addSegment(0.8, 0.7, mecanumNonstopSetPoint, 0, 3.0, 0);
            addCustomCode(DriveMode(false));
        }
    };

    Machine MachineCycle = new Machine()
            .addIndependent(CycleFirst)
            .addIndependent(7, AutoModuleUser::Cycle)
            .addIndependent(AutoModuleUser.CycleLast)
            ;






    /**
     * Cycle Extra
     */

    Machine MachineCycleExtra = new Machine()
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addCustomCode(() -> {
                        driveMode.set(SLOW);
                        lift.adjust = 0;
                        ArrayList<Double> xs = new ArrayList<>(); ArrayList<Double> ys = new ArrayList<>();
                        whileNotExit(() -> xs.size() > 3, () -> {
                            distanceSensors.ready();
                            xs.add(distanceSensors.getRightDistance() - cyclePoint.getX());
                            ys.add(-distanceSensors.getFrontDistance() - cyclePoint.getY());
                        });
                        Point point = new Point(Iterator.forAllAverage(xs), Iterator.forAllAverage(ys));
                        if(point.getDistanceTo(new Point()) < 15) {
                            odometry.setPointUsingOffset(point);
                        }else{
                            fault.warn("Distance sensors could not lock on, hard resetting odometry at " + point, Expectation.EXPECTED, Magnitude.MODERATE);
                            odometry.reset();
                        }
                        odometry.setHeadingUsingOffset(0.0);
                        pause(0.05);
                        fault.warn("ODOMETRY RESET FAILED at point: " + point + " with odometry pose: " + odometry.getPose(), Expectation.EXPECTED, Magnitude.CRITICAL, Precision.range(odometry.getPose().getAngle(), 4) && odometry.getPose().getDistanceTo(new Pose()) < 15 && Double.isFinite(odometry.getPose().getY()) && Double.isFinite(odometry.getPose().getX()), true);
                    });
                    addSegment(0.4, 0.4, mecanumNonstopSetPoint,  -2, 18,0.01);
                    addAutoModule(HoldMiddle);
                    addWaypoint(0.6,-2,-10,-10.0);
                    addConcurrentAutoModule(BackwardCycle(MIDDLE, 4));
                    addWaypoint(1.0, -25.0, -7.0, -25.0);
                    addSegment(0.8, 0.5, mecanumNonstopSetPoint, -48.5, -25.0, -25.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addConcurrentAutoModuleWithCancel(ForwardCycle, 0.15);
                    addWaypoint(0.5, -30.0, 0.0, -21.0);
                    addWaypoint(0.3, -27.0, 16.0, -25.0);
                    addSegment(0.5, 0.1, mecanumNonstopSetPoint, -27.0, 20.0, -25.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addCustomCode(() -> {
                        bot.cancelAutoModules(); bot.addAutoModule(BackwardCycleMove(LOW, 3.5));
                        pause(0.5);
                    });
                    addWaypoint(1.0, -25.0, 25.0, 0.0);
                    addWaypoint(1.0, -30.0, 5.0, -45);
                    addWaypoint(1.0, -50.0, -8.0, -85);
                    addWaypoint(0.6, -80.0, -20.0, -50);
                    addSegment(0.8, 0.4, mecanumNonstopSetPoint, -99.0, -35.0, -50);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addConcurrentAutoModuleWithCancel(ForwardCycleLow, 0.15);
                    addWaypoint(1.0, -70.0, -20.0, -75);
                    addWaypoint(0.8, -50.0, -10.0, -70);
                    addWaypoint(0.45, -40.0, 0.0, 0.0);
                    addWaypoint(0.4, -27.0, 16.0, 0.0);
                    addSegment(0.5, 0.1, mecanumNonstopSetPoint, -27.0, 20.0, -25.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addCustomCode(() -> {
                        bot.cancelAutoModules(); bot.addAutoModule(BackwardCycleMove(LOW, 3.5));
                        pause(0.5);
                    });
                    addSegment(1.3, 0.5, mecanumNonstopSetPoint, -35.0, 23.0, -57.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addConcurrentAutoModuleWithCancel(ForwardCycleLow,0.15);
                    addWaypoint(0.8, -25.0, 36.0, -57.0);
                    addWaypoint(0.6, -32.0, 20.0, -25.0);
                    addSegment(1.3, 0.6, mecanumNonstopSetPoint, -50, 45.0, -90);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addCustomCode(() -> {
                        bot.cancelAutoModules(); bot.addAutoModule(BackwardCycleMoveHold);
                        pause(0.6);
                    });
                    addSegment(1.2, 1.0, mecanumNonstopSetPoint, 94.0, 45.0, -90.0);
                    addCustomCode(() -> {
                        whileTime(() -> drive.move(0, isFlipped() ? 0.5 : -0.5, 0), 0.6);
                        final ArrayList<Double> xs = new ArrayList<>();
                        whileNotExit(() -> xs.size() > 3, () -> {
                            distanceSensors.ready();
                            xs.add(161.0 - distanceSensors.getFrontDistance());
                        });
                        Point point = new Point(Iterator.forAllAverage(xs), 57);

                        if(point.getDistanceTo(odometry.getPose().getPoint()) < 25) {
                            odometry.setPointUsingOffset(point);
                        }else{
                            fault.warn("Distance sensors could not lock on at " + point, Expectation.EXPECTED, Magnitude.MODERATE);
                        }
                        odometry.setHeadingUsingOffset(-90.0);
                        pause(0.05);
                    });
                    addConcurrentAutoModuleWithCancel(BackwardCycleMove(LOW, 3.5));
                    addSegment(1.2, 0.5, mecanumNonstopSetPoint, 88.0, 33.0, 0.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addWaypoint(0.6, 91.0, -6.0, 21.0);
                    addSegment(1.2, 0.5, mecanumNonstopSetPoint, 101.0, -33.0, 45.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addConcurrentAutoModuleWithCancel(ForwardCycleLow, 0.15);
                    addWaypoint(1.0, 70.0, -20.0, 75);
                    addWaypoint(0.8, 50.0, -10.0, 70);
                    addWaypoint(0.5, 40.0, 0.0, 0.0);
                    addWaypoint(0.4, 27.0, 16.0, 0.0);
                    addSegment(0.5, 0.1, mecanumNonstopSetPoint, 27.0, 20.0, 25.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addCustomCode(() -> {
                        bot.cancelAutoModules(); bot.addAutoModule(BackwardCycleMove(LOW, 3.5));
                        pause(0.5);
                    });
                    addSegment(1.3, 0.5, mecanumNonstopSetPoint, 36.0, 26.0, 57.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addConcurrentAutoModuleWithCancel(ForwardCycleLow,0.15);
                    addWaypoint(0.8, 20.0, 42.0, 57.0);
                    addSegment(0.8, 0.1, mecanumNonstopSetPoint, 27.0, 20.0, 25.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addCustomCode(() -> {
                        bot.cancelAutoModules(); bot.addAutoModule(BackwardCycleMove(MIDDLE, 3.5));
                        pause(0.5);
                    });
                    addWaypoint(0.5, 34.0, -3.0, 12.0);
                    addSegment(0.8, 0.5, mecanumNonstopSetPoint, 48.5, -25.0, 25.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addConcurrentAutoModuleWithCancel(ForwardCycle, 0.15);
                    addSegment(0.8, 0.4, mecanumNonstopSetPoint, 30, -15.0, 0.0);
                }
            })
            .addIndependentWithPause(new Independent() {
                @Override
                public void define() {
                    addWaypoint(0.8, 30.0, -55.0, 0.0);
                    addWaypoint(0.4, 16.0, -75.0, -45.0);
                    addWaypoint(0.8, 63.0, -81.0, -90.0);
                    addConcurrentAutoModule(ForwardStackTele(0));
                    addSegment(1.4, 0.5, mecanumNonstopSetPoint, 102.0, -76.0, -90.0);
                    addCustomCode(() -> {
                        ArrayList<Double> ys = new ArrayList<>();
                        whileNotExit(() -> ys.size() > 3 || odometry.getX() > 134, () -> {
                            drive.move(0.25, 0, 0);
                            distanceSensors.ready();
                            double dis = distanceSensors.getRightDistance();
                            if(dis < 30){
                                ys.add(-67 - distanceSensors.getRightDistance());
                            }
                        });
                        drive.halt();
                        Point point = new Point(odometry.getX(), Iterator.forAllAverage(ys));
                        Pose poseBefore = odometry.getPose();
//                        log.record("PoseBefore", poseBefore);
                        odometry.setPointUsingOffset(point);
                        Pose poseAfter = odometry.getPose();
//                        log.record("Point", point);
//                        log.record("PoseAfter", poseAfter);
                        pause(0.05);
                    });
                    addSegment(1.4, 0.5, mecanumNonstopSetPoint, 153.0, -82, -90.0);
                    addCustomCode(() -> {
                        whileTime(() -> drive.move(0.25, 0, 0), 0.5);
                        Point point = new Point(153, odometry.getY());
                        odometry.setPointUsingOffset(point);
                        pause(0.05);
                    });
                }
            })
            .addIndependentWithPause(AutoModuleUser.BackwardCycle(0.0, 0.0))
            .addIndependentWithPause(ForwardCycle(1, 0.0, 0.0))
            .addIndependentWithPause(AutoModuleUser.BackwardCycle(0.5, 0.2))
            .addIndependentWithPause(ForwardCycle(2, 0.5, 0.5))
            .addIndependentWithPause(AutoModuleUser.BackwardCycle(1.0, 0.4))
            .addIndependentWithPause(ForwardCycle(3, 1.0, 1.0))
            .addIndependentWithPause(AutoModuleUser.BackwardCycle(1.5, 0.6))
            .addIndependentWithPause(ForwardCycle(4, 1.5, 1.5))
            .addIndependentWithPause(AutoModuleUser.BackwardCycle(2.0, 0.8))
            .addIndependent(new Independent() {
                @Override
                public void define() {
                    addCustomCode(() -> { lift.stacked = false; });
                    addConcurrentAutoModuleWithCancel(ForwardStackCycle(5), 0.15);
                    addSegment(1.2, 0.5, mecanumNonstopSetPoint, 85.0, -55.0, 0.0);
                }
            })
            .addInstruction(() -> {
                driveMode.set(MEDIUM);
                bot.cancelAutoModules();
                if(lift.skipping) {
                    if(outtake.isClawClosed()) {
                        bot.addAutoModule(new AutoModule(outtake.stageReadyStart(0.0), lift.stageLift(0.6, heightMode.getValue(LOW)+2), RobotPart.pause(0.2), outtake.stageFlip(0.0)));
                    }else{
                        bot.addAutoModule(new AutoModule(outtake.stageOpen(0.0), outtake.stageStart(0.0), lift.stageLift(0.6, 0.0)));
                    }
                    lift.skipping = false;
                }
            })
            ;



    AutoModule BackwardCycleHigh = new AutoModule(
            outtake.stageClose(0.2),
            lift.moveTimeBack(-0.7, 0.5, () -> 0.3).attach(outtake.stage(0.71, 0.0)),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+2).attach(outtake.stageFlipAfter(0.33))
    );


    static Independent BackwardCycle(double x, double s){
        return new Independent() {
            @Override
            public void define() {
                addCustomCode(() -> {
                    bot.cancelAutoModules(); bot.addAutoModule(BackwardCycleHigh);
                    pause(0.5);
                });
                addWaypoint(0.6, 115-x, -79.5+s, -90.0);
                addSegment(1.3, 0.4, mecanumNonstopSetPoint, 76-x, -93+s, -60);
            }
        };
    }

    static Independent ForwardCycle(int i, double x, double s){
        return new Independent() {
            @Override
            public void define() {
                addConcurrentAutoModuleWithCancel(ForwardStackCycle(i), 0.15);
                addWaypoint(0.5,103 - x, -79.5+s, -83);
                addWaypoint(0.7,146 - x, -79.5+s, -90);
                addSegment( 0.2 ,0.1, mecanumNonstopWayPoint,  153 - x, -79.5+s, -90);
            }
        };
    }

    static AutoModule ForwardStackCycle(int i){return new AutoModule(
            outtake.stageEnd(0.15),
            outtake.stageOpen(0.0),
            lift.moveTime(-0.7, 0.15),
            lift.stageLift(1.0,  i == 0 ? 14.5 : Math.max(14.5 - (i*14.5/4.6), 0)).attach(outtake.stageStartAfter(0.1))
    );}


}

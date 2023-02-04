package automodules;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

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
import util.condition.OutputList;
import util.template.Iterator;
import util.template.Precision;

import static global.General.bot;
import static global.General.log;
import static global.Modes.*;
import static global.Modes.Height.GROUND;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;
import static global.Modes.Height.MIDDLE;
import static global.Modes.OuttakeStatus.DRIVING;
import static global.Modes.OuttakeStatus.PLACING;


public interface AutoModuleUser extends RobotUser{


    CodeSeg stack = () -> {bot.addAutoModule(AutoModuleUser.ForwardStackTele(lift.stackedMode)); lift.stackedMode++;};
    TeleTrack kappaBefore = new TeleTrack(
            new TeleTrack.Step(heightMode.setTo(LOW)), // LOW
            new TeleTrack.Step(heightMode.setTo(LOW)), // TERMINAL
            new TeleTrack.Step(heightMode.setTo(LOW)), // LOW
            new TeleTrack.Step(heightMode.setTo(LOW)).add(stack), // LOW
            new TeleTrack.Step(heightMode.setTo(HIGH)).add(stack), // HIGH
            new TeleTrack.Step(heightMode.setTo(GROUND)).add(stack), // GROUND
            new TeleTrack.Step(heightMode.setTo(GROUND)), // TERMINAL
            new TeleTrack.Step(heightMode.setTo(GROUND)), // GROUND
            new TeleTrack.Step(heightMode.setTo(LOW)) // LOW + CAP
    );

    static void enableKappa(){ kappaBefore.enable(); }
    static void disableKappa(){ kappaBefore.disable(); }

    /**
     * Tele
     */


    /**
     * Forward
     */


    AutoModule ForwardTeleHigh = new AutoModule(
            outtakeStatus.ChangeMode(DRIVING),
            outtake.stageEnd(0.1),
            outtake.stageOpen(0.0),
            lift.resetCutoff(),
            lift.stageLift(0.9,0).attach(outtake.stageStartAfter(0.1)),
            kappaBefore.next()
    );

    AutoModule ForwardTeleMiddle = new AutoModule(
            outtakeStatus.ChangeMode(DRIVING),
            outtake.stageEnd(0.1),
            outtake.stageOpen(0.0),
            drive.moveTime(1.0, 0.0, 0.0, 0.22),
            outtake.stageStart(0.0),
            lift.resetCutoff(),
            lift.stageLift(1.0,0),
            kappaBefore.next()
    );

    AutoModule ForwardTeleLow = new AutoModule(
            outtakeStatus.ChangeMode(DRIVING),
            outtake.stageEnd(0.1),
            outtake.stageOpen(0.0),
            drive.moveTime(1.0, 0.0, 0.0, 0.25),
            outtake.stageStart(0.0),
            lift.resetCutoff(),
            lift.stageLift(1.0,0),
            kappaBefore.next()
    );

    AutoModule ForwardTeleGround = new AutoModule(
            outtakeStatus.ChangeMode(DRIVING),
            outtake.stageStart(0.1),
            lift.resetCutoff(),
            lift.stageLift(1.0,0),
            outtake.stageOpen(0.0),
            kappaBefore.next()
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
            lift.moveTimeBack(-0.25, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.2;}else{return 0.0;}}),
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+2)
    );

    OutputList BackwardHighTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabHighTele).addOption(PLACING, ForwardTeleHigh);

    AutoModule BackwardGrabMiddleTele = new AutoModule(
            outtakeStatus.ChangeMode(PLACING),
            heightMode.ChangeMode(MIDDLE),
            outtake.stageClose(0.22),
            lift.moveTimeBack(-0.25, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.2;}else{return 0.0;}}),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+2).attach(outtake.stageReadyEndAfter(0.1))
    );

    OutputList BackwardMiddleTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabMiddleTele).addOption(PLACING, ForwardTeleMiddle);

    AutoModule BackwardGrabLowTele = new AutoModule(
            outtakeStatus.ChangeMode(PLACING),
            heightMode.ChangeMode(LOW),
            outtake.stageClose(0.22),
            lift.moveTimeBack(-0.25, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.2;}else{return 0.0;}}),
            lift.changeCutoff(0.0),
            outtake.stageReadyEndAfter(0.0),
            lift.stageLift(1.0, heightMode.getValue(LOW)+2)
    );

    OutputList BackwardLowTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabLowTele).addOption(PLACING, ForwardTeleLow);


    AutoModule BackwardGrabGroundTele = new AutoModule(
            outtakeStatus.ChangeMode(PLACING),
            heightMode.ChangeMode(GROUND),
            outtake.stageClose(0.22),
            lift.moveTimeBack(-0.25, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.2;}else{return 0.0;}}),
            lift.changeCutoff(0.0),
            lift.stageLift(1.0, heightMode.getValue(GROUND)+2)
    );

    OutputList BackwardGroundTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabGroundTele).addOption(PLACING, ForwardTeleGround);

    AutoModule ForwardTeleBottom = new AutoModule(
            outtakeStatus.ChangeMode(DRIVING),
            lift.changeHigh(false),
            outtake.stageStart(0.0),
            outtake.stageOpen(0.0),
            lift.resetCutoff(),
            lift.stageLift(1.0,0)
    );


    AutoModule ResetLift = new AutoModule(lift.moveTime(-0.3, 0.5),  lift.resetLift() );
//    AutoModule RetractOdometry = new AutoModule(drive.stageRetract());
//    AutoModule EngageOdometry = new AutoModule(drive.stageEngage());
    AutoModule UprightCone = new AutoModule(lift.stageLift(1.0, 15));
    AutoModule TakeOffCone = new AutoModule(outtakeStatus.ChangeMode(PLACING), outtake.stageClose(0.0), lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5).attach(outtake.stageReadyStartAfter(0.5)),RobotPart.pause(0.3),outtake.stageFlip(0.0));

    static AutoModule ForwardStackTele(int i){return new AutoModule(
            lift.changeCutoff(2),
            outtake.stageOpen(0.0),
            outtake.stageStart(0.0),
            lift.stageLift(1.0,  i == 0 ? 14.5 : Math.max(15.0 - (i*15.0/5.0), 0))
    );}

    /**
     * Misc
     */
    Independent MoveToZero = new Independent() { @Override public void define() {addSetpoint(0.0, 0.01, 0.0); }};
    Independent MoveToCycleStart = new Independent() {
        @Override
        public void define() {

//            addCustomCode(JunctionScannerAll::resume, 0.05);
//            addCustomCode(() -> {
//                Pose pose = JunctionScannerAll.getPose();
////                whileActive(() -> {
////                    log.show("JunctionScannerPose", JunctionScannerAll.getPose());
////                });
//                Point point = new Point(-pose.getX(), odometry.getY());
//                odometry.setCurrentPoint(point); odometry.setCurrentPoint(point);
//            });
//            addSegment(0.7, 0.3, mecanumNonstopSetPoint, 0, 0.01, 0);

            addCustomCode(() -> {  odometry.setHeading(0); gyro.reset();}, 0.05);




//            addCustomCode(JunctionScannerAll::resume, 0.05);
            addCustomCode(ResetOdometryForCycle(cyclePoint), 0.05);
            addWaypoint(1.0, -10, -26, 0);
            addSegment(0.7, 0.7, mecanumNonstopSetPoint, 0, 0.01, 0);
//            addCustomCode(ResetOdometryForCycle(cyclePoint2), 0.05);
//            addSegment(0.6, 0.5, mecanumNonstopSetPoint, 0, 0.01, 0);


//            addCustomCode(() -> {
//                Pose pose = new Pose();
//                while (pose.getLength() < 10) {
//                    ArrayList<Pose> poses = new ArrayList<>();
//                    for (int i = 0; i < 100; i++) {
//                        poses.add(JunctionScannerAll.getPose());
//                    }
//                    pose = Pose.forAllAverage(poses);
////                    log.show("JunctionScannerPose", pose);
////                    log.show("CurrentPose", JunctionScannerAll.getPose());
//                }
////                pose.add(new Pose(0, -23, 0));
//                Point point = new Point(pose.getX(), odometry.getY());
//                odometry.setCurrentPoint(point); odometry.setCurrentPoint(point);
////                odometry.setCurrentPose(pose);
////                odometry.setCurrentPose(pose);
////                whileActive(() -> {
////                    log.show("JunctionScannerPose", pose);
////                });
//
//            });
//            addSegment(0.7, 0.7, mecanumNonstopSetPoint, 0, 0.01, 0);
//            addCustomCode(JunctionScannerAll::pause, 0.05);
            addCustomCode(() -> drive.slow = true);
            addAutoModule(ForwardTeleBottom);
        }
    };


    /**
     * Cycle
     */
    Point cyclePoint = new Point(Field.halfWidth-19, -51);
    Point cyclePoint2 = new Point(46.5, -50);
    static CodeSeg ResetOdometryForCycle(Point point) {return  () -> {
        distanceSensors.ready();
        Point point1 = new Point(distanceSensors.getRightDistance() - point.getX(),-distanceSensors.getFrontDistance() - point.getY());
        odometry.setCurrentPoint(point1); odometry.setCurrentPoint(point1);
    };}

    static CodeSeg SoftResetOdometryForCycle(Point point) {return  () -> {
        distanceSensors.ready();
        double front = distanceSensors.getFrontDistance();
        double right = distanceSensors.getRightDistance();
        if(Precision.range(front, 45, 51) && Precision.range(right, 44, 50)){
            Point point1 = new Point(right - point.getX(),-front - point.getY());
            odometry.setPoseUsingOffset(point1);
        }
    };}

    static AutoModule BackwardCycle(Height height, double offset) {return new AutoModule(
            outtake.stageClose(0.18),
            outtake.stageReadyEnd(0.0),
//            lift.moveTime(1.0, 0.3),
            lift.stageLift(1.0, heightMode.getValue(height)+offset)
    );}

    AutoModule ForwardCycle = new AutoModule(
            outtake.stageEnd(0.1),
            lift.moveTime(-0.7, 0.1).attach(outtake.stageOpen(0.0)),
            lift.stageLift(1.0,0).attach(outtake.stageStartAfter(0.05))
    );

    AutoModule StageStart = new AutoModule(outtake.stageStart(0.0));
    AutoModule ReadyStart = new AutoModule(outtake.stageReadyStart(0.0));

    AutoModule HoldMiddle = new AutoModule(outtake.stageClose(0.18), outtake.stageMiddle(0.0));

    static Independent Cycle2(int i) { return new Independent() {
            @Override
            public void define() {
            double x = 0.0;
            if(i+1 == 1){
                addCustomCode(() -> drive.slow = false);
                addAutoModule(leds.autoModuleColor(OLed.LEDColor.OFF));
                addCustomCode(SoftResetOdometryForCycle(cyclePoint2), 0.05);
                addSegment(0.35, 0.55, mecanumNonstopWayPoint, x, 11, 0);
                addPause(0.05);
            }
            if(i+1 != 11){
                if(i+1 == 10){ addAutoModule(leds.autoModuleColor(OLed.LEDColor.ORANGE)); }
                addConcurrentAutoModuleWithCancel(BackwardCycle(HIGH, 4), 0.2);
                addWaypoint(0.57, x-1, -26, -2);
                addSegment(0.15, 0.8, mecanumNonstopSetPoint, x-1, -32, -2);
                addConcurrentAutoModuleWithCancel(ForwardCycle);
                addCustomCode(() -> {
                    ArrayList<Double> values = new ArrayList<>();
                    whileNotExit(() -> values.size() > 3, () -> {
                        drive.move(0.38,0.12*Math.signum(Precision.attract(x-odometry.getX()-1, 1)), 0.008*odometry.getHeading());
                        distanceSensors.ready();
                        double distance = distanceSensors.getRightDistance();
                        if(distance < 50){ values.add(distance); }
                    });
                    double avgDis = Iterator.forAllAverage(values);
                    Point point = new Point(((avgDis-48.0)+odometry.getX())/2.0, odometry.getY());
                    odometry.setPoseUsingOffset(point);
                });
                addSegment(0.15, 0.4, mecanumNonstopSetPoint,  x-0.5, 5.0,0);
            } else{
                addAutoModule(leds.autoModuleColor(OLed.LEDColor.GREEN));
                addConcurrentAutoModuleWithCancel(HoldMiddle, 0.2);
                addSegment(0.7, 0.7, mecanumNonstopSetPoint, x, 0.01, 0);
                addPause(0.05);
//                addAutoModule(RetractOdometry);
                addAutoModule(leds.autoModuleColor(OLed.LEDColor.OFF));
            }
    }};}

    Machine MachineCycle = new Machine()
            .addIndependent(11, AutoModuleUser::Cycle2)
    ;



    AutoModule ForwardCycleLow = new AutoModule(
            lift.moveTime(1.0, 0.1).attach(outtake.stageEnd(0.2)),
            outtake.stageOpen(0.0),
            lift.stageLift(1.0,0).attach(outtake.stageStartAfter(0.1))
    );






    /**
     * Cycle Medium
     */
    Independent CycleExtra = new Independent() {
        @Override
        public void define() {
            addCustomCode(SoftResetOdometryForCycle(cyclePoint2), 0.05);
            addWaypoint(0.6,0,-10,-10.0);
            addConcurrentAutoModule(BackwardCycle(MIDDLE, 4));
            addWaypoint(1.0, -25.0, -12.0, -25.0);
            addSegment(0.6, 0.55, mecanumNonstopWayPoint, -47.0, -25.0, -25.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle);
            addSegment(1.3, 0.45, mecanumNonstopWayPoint,  -24.0, 40.0, -21.0);
            addConcurrentAutoModuleWithCancel(BackwardCycle(LOW, 2), 0.2);
            addWaypoint(1.0, -25.0, 25.0, 0.0);
            addWaypoint(1.0, -30.0, 5.0, -45);
            addWaypoint(1.0, -50.0, -8.0, -85);
            addSegment(1.2, 0.65, mecanumNonstopSetPoint, -99.0, -35.0, -50);
            addConcurrentAutoModuleWithCancel(ForwardCycleLow, 0.1);
            addWaypoint(1.0, -70.0, -20.0, -90);
            addWaypoint(1.0, -50.0, -10.0, -70);
            addWaypoint(0.8, -30.0, 0.0, 0.0);
            addSegment(1.0, 0.5, mecanumNonstopWayPoint,  -25.0, 36.0, -21.0);
            addConcurrentAutoModuleWithCancel(BackwardCycle(LOW, 1), 0.3);
            addSegment(1.0, 0.6, mecanumNonstopSetPoint, -35.0, 24.0, -57.0);
            addConcurrentAutoModuleWithCancel(ForwardCycleLow,0.1);
            addWaypoint(-25.0, 36.0, -57.0);
            addSegment(1.0, 0.8, mecanumNonstopSetPoint, -32.0, 20.0, -21.0);
            addSegment(0.5, 0.5, mecanumNonstopWayPoint,  -25.0, 36.0, -21.0);
            addConcurrentAutoModuleWithCancel(BackwardCycle(LOW, 0), 0.2);
            addSegment(0.5, 0.7, mecanumNonstopWayPoint,  -25.0, 40.0, -90.0);
            addWaypoint(1.0, -64.0, 42.0, -90.0);
            addWaypoint(1.0, -128.0, 42.0, -125.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle, 0.1);
            addWaypoint(1.0, -80.0, 44.0, -90.0);
        }
    };

    Machine MachineCycleExtra = new Machine()
            .addIndependent(CycleExtra)
    ;


}

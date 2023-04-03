package automodules;

import java.util.ArrayList;

import elements.Field;
import geometry.framework.Point;
import geometry.position.Pose;
import robot.RobotUser;
import robotparts.RobotPart;
import teleutil.independent.Independent;
import teleutil.independent.Machine;
import util.codeseg.CodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Iterator;
import util.template.Precision;

import static global.General.bot;
import static global.General.fault;
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

    /**
     * Forward
     */


    AutoModule ForwardTeleHigh = new AutoModule(
            // 0.7
            RobotPart.pause(0.1),
            lift.stageLift(1.0,0).attach(outtake.stageStartAfter(0.15))
    ).setStartCode(() -> {
            outtakeStatus.set(DRIVING);
            outtake.moveEnd();
            outtake.openClaw();
            lift.resetCutoff();
    });

    // 0.6
    AutoModule ForwardTeleMiddle = new AutoModule(
            RobotPart.pause(0.1),
            lift.stageLift(1.0,0).attach(outtake.stageStartAfter(0.15))
    ).setStartCode(() -> {
            outtakeStatus.set(DRIVING);
            outtake.moveEnd();
            outtake.openClaw();
            lift.resetCutoff();
    });

    // 0.8
    AutoModule ForwardTeleLow = new AutoModule(
            RobotPart.pause(0.1),
            drive.moveTime(1.0, 0.0, 0.0, 0.2),
            outtake.stageStart(0.0),
            lift.stageLift(1.0,0)
    ).setStartCode(() -> {
            outtakeStatus.set(DRIVING);
            outtake.moveEnd();
            outtake.openClaw();
            lift.resetCutoff();
    });


    AutoModule ForwardTeleGround = new AutoModule(
            RobotPart.pause(0.6),
            lift.ResetCutoff(),
            outtake.stageStart(0.0),
            lift.moveTime(-0.4, 0.4)
    ).setStartCode(() -> {
            outtakeStatus.set(DRIVING);
            driveMode.set(MEDIUM);
            outtake.openClaw();
    });

    /**
     * Backward
     */


    AutoModule BackwardGrabHighTele = new AutoModule(
            outtake.stageClose(0.22),
            lift.moveTimeBack(-0.2, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.3;}else{return 0.0;}}),
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+2)
    ).setStartCode(() -> {
            heightMode.set(HIGH);
            outtakeStatus.set(PLACING);
    });

    AutoModule BackwardGrabMiddleTele = new AutoModule(
            outtake.stageClose(0.22),
            lift.moveTimeBack(-0.2, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.3;}else{return 0.0;}}),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+2).attach(outtake.stageReadyEndAfter(0.1))
    ).setStartCode(() -> {
            outtakeStatus.set(PLACING);
            heightMode.set(MIDDLE);
    });

    AutoModule BackwardGrabLowTele = new AutoModule(
            outtake.stageClose(0.22),
            lift.moveTimeBack(-0.2, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.3;}else{return 0.0;}}),
            lift.changeCutoff(0.0),
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(LOW)+2)
    ).setStartCode(() -> {
            outtakeStatus.set(PLACING);
            heightMode.set(LOW);
    });

    AutoModule BackwardGrabGroundTele = new AutoModule(
            outtake.stageClose(0.22),
            outtake.stageStart(0.0),
            lift.moveTimeBack(-0.2, 1.0, () -> {if(lift.stacked){ lift.stacked = false; return 0.3;}else{return 0.0;}}),
            lift.changeCutoff(0.0),
            outtake.stage(0.3, 0.0),
            RobotPart.pause(0.5),
            lift.stageLift(1.0, heightMode.getValue(GROUND)+10),
            outtake.stage(0.1, 0.0)
    ).setStartCode(() -> {
            outtakeStatus.set(PLACING);
            heightMode.set(GROUND);
            lift.setGround(true);
    });

    AutoModule BackwardPlaceGroundTele = new AutoModule(
            lift.moveTime(-0.4, 0.5)
    ).setStartCode(() -> {
            lift.setGround(false);
            outtakeStatus.set(PLACING);
            heightMode.set(GROUND);
    });

    AutoModule ForwardTeleBottom = new AutoModule(
            lift.stageLift(1.0,0)
    ).setStartCode(() -> {
            outtakeStatus.set(DRIVING);
            outtake.moveStart();
            outtake.openClaw();
            lift.resetCutoff();
    });

    AutoModule CapGrab = new AutoModule(outtake.stageClose(0.0));
    AutoModule CapPick = new AutoModule(lift.moveTime(1.0, 0.18));
    AutoModule ResetLift = new AutoModule(lift.moveTime(-0.3, 0.5),  lift.resetLift() );
    AutoModule UprightCone = new AutoModule(lift.stageLift(1.0, 14), outtake.stage(0.08, 0.0), driveMode.ChangeMode(SLOW));
    AutoModule FixCone = new AutoModule(lift.moveTimeBack(0.1, -0.5, () -> 0.4), lift.moveTimeBack(-0.2, -0.3, () -> 0.8), outtake.stageStart(0.0), driveMode.ChangeMode(MEDIUM));
    AutoModule TakeOffCone = new AutoModule(heightMode.ChangeMode(HIGH), outtake.stageClose(0.0), lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5).attach(outtake.stageReadyStartAfter(0.5)),RobotPart.pause(0.1),outtake.stageFlip(0.0));

    static AutoModule ForwardStackTele(int i){return new AutoModule(
            lift.stageLift(1.0,  i == 0 ? 13.5 : Math.max(13.5 - (i*13.5/4.6), 0))
    ).setStartCode(() -> {
        lift.setCutoffPosition(2);
        outtake.openClaw();
        outtake.moveStart();
    });}



    /**
     * Cycle
     */

    static AutoModule BackwardCycle(Height height, double offset) {return new AutoModule(
            outtake.stageClose(0.18),
            outtake.stageWithFlip(0.72, 0.0),
            lift.stageLift(1.0, heightMode.getValue(height)+offset)
    );}

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
            addConcurrentAutoModuleWithCancel(BackwardCycle(HIGH, 4.5), 0.2);
            addWaypoint(0.5, -1.0, -26, 0.0);
            addSegment(0.2, 0.1, mecanumNonstopSetPoint, -1.5, -32.5, 0.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle);
            addWaypoint(0.38, -0.3, 8.0, 0.0);
        }
    };}

    Independent CycleLast = new Independent() {
        @Override
        public void define() {
            addSegment(0.8, 0.7, mecanumNonstopSetPoint, 0, 3.0, 0);
            addCustomCode(driveMode.setTo(SLOW));
        }
    };

    Machine MachineCycle = new Machine()
            .addIndependent(CycleFirst)
            .addIndependent(8, AutoModuleUser::Cycle)
            .addIndependent(AutoModuleUser.CycleLast);

}

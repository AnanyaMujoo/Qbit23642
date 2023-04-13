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
    });

    // 0.6
    AutoModule ForwardTeleMiddle = new AutoModule(
            RobotPart.pause(0.1),
            lift.stageLift(1.0,0).attach(outtake.stageStartAfter(0.15))
    ).setStartCode(() -> {
            outtakeStatus.set(DRIVING);
            outtake.moveEnd();
            outtake.openClaw();
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
    });


    AutoModule ForwardTeleGround = new AutoModule(
            RobotPart.pause(0.6),
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
            outtake.stageClose(0.2),
            lift.checkAndLift(),
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH))
    ).setStartCode(() -> {
            lift.setGround(false);
            heightMode.set(HIGH);
            outtakeStatus.set(PLACING);
    });

    AutoModule BackwardGrabMiddleTele = new AutoModule(
            outtake.stageClose(0.22),
            lift.checkAndLift(),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE)).attach(outtake.stageReadyEndAfter(0.1))
    ).setStartCode(() -> {
            lift.setGround(false);
            outtakeStatus.set(PLACING);
            heightMode.set(MIDDLE);
    });

    AutoModule BackwardGrabLowTele = new AutoModule(
            outtake.stageClose(0.22),
            lift.checkAndLift(),
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(LOW))
    ).setStartCode(() -> {
            lift.setGround(false);
            outtakeStatus.set(PLACING);
            heightMode.set(LOW);
    });

    AutoModule BackwardGrabGroundTele = new AutoModule(
            outtake.stageClose(0.22),
            outtake.stageStart(0.0),
            lift.checkAndLift(),
            outtake.stage(0.3, 0.0),
            RobotPart.pause(0.5),
            lift.stageLift(1.0, 13),
            outtake.stage(0.1, 0.0)
    ).setStartCode(() -> {
            outtakeStatus.set(PLACING);
            heightMode.set(GROUND);
            lift.setGround(true);
    });

    AutoModule BackwardPlaceGroundTele = new AutoModule(
            lift.moveTime(-0.5, 0.7)
    ).setStartCode(() -> {
            lift.setGround(false);
            outtakeStatus.set(PLACING);
            heightMode.set(GROUND);
            outtake.moveStart();
    });

    AutoModule ForwardTeleBottom = new AutoModule(
            lift.stageLift(1.0,0)
    ).setStartCode(() -> {
            outtakeStatus.set(DRIVING);
            outtake.moveStart();
            outtake.openClaw();
    });

    AutoModule CapGrab = new AutoModule(outtake.stageClose(0.2));
    AutoModule ResetLift = new AutoModule(lift.moveTime(-0.3, 0.5),  lift.resetLift() );
    AutoModule UprightCone = new AutoModule(outtake.stage(0.08, 0.0), lift.stageLift(1.0, 9), driveMode.ChangeMode(SLOW));
    AutoModule FixCone = new AutoModule(lift.moveTimeBack(0.1, -0.5, () -> 0.4), outtake.stageStart(0.0), lift.moveTimeBack(-0.2, -0.3, () -> 0.8), driveMode.ChangeMode(MEDIUM));
    AutoModule TakeOffCone = new AutoModule(heightMode.ChangeMode(HIGH), outtake.stageClose(0.0), lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5).attach(outtake.stageReadyStartAfter(0.5)),RobotPart.pause(0.1),outtake.stageFlip(0.0));

    static AutoModule ForwardStackTele(int i){return new AutoModule(
        lift.stageLift(1.0,  Math.max(13 - ((i+1)*13/4.6), -0.5))
    ).setStartCode(() -> {
        outtake.openClaw();
        outtake.moveStart();
    });}



    /**
     * Cycle
     */

    AutoModule BackwardCycle = new AutoModule(
            outtake.stageClose(0.2),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+1.5).attach(outtake.stageEndContinuousWithFlip(1.0, 0.1))
    );

    AutoModule ForwardCycle = new AutoModule(
            lift.stageLift(1.0,0).attach(outtake.stageBack(0.4))
    ).setStartCode(outtake::dropConeRaw);

    static Independent Cycle(int i) {return new Independent() {
        @Override
        public void define() {
            double x = 0.0;
            if(i==0){ addSegment(0.3, 0.1, mecanumNonstopSetPoint, x, 5.0, 0.0);}
            addConcurrentAutoModuleWithCancel(BackwardCycle, 0.2);
            addWaypoint(0.4, x, -38, 0);
            addSegment(0.3, 0.1, mecanumNonstopSetPoint, x+0.5, -44.0, 0.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle, 0.1);
            addWaypoint(0.4, x+0.5, -5, 0);
            addSegment(0.3, 0.1, mecanumNonstopSetPoint, x, 5.0, 0.0);
        }

        @Override
        public void flip() {

        }
    };}


    Machine MachineCycle = new Machine()
            .addInstruction(odometry::reset,0.1)
            .addIndependent(8, AutoModuleUser::Cycle)
            .addIndependent(new Independent() {
                @Override
                public void define() {
                    addSegment(0.4, 0.5, mecanumNonstopSetPoint, 0, 0.0, 0);
                }
            });

}

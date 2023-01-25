package automodules;

import org.checkerframework.checker.units.qual.A;

import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.reactors.MecanumJunctionReactor2;
import autoutil.reactors.Reactor;
import elements.Field;
import elements.Robot;
import geometry.position.Pose;
import global.Modes;
import robot.RobotUser;
import robotparts.RobotPart;
import robotparts.electronics.output.OLed;
import teleop.TerraOp;
import teleutil.TeleTrack;
import teleutil.independent.Independent;
import teleutil.independent.Machine;
import util.codeseg.CodeSeg;
import util.condition.OutputList;

import static global.General.bot;
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
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+2)
    );

    OutputList BackwardHighTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabHighTele).addOption(PLACING, ForwardTeleHigh);

    AutoModule BackwardGrabMiddleTele = new AutoModule(
            outtakeStatus.ChangeMode(PLACING),
            heightMode.ChangeMode(MIDDLE),
            outtake.stageClose(0.22),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+2).attach(outtake.stageReadyEndAfter(0.1))
    );

    OutputList BackwardMiddleTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabMiddleTele).addOption(PLACING, ForwardTeleMiddle);

    AutoModule BackwardGrabLowTele = new AutoModule(
            outtakeStatus.ChangeMode(PLACING),
            heightMode.ChangeMode(LOW),
            outtake.stageClose(0.22),
            lift.changeCutoff(0.0),
            outtake.stageReadyEndAfter(0.0),
            lift.stageLift(1.0, heightMode.getValue(LOW)+2)
    );

    OutputList BackwardLowTele = new OutputList(outtakeStatus::get).addOption(DRIVING, BackwardGrabLowTele).addOption(PLACING, ForwardTeleLow);


    AutoModule BackwardGrabGroundTele = new AutoModule(
            outtakeStatus.ChangeMode(PLACING),
            heightMode.ChangeMode(GROUND),
            outtake.stageClose(0.22),
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
    AutoModule TakeOffCone = new AutoModule(outtake.stageClose(0.0), lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5).attach(outtake.stageReadyStartAfter(0.5)),RobotPart.pause(0.3),outtake.stageFlip(0.0));

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
            addWaypoint(0.8, 65,-71,0);
            addWaypoint(0.4, 93,-72, 0);
            addWaypoint(0.35,  89.0, -62.0, 0.0 );
        }
    };

    CodeSeg ResetOdometry = () -> {
        odometry.reset(); odometry.setCurrentPose(new Pose()); odometry.setCurrentPose(new Pose());
    };

    /**
     * Cycle
     */

    static AutoModule BackwardCycle(Height height) {return new AutoModule(
            outtake.stageClose(0.2),
            outtake.stageReadyEnd(0.1),
            lift.stageLift(1.0, heightMode.getValue(height)+4)
    );}


    AutoModule ForwardCycle = new AutoModule(
            outtake.stageEnd(0.1),
            outtake.stageOpen(0.0),
            lift.resetCutoff(),
            lift.stageLift(1.0,0).attach(outtake.stageStartAfter(0.1))
    );

    AutoModule StageStart = new AutoModule(outtake.stageStart(0.0));
    AutoModule ReadyStart = new AutoModule(outtake.stageReadyStart(0.0));

    AutoModule HoldMiddle = new AutoModule(outtake.stageClose(0.18), outtake.stageMiddle(0.0));

    static Independent Cycle2(int i) { return new Independent() {
            @Override
            public void define() {
            double x = (i*0.0); double y = i*0.0;
            if(i+1 == 1){
                addAutoModule(leds.autoModuleColor(OLed.LEDColor.OFF));
                addWaypoint(0.7,  x, 17+y, 0);
                addWaypoint(0.4,  x, 21+y, 0);
            }
            if(i+1 != 11){
                if(i+1 == 10){ addAutoModule(leds.autoModuleColor(OLed.LEDColor.ORANGE)); }

                addConcurrentAutoModuleWithCancel(BackwardCycle(HIGH), 0.2);
                addWaypoint(i == 0 ? 0.5 : 0.6, x, -15+y, 0);
                addSegment(0.2, 0.5, mecanumNonstopSetPoint, x, -23+y, 0);
                addConcurrentAutoModuleWithCancel(ForwardCycle);
                addWaypoint(0.4,  x, 30+y, 0);


            } else{
                addAutoModule(leds.autoModuleColor(OLed.LEDColor.GREEN));
                addConcurrentAutoModuleWithCancel(HoldMiddle, 0.2);
                addSegment(0.6, 0.5, mecanumNonstopSetPoint, x, y, 0);
                addPause(0.05);
//                addAutoModule(RetractOdometry);
                addAutoModule(leds.autoModuleColor(OLed.LEDColor.OFF));
            }
    }};}

    Machine MachineCycle2 = new Machine()
            .addInstruction(ResetOdometry, 0.2)
            .addIndependent(11, AutoModuleUser::Cycle2)
//            .addInstruction(AutoModuleUser::enableKappa, 0.1)
    ;








    /**
     * Cycle Medium
     */
    Independent CycleMedium = new Independent() {
        @Override
        public void define() {
            startPose = new Pose(Robot.halfLength + 59, Field.width/2.0, 90);
            addWaypoint(0.5,0,0.01,0);
            addConcurrentAutoModule(ReadyStart);
            addWaypoint(0.5,0,-14,0);
            addWaypoint(0.5,-16.0, -14.0, 0.0);
            addConcurrentAutoModule(StageStart);
            addWaypoint(0.5, -31.0, 1.0, 0.0);
            addSegment(0.8, 0.7, mecanumNonstopSetPoint,  -25.0, 37.0, -21.0);
            addConcurrentAutoModuleWithCancel(BackwardCycle(MIDDLE), 0.2);
            addSegment(1.2, 0.7, mecanumNonstopSetPoint, -49.0, -14.0, -21.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle, 0.3);
            addSegment(0.8, 0.7, mecanumNonstopSetPoint,  -25.0, 37.0, -21.0);
            addConcurrentAutoModuleWithCancel(BackwardCycle(LOW), 0.2);
            addSegment(0.8, 0.7, mecanumNonstopSetPoint, -34.0, 37.0, -57.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle, 0.3);
            addWaypoint(0.5, -43.0, 43.0, -57.0 );
            addSegment(0.8, 0.7, mecanumNonstopSetPoint, -37.0, 47.5, -57.0);
            addConcurrentAutoModuleWithCancel(BackwardCycle(GROUND), 0.2);
            addWaypoint(0.5, -64.0, 55.0, -90.0);
            addWaypoint(0.5, -136.0, 58.0, -90.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle, 0.3);
        }
    };
    Machine CycleMediumMachine = new Machine()
            .addInstruction(ResetOdometry, 0.3)
            .addIndependent(CycleMedium)
    ;


}

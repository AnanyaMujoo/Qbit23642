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

import static global.Modes.AttackMode.ON_BY_DEFAULT;
import static global.Modes.AttackStatus.REST;
import static global.Modes.AttackStatus.ATTACK;
import static global.Modes.Drive.MEDIUM;
import static global.Modes.Drive.SLOW;
import static global.Modes.GameplayMode.CIRCUIT_PICK;
import static global.Modes.GameplayMode.CIRCUIT_PLACE;
import static global.Modes.GameplayMode.CYCLE;
import static global.Modes.*;
import static global.Modes.Height.GROUND;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;
import static global.Modes.Height.MIDDLE;
import static global.Modes.OuttakeStatus.DRIVING;
import static global.Modes.OuttakeStatus.PLACING;


public interface AutoModuleUser extends RobotUser{

    /**
     * Tele
     */
    AutoModule BackwardCircuitPick = new AutoModule(
            TerraOp.trackAfter.next(),
            driveMode.ChangeMode(MEDIUM),
            outtake.stageClose(0.25),
            outtake.stageReadyStart(0.4),
            gameplayMode.ChangeMode(CIRCUIT_PLACE)
    );
    AutoModule BackwardCircuitGroundPick = new AutoModule(
            TerraOp.trackAfter.next(),
            Modes.driveMode.ChangeMode(MEDIUM),
            lift.changeCutoff(2.0),
            outtake.stageClose(0.25),
            outtake.stageReadyStart(0.0),
            lift.stageLift(1.0, heightMode.getValue(GROUND)),
            gameplayMode.ChangeMode(CIRCUIT_PLACE)
    );
    AutoModule BackwardCycleGroundPick = new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            lift.changeCutoff(2.0),
            outtake.stageClose(0.25),
            lift.stageLift(1.0, heightMode.getValue(GROUND))
    );
    OutputList BackwardCircuitPickAll = new OutputList(heightMode::get)
            .addOption(HIGH, BackwardCircuitPick)
            .addOption(MIDDLE, BackwardCircuitPick)
            .addOption(LOW, BackwardCircuitPick)
            .addOption(GROUND, BackwardCircuitGroundPick);
    AutoModule BackwardCircuitGroundPlace = new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtake.stageStart(0.6),
            gameplayMode.ChangeMode(CIRCUIT_PICK),
            heightMode.ChangeMode(LOW)
    );
    OutputList BackwardCircuitPlace = new OutputList(heightMode::get).addOption(LOW, BackwardCircuitPlace(LOW)).addOption(MIDDLE, BackwardCircuitPlace(MIDDLE)).addOption(HIGH, BackwardCircuitPlace(HIGH)).addOption(GROUND, BackwardCircuitGroundPlace);
    static AutoModule BackwardCircuitPlace(Height height){ return new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtakeStatus.ChangeMode(PLACING),
            Modes.attackStatus.ChangeMode(() -> attackMode.modeIs(ON_BY_DEFAULT) ? ATTACK : REST),
            outtake.stageFlip(0.0),
            lift.stageLift(1.0, heightMode.getValue(height)).attach(outtake.stageReadyEndAfter(0.4)),
            gameplayMode.ChangeMode(CIRCUIT_PICK)
    );}
    static AutoModule BackwardHeightTele(Height height){ return new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtakeStatus.ChangeMode(PLACING),
            Modes.attackStatus.ChangeMode(() -> attackMode.modeIs(ON_BY_DEFAULT) ? ATTACK : REST),
            outtake.stageClose(0.25),
            lift.stageLift(1.0, heightMode.getValue(height)).attach(outtake.stageReadyEndAfter(height.equals(HIGH) ? 0.4 : height.equals(MIDDLE) ? 0.2 : 0.0))
    );}
    OutputList BackwardTele = new OutputList(heightMode::get).addOption(HIGH, BackwardHeightTele(HIGH)).addOption(MIDDLE, BackwardHeightTele(MIDDLE)).addOption(LOW, BackwardHeightTele(LOW)).addOption(GROUND, BackwardCycleGroundPick);
    OutputList BackwardAllTele = new OutputList(gameplayMode::get).addOption(CYCLE, BackwardTele::check).addOption(CIRCUIT_PICK, BackwardCircuitPickAll::check).addOption(CIRCUIT_PLACE, BackwardCircuitPlace::check);



    OutputList ForwardCircuitTele = new OutputList(outtakeStatus::get).addOption(DRIVING, ForwardTele(false, true, false)).addOption(PLACING, ForwardTele(false, true, true));
    OutputList ForwardCircuitTelePlace = new OutputList(outtakeStatus::get).addOption(DRIVING, ForwardTele(true, true, false)).addOption(PLACING, ForwardTele(true, true, true));
    OutputList ForwardCircuitTelePlaceAll = new OutputList(heightMode::get).addOption(HIGH, ForwardCircuitTelePlace::check).addOption(MIDDLE, ForwardCircuitTelePlace::check).addOption(LOW, ForwardCircuitTelePlace::check).addOption(GROUND, ForwardCircuitTele::check);

    OutputList ForwardTele = new OutputList(outtakeStatus::get).addOption(DRIVING, ForwardTele(false, false, false)).addOption(PLACING, ForwardTele(false, false, true));
    static AutoModule ForwardTele(boolean place, boolean circuit, boolean move) {return new AutoModule(
            Modes.driveMode.ChangeMode(circuit ? MEDIUM : SLOW),
            Modes.attackStatus.ChangeMode(REST),
            outtakeStatus.ChangeMode(DRIVING),
            RobotPart.emptyIfNot(move, outtake.stageEnd(0.0)),
            RobotPart.condition(place, outtake.stageStart(0.0), outtake.stageOpen(0.0)),
            RobotPart.emptyIfNot(place, lift.stageLift(1.0, heightMode.getValue(LOW)+8)),
            RobotPart.emptyIfNot(move, drive.moveTime(1.0, 0.0, 0.0, () -> heightMode.modeIs(LOW) ? 0.2 : 0.12)),
            lift.resetCutoff(),
            RobotPart.condition(place, outtake.stageOpen(0.0), outtake.stageStart(0.0)),
            lift.stageLift(0.8,0),
            RobotPart.emptyIfNot(circuit, gameplayMode.ChangeMode(CIRCUIT_PICK)),
            TerraOp.trackBefore.next()
    );}
    OutputList ForwardAll = new OutputList(gameplayMode::get).addOption(CYCLE, ForwardTele::check).addOption(CIRCUIT_PICK, ForwardCircuitTele::check).addOption(CIRCUIT_PLACE, ForwardCircuitTelePlaceAll::check);
    AutoModule High = new AutoModule(heightMode.ChangeMode(HIGH), attackStatus.ChangeMode(() -> attackMode.modeIs(ON_BY_DEFAULT) ? ATTACK : REST));
    AutoModule Middle = new AutoModule(heightMode.ChangeMode(MIDDLE),attackStatus.ChangeMode(() -> attackMode.modeIs(ON_BY_DEFAULT) ? ATTACK : REST));
    AutoModule Low = new AutoModule(heightMode.ChangeMode(LOW), attackStatus.ChangeMode(() -> attackMode.modeIs(ON_BY_DEFAULT) ? ATTACK : REST), lift.changeCutoff(2));
    AutoModule Ground = new AutoModule(heightMode.ChangeMode(GROUND), attackStatus.ChangeMode(() -> attackMode.modeIs(ON_BY_DEFAULT) ? ATTACK : REST), lift.changeCutoff(2));
    AutoModule LiftHigh = new AutoModule(heightMode.ChangeMode(HIGH), attackStatus.ChangeMode(() -> attackMode.modeIs(ON_BY_DEFAULT) ? ATTACK : REST), lift.stageLift(1.0, heightMode.getValue(HIGH)));
    AutoModule LiftMiddle = new AutoModule(heightMode.ChangeMode(MIDDLE),attackStatus.ChangeMode(() -> attackMode.modeIs(ON_BY_DEFAULT) ? ATTACK : REST), lift.stageLift(1.0, heightMode.getValue(MIDDLE)));
    AutoModule LiftLow = new AutoModule(heightMode.ChangeMode(LOW), attackStatus.ChangeMode(() -> attackMode.modeIs(ON_BY_DEFAULT) ? ATTACK : REST), lift.changeCutoff(2), lift.stageLift(1.0, heightMode.getValue(LOW)));
    AutoModule LiftGround = new AutoModule(heightMode.ChangeMode(GROUND), attackStatus.ChangeMode(() -> attackMode.modeIs(ON_BY_DEFAULT) ? ATTACK : REST), lift.changeCutoff(2), lift.stageLift(1.0, heightMode.getValue(GROUND)));
    AutoModule ResetLift = new AutoModule(lift.moveTime(-0.3, 0.5),  lift.resetLift() );
    AutoModule UprightCone = new AutoModule(driveMode.ChangeMode(SLOW), lift.stageLift(1.0, 15));
    AutoModule TakeOffCone = new AutoModule(outtake.stageClose(0.0), lift.stageLift(1.0, heightMode.getValue(HIGH)+3.5).attach(outtake.stageReadyStartAfter(0.5)),RobotPart.pause(0.3),outtake.stageFlip(0.0), gameplayMode.ChangeMode(() -> lift.circuitMode ? CIRCUIT_PLACE : CYCLE));

    static AutoModule ForwardStackTele(int i){return new AutoModule(
            lift.changeCutoff(2),
            outtake.stageOpen(0.0),
            outtake.stageStart(0.0),
            lift.stageLift(1.0,  i == 0 ? 14.5 : Math.max(15.0 - (i*15.0/5.0), 0))
    );}

    /**
     * Auto
     */

    AutoModule BackwardAuto2First = new AutoModule(
            lift.stageLift(1.0, heightMode.getValue(HIGH)-5.5),
            outtake.stageReadyEnd(0.32),
            outtake.stageOpen(0.0)
    );

    AutoModule BackwardAuto2 = new AutoModule(
            RobotPart.pause(0.1),
            outtake.stageReadyEnd(0.32),
            outtake.stageOpen(0.0)
    );

    AutoModule BackwardAutoReadyFirst = new AutoModule(
            outtake.stageMiddle(0.0),
            lift.changeCutoff(2),
            lift.stageLift(1.0, heightMode.getValue(LOW)+15)
    );

    AutoModule BackwardAutoReady = new AutoModule(
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)-4.5)
    );

    default AutoModule ForwardAuto2(int i){return new AutoModule(
            Reactor.forceExit(),
            outtake.stageOpen(0.0),
            lift.stageLift(1.0,  i == 0 ? 14.5 : Math.max(15.0 - (i*15.0/5.0), 0)).attach(outtake.stageStartAfter(0.15))
    );}

    AutoModule GrabAuto2 = new AutoModule(
            outtake.stageClose(0.15),
            lift.moveTime(1,0.2).attach(outtake.stageReadyStartAfter(0.1))
    );

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

    AutoModule BackwardCycle = new AutoModule(
            outtake.stageClose(0.2),
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)-2).attach(outtake.stageReadyEndAfter(0.25))
    );

    /**
     * Cycle 2
     */

    static AutoModule BackwardCycle2(Height height) {return new AutoModule(
            outtake.stageClose(0.18),
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(height)+2)
    );}


    AutoModule ForwardCycle2 = new AutoModule(

            outtake.stageEnd(0.1),
            outtake.stageOpen(0.1),
            lift.moveTime(-0.5, 0.1),
            lift.resetCutoff(),
            outtake.stageStart(0.0),
            lift.stageLift(1.0,0)
//
//
//            outtake.stageEnd(0.0),
//            outtake.stageOpen(0.0),
//            lift.moveTime(-1.0, 0.1),
//            outtake.stageMiddle(0.0),
//            lift.stageLift(1.0,  0).attach(outtake.stageStartAfter(0.05))
    );

    AutoModule StageStart = new AutoModule(outtake.stageStart(0.0));
    AutoModule ReadyStart = new AutoModule(outtake.stageReadyStart(0.0));

    AutoModule HoldMiddle = new AutoModule(outtake.stageClose(0.18), outtake.stageMiddle(0.0));

    static Independent Cycle2(int i) { return new Independent() {
            @Override
            public void define() {
            double x = i*0.0; double y = i*0.0;
            if(i+1 == 1){ addWaypoint(0.7,  x, 17+y, 0);
                addWaypoint(0.4,  x, 20+y, 0);
            }
            if(i+1 != 11){
                if(i+1 == 10){ addAutoModule(leds.autoModuleColor(OLed.LEDColor.ORANGE)); }
                addConcurrentAutoModuleWithCancel(BackwardCycle2(HIGH), 0.5);
                addSegment(1.0, 0.7, mecanumNonstopSetPoint, x, -23+y, 0);
                addConcurrentAutoModuleWithCancel(ForwardCycle2, 0.1);
                addSegment(0.8, 0.73, mecanumNonstopSetPoint, x, 8+y, 0);
                addWaypoint(0.5,  x, 25+y, 0);
            } else{
                addAutoModule(leds.autoModuleColor(OLed.LEDColor.GREEN));
                addConcurrentAutoModuleWithCancel(HoldMiddle, 0.2);
                addSegment(0.6, 0.5, mecanumNonstopSetPoint, x, y, 0);
                addPause(0.05);
                addAutoModule(new AutoModule(gameplayMode.ChangeMode(CIRCUIT_PLACE), heightMode.ChangeMode(MIDDLE), driveMode.ChangeMode(MEDIUM)));
                addAutoModule(new AutoModule(drive.stageRetract()));
                addAutoModule(leds.autoModuleColor(OLed.LEDColor.OFF));
            }
    }};}


    // TODO TEST
    Machine MachineCycle2 = new Machine()
            .addInstruction(ResetOdometry, 0.2)
            .addIndependent(11, AutoModuleUser::Cycle2)
            .addInstruction(() -> {TerraOp.setTrack(TerraOp.kappaBefore, TerraOp.kappaAfter);}, 0.1);








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
            addConcurrentAutoModuleWithCancel(BackwardCycle2(MIDDLE), 0.2);
            addSegment(1.2, 0.7, mecanumNonstopSetPoint, -49.0, -14.0, -21.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle2, 0.3);
            addSegment(0.8, 0.7, mecanumNonstopSetPoint,  -25.0, 37.0, -21.0);
            addConcurrentAutoModuleWithCancel(BackwardCycle2(LOW), 0.2);
            addSegment(0.8, 0.7, mecanumNonstopSetPoint, -34.0, 37.0, -57.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle2, 0.3);
            addWaypoint(0.5, -43.0, 43.0, -57.0 );
            addSegment(0.8, 0.7, mecanumNonstopSetPoint, -37.0, 47.5, -57.0);
            addConcurrentAutoModuleWithCancel(BackwardCycle2(GROUND), 0.2);
            addWaypoint(0.5, -64.0, 55.0, -90.0);
            addWaypoint(0.5, -136.0, 58.0, -90.0);
            addConcurrentAutoModuleWithCancel(ForwardCycle2, 0.3);
        }
    };
    Machine CycleMediumMachine = new Machine()
            .addInstruction(ResetOdometry, 0.3)
            .addIndependent(CycleMedium)
    ;








    static Stage junctionStop(){ return new Stage(new Main(() -> MecanumJunctionReactor2.stop = true), RobotPart.exitAlways()); }






    /**
     * Cycle Old
     */
    Independent Cycle = new Independent() {
        @Override
        public void define() {
            addWaypoint(0,0.01,0);
            addWaypoint(0.38, 0,27, 0);
            addConcurrentAutoModule(BackwardCycle);
            addPause(0.2);
            addWaypoint(0.38, 0, 3, 0);
            addSetpoint(1.05, 0.95,0, -4,0);
            addCancelAutoModules();
            addConcurrentAutoModule(ForwardTele(false, false,false));
            addPause(0.4);
        }
    };
    Machine CycleMachine = new Machine()
            .addInstruction(odometry::reset, 0.3)
            .addIndependent(12, AutoModuleUser.Cycle);


    AutoModule BackwardCycleMedium = new AutoModule(
            outtake.stageClose(0.2),
            lift.stageLift(1.0, heightMode.getValue(MIDDLE)+9).attach(outtake.stageEndAfter(0.4))
    );

    AutoModule ForwardMedium = new AutoModule(
            outtake.stageOpen(0.25),
            outtake.stageStart(0.0),
//            RobotPart.pause(0.1),
            lift.resetCutoff(),
            lift.stageLift(0.4, 0)
    );



    /**
     * Old Auto
     */

    AutoModule DropAutoFirst = new AutoModule(outtake.stageEnd(0.3), outtake.stageOpen(0.1));
    AutoModule DropAuto = new AutoModule(outtake.stageOpen(0.15));
    AutoModule GrabAuto = new AutoModule(
            outtake.stageClose(0.2),
            outtake.stageReadyStart(0.3).attach(drive.moveTime(-0.2, 0, 0, 0.2)),
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+5)
    );
    default AutoModule ForwardAuto(int i){return new AutoModule(
            outtake.stageStart(0.0),
            lift.stageLift(0.7, Math.max(14.0 - (i*14.0/5.0), 0))
    );}
    AutoModule BackwardAutoFirst = new AutoModule(
            outtake.stageReadyEnd(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)+7)
    );
    AutoModule BackwardAuto = new AutoModule(
            RobotPart.pause(0.1),
            outtake.stageEnd(0.0)
    );






    //        gph1.link(Button.X, () -> {odometry.reset(); bot.cancelIndependents(); bot.addIndependent(CycleAround);}, AUTOMATED)



//
//    Independent CycleAround = new Independent() { @Override public void define() {
//        addPause(0.1);
//        addScaledWaypoint(0.5, 9.5, 24.5, -35.0);
//        addScaledSetpoint(1.0, 23.5, 41.0, -58.0);
//        addAutoModule(BackwardHeightTele(HIGH));
//        addPause(0.3);
//        addScaledWaypoint(0.5, 9.5, 24.5, -35.0);
//        addAccuracyScaledSetpoint(1.0,1.0, 0.01, 0.01, 2);
//        addConcurrentAutoModule(ForwardTele);
//        addPause(0.3);
//    }}

//    Independent CycleMediumFirst = new Independent() {
//        @Override
//        public void define() {
//            addScaledWaypoint(0.6, 0.0, -6, 24.0);
//            addScaledWaypoint(1.0, 18.0, -6, 24.0);
//            addScaledWaypoint(1.0, 29.0, -8.5, 24.0);
//            addAccuracySetpoint(0.3, 42, -6.0, 26.0);
//        }
//    };

//      .addInstruction(odometry::reset, 0.05)
//            .addIndependent(CycleMediumFirst)




//
//    /**
//     * Cycle Around
//     */
//
//
//    AutoModule ForwardAutoCycleAround = new AutoModule(outtake.stageStart(0.0), lift.stageLift(0.4, 0));
//    AutoModule CloseAutoCycleAround = new AutoModule(outtake.stageClose(0.2));
//    Independent MoveToJunction = new Independent() { @Override public void define() { addCustomSegment(mecanumJunctionSetpoint, 0.0, 0.0, 0.0); }};
//    Independent CycleAroundFirst = new Independent() {
//        @Override
//        public void define() {
//            addWaypoint(0.01,0.01,0.01);
//            addScaledSetpoint(1.0, 11.5, 32.5, -52.0);
//            addPause(0.5);
//            addScaledSetpoint(1.0, 20.5, 38.5, -52.0);
//            addAutoModule(new AutoModule(outtake.stageClose(0.2)));
//            addScaledWaypoint(0.8, 9.5, 24.5, -35.0);
//            addConcurrentAutoModule(BackwardAuto);
//            addAccuracyScaledSetpoint(1.0,1.0, 0.01, 0.01, 2);
//            addPause(0.2);
//            addAutoModule(DropAuto);
//            addConcurrentAutoModule(ForwardAutoCycleAround);
//            addPause(0.4);
//        }
//    };
//    Independent CycleAround = new Independent() { @Override public void define() {
//        addPause(0.1);
//        addScaledWaypoint(0.6, 9.5, 24.5, -52.0);
//        addScaledSetpoint(1.0, 20.5, 38.5, -52.0);
//        addAutoModule(CloseAutoCycleAround);
//        addScaledWaypoint(0.8, 9.5, 24.5, -35.0);
//        addConcurrentAutoModule(BackwardAuto);
//        addAccuracyScaledSetpoint(1.0,1.0, 0.01, 0.01, 2);
//        addPause(0.2);
//        addAutoModule(DropAuto);
//        addConcurrentAutoModule(ForwardAutoCycleAround);
//        addPause(0.4);
//    }};
//    Machine CycleAroundMachine = new Machine()
//            .addIndependent(MoveToJunction)
//            .addInstruction(odometry::reset, 0.1)
//            .addIndependent(CycleAroundFirst)
//            .addIndependent(9, CycleAround)
//    ;


}

package automodules;

import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.reactors.MecanumJunctionReactor2;
import global.Modes;
import robot.RobotUser;
import robotparts.RobotPart;
import teleutil.independent.Independent;
import teleutil.independent.Machine;
import util.condition.OutputList;

import static global.Modes.AttackMode.NORMAL;
import static global.Modes.AttackMode.STICKY;
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


public interface AutoModuleUser extends RobotUser{

    /**
     * Tele
     */
    AutoModule BackwardCircuitPick = new AutoModule(
            driveMode.ChangeMode(MEDIUM),
            outtake.stageClose(0.2),
            outtake.stageReadyStart(0.4),
            gameplayMode.ChangeMode(CIRCUIT_PLACE)
    );
    AutoModule BackwardCircuitGroundPick = new AutoModule(
            Modes.driveMode.ChangeMode(MEDIUM),
            lift.changeCutoff(2.0),
            outtake.stageClose(0.2),
            outtake.stageReadyStart(0.0),
            lift.stageLift(1.0, heightMode.getValue(GROUND)),
            gameplayMode.ChangeMode(CIRCUIT_PLACE)
    );
    AutoModule UprightCone = new AutoModule(
            driveMode.ChangeMode(SLOW),
            lift.stageLift(1.0, 12)
    );
    AutoModule BackwardCycleGroundPick = new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            lift.changeCutoff(2.0),
            outtake.stageClose(0.2),
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
            gameplayMode.ChangeMode(CIRCUIT_PICK)
    );
    OutputList BackwardCircuitPlace = new OutputList(heightMode::get)
            .addOption(LOW, BackwardCircuitPlace(LOW))
            .addOption(MIDDLE, BackwardCircuitPlace(MIDDLE))
            .addOption(HIGH, BackwardCircuitPlace(HIGH))
            .addOption(GROUND, BackwardCircuitGroundPlace);
    static AutoModule BackwardCircuitPlace(Height height){ return new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            Modes.attackMode.ChangeMode(STICKY),
            outtake.stageFlip(0.0),
            lift.stageLift(1.0, heightMode.getValue(height)).attach(outtake.stageReadyEndAfter(0.1)),
            gameplayMode.ChangeMode(CIRCUIT_PICK)
    );}
    static AutoModule BackwardHeightTele(Height height){ return new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            Modes.attackMode.ChangeMode(STICKY),
            outtake.stageClose(0.18),
            lift.stageLift(1.0, heightMode.getValue(height)).attach(outtake.stageReadyEndAfter(0.1))
    );}
    OutputList BackwardTele = new OutputList(heightMode::get)
            .addOption(HIGH, BackwardHeightTele(HIGH))
             .addOption(MIDDLE, BackwardHeightTele(MIDDLE))
            .addOption(LOW, BackwardHeightTele(LOW))
            .addOption(GROUND, BackwardCycleGroundPick);
    OutputList BackwardAllTele = new OutputList(gameplayMode::get)
            .addOption(CYCLE, BackwardTele::check)
            .addOption(CIRCUIT_PICK, BackwardCircuitPickAll::check)
            .addOption(CIRCUIT_PLACE, BackwardCircuitPlace::check);
    AutoModule ForwardCircuitTele = new AutoModule(
            Modes.driveMode.ChangeMode(MEDIUM),
            Modes.attackMode.ChangeMode(NORMAL),
            outtake.stageOpen(0.25),
            outtake.stageStart(0.0),
            lift.resetCutoff(),
            lift.stageLift(0.7, 0)
    );
    AutoModule ForwardTele = new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            Modes.attackMode.ChangeMode(NORMAL),
            outtake.stageOpen(0.25),
            outtake.stageStart(0.0),
            lift.resetCutoff(),
            lift.stageLift(0.7, 0)
    );
    OutputList ForwardAll = new OutputList(gameplayMode::get)
            .addOption(CYCLE, ForwardTele)
            .addOption(CIRCUIT_PICK, ForwardCircuitTele)
            .addOption(CIRCUIT_PICK, ForwardCircuitTele);
    AutoModule LiftHigh = new AutoModule(heightMode.ChangeMode(HIGH), lift.stageLift(1.0, heightMode.getValue(HIGH)));
    AutoModule LiftMiddle = new AutoModule(heightMode.ChangeMode(MIDDLE), lift.stageLift(1.0, heightMode.getValue(MIDDLE)));
    AutoModule LiftLow = new AutoModule(heightMode.ChangeMode(LOW), lift.changeCutoff(2), lift.stageLift(1.0, heightMode.getValue(LOW)));
    AutoModule LiftGround = new AutoModule(heightMode.ChangeMode(GROUND), lift.changeCutoff(2), lift.stageLift(1.0, heightMode.getValue(GROUND)));

    /**
     * Auto
     */

    AutoModule BackwardAuto2First = new AutoModule(
            lift.stageLift(1.0, heightMode.getValue(HIGH)-5.5),
            outtake.stageReadyEnd(0.32),
            outtake.stageOpen(0.0),
            junctionStop()
    );

    AutoModule BackwardAuto2 = new AutoModule(
            RobotPart.pause(0.1),
            outtake.stageReadyEnd(0.32),
            outtake.stageOpen(0.0),
            junctionStop()
    );

    default AutoModule ForwardAuto2(int i){return new AutoModule(
            outtake.stageOpen(0.0),
            lift.stageLift(1.0,  i == 0 ? 14.5 : Math.max(15.0 - (i*15.0/5.0), 0)).attach(outtake.stageStartAfter(0.15))
    );}

    AutoModule GrabAuto2 = new AutoModule(
            outtake.stageClose(0.15),
            lift.moveTime(1,0.2).attach(outtake.stageReadyStartAfter(0.1))
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

    AutoModule BackwardAutoReadyFirst = new AutoModule(
            outtake.stageMiddle(0.0),
            lift.changeCutoff(2),
            lift.stageLift(1.0, heightMode.getValue(LOW)+15)
    );

    AutoModule BackwardAutoReady = new AutoModule(
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)-4.5)
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

    AutoModule BackwardCycle = new AutoModule(
            outtake.stageClose(0.2),
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, heightMode.getValue(HIGH)-2).attach(outtake.stageReadyEndAfter(0.25))
    );

    /**
     * Cycle
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
            addConcurrentAutoModule(ForwardTele);
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
     * Cycle Medium
     */
    Independent CycleMedium = new Independent() {
        @Override
        public void define() {
            addWaypoint(0.5, 0.0, 30.0, 0.0);
            addWaypoint(0.3, 0.0, 50, 0.0);
            addConcurrentAutoModule(BackwardCycleMedium);
            addPause(0.3);
            addWaypoint(0.4, 0.0, 19.0, 0.0);
            addSetpoint(0.7, 1.1,0, 3,0);
            addCancelAutoModules();
            addConcurrentAutoModule(ForwardMedium);
            addPause(0.5);
        }
    };
    Machine CycleMediumMachine = new Machine()
            .addInstruction(odometry::reset, 0.3)
            .addIndependent(8, CycleMedium)
    ;


    AutoModule ResetLift = new AutoModule(
            lift.moveTime(-0.3, 0.5),
            lift.resetLift()
    );



    static Stage junctionStop(){ return new Stage(new Main(() -> MecanumJunctionReactor2.stop = true), RobotPart.exitAlways()); }










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

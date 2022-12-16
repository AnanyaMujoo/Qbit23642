package automodules;

import android.view.Display;

import org.omg.CORBA.INITIALIZE;

import auton.Auto;
import global.Modes;
import robot.RobotUser;
import robotparts.RobotPart;
import teleutil.independent.Independent;
import teleutil.independent.Machine;
import util.condition.OutputList;

import static global.Modes.DriveMode.Drive.MEDIUM;
import static global.Modes.DriveMode.Drive.SLOW;
import static global.Modes.HeightMode.Height.*;


public interface AutoModuleUser extends RobotUser{

    /**
     * Tele
     */
    AutoModule BackwardCircuitPick = new AutoModule(
            Modes.driveMode.ChangeMode(MEDIUM),
            outtake.stageClose(0.2),
            outtake.stageReadyStart(0.4),
            Modes.ChangeGameplayMode(Modes.GameplayMode.CIRCUIT_PLACE)
    );
    AutoModule BackwardCycleGround = new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtake.stageClose(0.2),
            lift.stageLift(0.8, GROUND.getValue())
    );
    AutoModule BackwardCircuitGround = new AutoModule(
            outtake.stageStart(0.3),
            RobotPart.pause(0.5),
            outtake.stageOpen(0.2),
            Modes.ChangeGameplayMode(Modes.GameplayMode.CIRCUIT_PICK)
    );
    OutputList BackwardCircuitPlace = new OutputList(Modes.heightMode::get)
            .addOption(LOW, BackwardCircuitPlace(LOW))
            .addOption(MIDDLE, BackwardCircuitPlace(MIDDLE))
            .addOption(HIGH, BackwardCircuitPlace(HIGH))
            .addOption(GROUND, BackwardCircuitGround);
    static AutoModule BackwardCircuitPlace(Modes.HeightMode.Height height){ return new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtake.stageFlip(0.0),
            lift.stageLift(1.0, height.getValue()).attach(outtake.stageReadyEndAfter(0.1)),
            Modes.ChangeGameplayMode(Modes.GameplayMode.CIRCUIT_PICK)
    );}
    static AutoModule BackwardHeightTele(Modes.HeightMode.Height height){ return new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtake.stageClose(0.2),
            lift.stageLift(1.0, height.getValue()).attach(outtake.stageReadyEndAfter(0.1))
    );}
    OutputList BackwardTele = new OutputList(Modes.heightMode::get)
            .addOption(HIGH, BackwardHeightTele(HIGH))
             .addOption(MIDDLE, BackwardHeightTele(MIDDLE))
            .addOption(LOW, BackwardHeightTele(LOW))
            .addOption(GROUND, BackwardCycleGround);
    OutputList BackwardAllTele = new OutputList(() -> Modes.gameplayMode)
            .addOption(Modes.GameplayMode.CYCLE, BackwardTele::check)
            .addOption(Modes.GameplayMode.CIRCUIT_PICK, BackwardCircuitPick)
            .addOption(Modes.GameplayMode.CIRCUIT_PLACE, BackwardCircuitPlace::check);






    AutoModule ForwardTele = new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtake.stageOpen(0.25),
            outtake.stageStart(0.0),
            lift.stageLift(0.4, 0)
    );
    default AutoModule ForwardStackTele(int i){return new AutoModule(
            lift.changeCutoff(2),
            outtake.stageOpen(0.0),
            outtake.stageStart(0.0),
            lift.stageLift(0.7, Math.max(14.0 - (i*14.0/4.0), 0)),
            lift.changeCutoff(6)
    );}

    /**
     * Auto
     */

    AutoModule DropAutoFirst = new AutoModule(outtake.stageEnd(0.3), outtake.stageOpen(0.1));
    AutoModule DropAuto = new AutoModule(outtake.stageOpen(0.15));
    AutoModule GrabAuto = new AutoModule(
            outtake.stageClose(0.2),
            outtake.stageReadyStart(0.3).attach(drive.moveTime(-0.2, 0, 0, 0.2)),
            outtake.stageMiddle(0.0),
            lift.stageLift(0.8, HIGH.getValue()+5)
    );
    default AutoModule ForwardAuto(int i){return new AutoModule(
            outtake.stageStart(0.0),
            lift.stageLift(0.5, Math.max(14.0 - (i*14.0/5.0), 0))
    );}
    AutoModule BackwardAutoFirst = new AutoModule(
            outtake.stageReadyEnd(0.0),
            lift.stageLift(0.8, HIGH.getValue()+7)
    );
    AutoModule BackwardAuto = new AutoModule(
            RobotPart.pause(0.1),
            outtake.stageEnd(0.0)
    );



    /**
     * Misc
     */
    Independent MoveToZero = new Independent() { @Override public void define() {addSetpoint(0.0, 0.01, 0.0); }};
    Independent MoveToCycleStart = new Independent() {
        @Override
        public void define() {
            addScaledWaypoint(0.8, 65,-71,0);
            addScaledWaypoint(0.4, 93,-72, 0);
            addScaledWaypoint(0.35,  89.0, -62.0, 0.0 );
        }
    };

    AutoModule BackwardCycle = new AutoModule(
            outtake.stageClose(0.2),
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, HIGH.getValue()-2).attach(outtake.stageReadyEndAfter(0.2))
    );

    /**
     * Cycle
     */
    Independent Cycle = new Independent() {
        @Override
        public void define() {
            addWaypoint(0,0.01,0);
            addScaledWaypoint(0.38, 0,27, 0);
            addConcurrentAutoModule(BackwardCycle);
            addPause(0.2);
            addScaledWaypoint(0.4, 0, 3, 0);
            addAccuracyScaledSetpoint(1.0, 0.9,0, 0.01,0);
            addCancelAutoModules();
            addConcurrentAutoModule(ForwardTele);
            addPause(0.4);
        }
    };
    Machine CycleMachine = new Machine()
            .addInstruction(odometry::reset, 0.05)
            .addIndependent(12, AutoModuleUser.Cycle);


    AutoModule BackwardCycleMedium = new AutoModule(
            outtake.stageClose(0.2),
            outtake.stageMiddle(0.0),
            lift.stageLift(1.0, MIDDLE.getValue()+9).attach(outtake.stageEndAfter(0.6))
    );

    /**
     * Cycle Medium
     */
    Independent CycleMedium = new Independent() {
        @Override
        public void define() {
            addScaledWaypoint(0.5, 0.0, 30.0, 0.0);
            addScaledWaypoint(0.3, 0.0, 50, 0.0);
            addConcurrentAutoModule(BackwardCycleMedium);
            addPause(0.2);
            addScaledWaypoint(0.6, 0.0, 19.0, 0.0);
            addAccuracyScaledSetpoint(0.7, 1.1,0, 0.01,0);
            addCancelAutoModules();
            addConcurrentAutoModule(ForwardTele);
            addPause(0.4);
        }
    };
    Machine CycleMediumMachine = new Machine()
            .addInstruction(odometry::reset, 0.05)
            .addIndependent(8, CycleMedium)
    ;











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

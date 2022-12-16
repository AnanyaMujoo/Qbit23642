package automodules;

import org.omg.CORBA.INITIALIZE;

import global.Modes;
import robot.RobotUser;
import robotparts.RobotPart;
import teleutil.independent.Independent;
import teleutil.independent.Machine;
import util.condition.OutputList;

import static global.Modes.DriveMode.Drive.SLOW;
import static global.Modes.HeightMode.Height.*;


public interface AutoModuleUser extends RobotUser{

    /**
     * Tele
     */
    OutputList BackwardAllTele = new OutputList(Modes.heightMode::get)
            .addOption(LOW, BackwardHeightTele(LOW))
            .addOption(MIDDLE, BackwardHeightTele(MIDDLE))
            .addOption(HIGH, BackwardHeightTele(HIGH));
    static AutoModule BackwardHeightTele(Modes.HeightMode.Height height){ return new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtake.stageClose(0.2),
            lift.stageLift(1.0, height.getValue()-8).attach(outtake.stageReadyEndAfter(0.1))
    );}
    AutoModule ForwardTele = new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtake.stageOpen(0.2),
            outtake.stageStart(0.0),
            lift.stageLift(0.4, 0)
    );
    default AutoModule ForwardStackTele(int i){return new AutoModule(
            outtake.stageOpen(0.0),
            outtake.stageStart(0.0),
            lift.stageLift(0.7, Math.max(14.0 - (i*14.0/4.0), 0))
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
            lift.stageLift(0.8, HIGH.getValue()-3)
    );
    default AutoModule ForwardAuto(int i){return new AutoModule(
            outtake.stageStart(0.0),
            lift.stageLift(0.5, Math.max(14.0 - (i*14.0/5.0), 0))
    );}
    AutoModule BackwardAutoFirst = new AutoModule(
            outtake.stageReadyEnd(0.0),
            lift.stageLift(0.8, HIGH.getValue()-1)
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
            addScaledWaypoint(0.35,  89, -47.5, 0);
        }
    };


    // TODO TEST FOR UPPER

    AutoModule BackwardCycle = new AutoModule(
            outtake.stageClose(0.2),
            outtake.stageMiddle(0.2),
            lift.stageLift(1.0, HIGH.getValue()-8)
    );
    AutoModule BackwardCycle2 = new AutoModule(
            outtake.stageReadyEnd(0.3)
    );

    /**
     * Cycle
     */
    Independent CycleFirst = new Independent() {
        @Override
        public void define() {
            addWaypoint(0.0, 0.01, 0.0);
            addAccuracySetpoint(0.5, 0, -11.5,0);
            addAutoModule(new AutoModule(outtake.stageStart(0.0)));
        }
    };
    Independent Cycle = new Independent() {
        @Override
        public void define() {
            addWaypoint(0,0.01,0);
            addScaledWaypoint(0.6, 0,11.5, 0);
            addAccuracySetpoint(1.5, 0, 21.4, 0);
            addConcurrentAutoModule(BackwardCycle);
            addPause(0.3);
            addScaledWaypoint(0.45, 0, 3, 0);
            addConcurrentAutoModule(BackwardCycle2);
            addAccuracySetpoint(0.65, 0, 0.01,0);
            addConcurrentAutoModule(ForwardTele);
            addPause(0.4);
        }
    };
    Machine CycleMachine = new Machine()
            .addInstruction(odometry::reset, 0.01)
            .addIndependent(CycleFirst)
            .addInstruction(odometry::reset, 0.01)
            .addIndependent(10, AutoModuleUser.Cycle);


    /**
     * Cycle Medium
     */
    Independent CycleMediumFirst = new Independent() {
        @Override
        public void define() {
            addWaypoint(0,0,0);
            addScaledWaypoint(0.8, 0.0, -10, 0.0);
            addScaledWaypoint(0.6, -18.0, -9.5, -12.0);
            addScaledWaypoint(0.6, -29.0, -8.5, -24.0);
            addAccuracySetpoint(0.5, -42.5, -4.0, -24.0);
        }
    };
    Independent CycleMedium = new Independent() {
        @Override
        public void define() {
            addWaypoint(0,0,0);
            addScaledWaypoint(0.8, 0.0, 36.0, 0.0);
            addSetpoint(0.5, 45.0, -3.0);
            addScaledWaypoint(0.8, 0.0, 12.0, 0.0);
            addSetpoint(0,0,0);
        }
    };
    Machine CycleMediumMachine = new Machine()
            .addInstruction(odometry::reset, 0.01)
            .addIndependent(CycleMediumFirst)
            .addInstruction(odometry::reset, 0.01)
            .addIndependent(6, CycleMedium);



    Independent CycleAround = new Independent() { @Override public void define() {
        addPause(0.1);
        addScaledWaypoint(0.5, 9.5, 24.5, -35.0);
        addScaledSetpoint(1.0, 23.5, 41.0, -58.0);
        addAutoModule(BackwardHeightTele(HIGH));
        addPause(0.3);
        addScaledWaypoint(0.5, 9.5, 24.5, -35.0);
        addAccuracyScaledSetpoint(1.0,1.0, 0.01, 0.01, 2);
        addConcurrentAutoModule(ForwardTele);
        addPause(0.3);
    }};

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

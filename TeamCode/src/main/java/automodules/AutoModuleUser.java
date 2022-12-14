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
            outtake.stageFlip(0.0),
            outtake.stageReadyEnd(0.5),
            lift.stageLift(1.0, height.getValue()),
            outtake.stageEnd(0.2)
    );}
    AutoModule ForwardTele = new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtake.stageOpen(0.2),
            outtake.stageStart(0.0),
            lift.stageLift(0.6, 0)
    );

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
    // TODO TEST
    Independent MoveToCycleStart = new Independent() {
        @Override
        public void define() {
            addScaledWaypoint(0.8, 65,-71,0);
            addScaledWaypoint(0.4, 93,-72, 0);
            addScaledWaypoint(0.35,  89, -47.5, 0);
        }
    };




    /**
     * Cycle
     */

    AutoModule CloseAutoCycle = new AutoModule(outtake.stageClose(0.2), outtake.stageEnd(0.0));
    AutoModule ForwardAutoCycle = new AutoModule( outtake.stageStart(0.0),  lift.stageLift(0.9, 0));
    Independent CycleFirst = new Independent() {
        @Override
        public void define() {
            addWaypoint(0.0, 0.01, 0.0);
            addAccuracyScaledSetpoint(1.5, 1.2, 0, 10, 0);
            addAutoModule(CloseAutoCycle);
            addScaledWaypoint(0.6, 0, -5, 0);
            addConcurrentAutoModule(BackwardAuto);
            addSetpoint(0, -9.5,0);
            addPause(0.4);
            addAutoModule(DropAuto);
            addConcurrentAutoModule(ForwardAutoCycle);
            addPause(0.3);
        }
    };
    Independent Cycle = new Independent() {
        @Override
        public void define() {
            addWaypoint(0.0,-11.5,0.0);
            addAccuracyScaledSetpoint(1.5, 1.5, 0, 10, 0);
            addAutoModule(CloseAutoCycle);
            addScaledWaypoint(0.8, 0, -5, 0);
            addConcurrentAutoModule(BackwardAuto);
            addAccuracySetpoint(0.8, 0, -9.5,0);
            addPause(0.2);
            addAutoModule(DropAuto);
            addConcurrentAutoModule(ForwardAutoCycle);
            addPause(0.3);
        }
    };
    Machine CycleMachine = new Machine().addInstruction(odometry::reset, 0.1).addIndependent(CycleFirst).addIndependent(9, AutoModuleUser.Cycle);


    /**
     * Cycle Medium
     */
    // TODO MAKE MEDIUM JUNCTION
    AutoModule CloseMediumCycle = new AutoModule(outtake.stageClose(0.2), outtake.stageEnd(0.0));
    AutoModule ForwardMediumCycle = new AutoModule( outtake.stageStart(0.0),  lift.stageLift(0.9, 0));
    Independent CycleMediumFirst = new Independent() {
        @Override
        public void define() {
            addWaypoint(0.0, 0.01, 0.0);
            addAccuracyScaledSetpoint(1.5, 1.2, 0, 10, 0);
            addAutoModule(CloseAutoCycle);
            addScaledWaypoint(0.6, 0, -5, 0);
            addConcurrentAutoModule(BackwardAuto);
            addSetpoint(0, -9.5,0);
            addPause(0.4);
            addAutoModule(DropAuto);
            addConcurrentAutoModule(ForwardAutoCycle);
            addPause(0.3);
        }
    };
    Independent CycleMedium = new Independent() {
        @Override
        public void define() {
            addWaypoint(0.0,-11.5,0.0);
            addAccuracyScaledSetpoint(1.5, 1.5, 0, 10, 0);
            addAutoModule(CloseAutoCycle);
            addScaledWaypoint(0.8, 0, -5, 0);
            addConcurrentAutoModule(BackwardAuto);
            addAccuracySetpoint(0.8, 0, -9.5,0);
            addPause(0.2);
            addAutoModule(DropAuto);
            addConcurrentAutoModule(ForwardAutoCycle);
            addPause(0.3);
        }
    };
    Machine CycleMediumMachine = new Machine().addInstruction(odometry::reset, 0.1).addIndependent(CycleFirst).addIndependent(9, AutoModuleUser.Cycle);





    /**
     * Cycle Around
     */


    AutoModule ForwardAutoCycleAround = new AutoModule(outtake.stageStart(0.0), lift.stageLift(0.4, 0));
    AutoModule CloseAutoCycleAround = new AutoModule(outtake.stageClose(0.2));
    Independent MoveToJunction = new Independent() { @Override public void define() { addCustomSegment(mecanumJunctionSetpoint, 0.0, 0.0, 0.0); }};
    Independent CycleAroundFirst = new Independent() {
        @Override
        public void define() {
            addWaypoint(0.01,0.01,0.01);
            addScaledSetpoint(1.0, 11.5, 32.5, -52.0);
            addPause(0.5);
            addScaledSetpoint(1.0, 20.5, 38.5, -52.0);
            addAutoModule(new AutoModule(outtake.stageClose(0.2)));
            addScaledWaypoint(0.8, 9.5, 24.5, -35.0);
            addConcurrentAutoModule(BackwardAuto);
            addAccuracyScaledSetpoint(1.0,1.0, 0.01, 0.01, 2);
            addPause(0.2);
            addAutoModule(DropAuto);
            addConcurrentAutoModule(ForwardAutoCycleAround);
            addPause(0.4);
        }
    };
    Independent CycleAround = new Independent() { @Override public void define() {
        addPause(0.1);
        addScaledWaypoint(0.6, 9.5, 24.5, -52.0);
        addScaledSetpoint(1.0, 20.5, 38.5, -52.0);
        addAutoModule(CloseAutoCycleAround);
        addScaledWaypoint(0.8, 9.5, 24.5, -35.0);
        addConcurrentAutoModule(BackwardAuto);
        addAccuracyScaledSetpoint(1.0,1.0, 0.01, 0.01, 2);
        addPause(0.2);
        addAutoModule(DropAuto);
        addConcurrentAutoModule(ForwardAutoCycleAround);
        addPause(0.4);
    }};
    Machine CycleAroundMachine = new Machine()
            .addIndependent(MoveToJunction)
            .addInstruction(odometry::reset, 0.1)
            .addIndependent(CycleAroundFirst)
            .addIndependent(9, CycleAround)
    ;


    // TODO MAKE CIRCUIT
    Machine CircuitMachine = new Machine()
            .addIndependent(new Independent() {
                @Override
                public void define() {

                }
            }).addIndependent(new Independent() {
                @Override
                public void define() {

                }
            })
    ;





    // TODO MAKE PARK
    Independent ParkClose = new Independent() {
        @Override
        public void define() {

        }
    };

    Independent ParkFar = new Independent() {
        @Override
        public void define() {

        }
    };


}

package automodules;

import org.checkerframework.checker.units.qual.A;

import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.AutoSegment;
import elements.FieldSide;
import elements.Level;
import global.Modes;
import robot.RobotUser;
import robotparts.RobotPart;
import robotparts.hardware.Lift;
import teleutil.independent.Independent;
import teleutil.independent.Machine;
import unused.auto.AutoModuleUserOld;
import util.condition.DecisionList;
import util.condition.OutputList;

import static global.General.bot;
import static global.General.fieldSide;
import static global.Modes.DriveMode.Drive.MEDIUM;
import static global.Modes.DriveMode.Drive.SLOW;
import static global.Modes.HeightMode.Height.*;
import static global.Modes.OuttakeMode.ALLIANCE;
import static global.Modes.OuttakeMode.SHARED;



public interface AutoModuleUser extends RobotUser{

    AutoModule DropAuto = new AutoModule(outtake.stageOpen(0.2));
    AutoModule GrabAuto = new AutoModule(outtake.stageClose(0.3),  outtake.stageReadyStart(0.1), lift.stageLift(1.0, 35));

    default AutoModule ForwardAuto(int i){return new AutoModule(
            outtake.stageStart(0.0),
            lift.stageLift(0.6, Math.max(18.0 - (i*3.6), 0)),
            outtake.stageStart(0.0), outtake.stageOpen(0.0)
    );}


    AutoModule BackwardAuto = new AutoModule(
            outtake.stageEnd(0.0),
            lift.stageLift(0.9, HIGH.getValue())
    );







    OutputList BackwardAll = new OutputList(Modes.heightMode::get)
            .addOption(LOW, BackwardHeight(LOW))
            .addOption(MIDDLE, BackwardHeight(MIDDLE))
            .addOption(HIGH, BackwardHeight(HIGH));


    static AutoModule BackwardHeight(Modes.HeightMode.Height height){ return new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtake.stageClose(0.2),
            outtake.stageFlip(0.0),
            outtake.stageMiddle(0.5),
            lift.stageLift(1.0, height.getValue()),
            outtake.stageEnd(0.2)
//            outtake.stageEndContinuous(0.3),
//            outtake.stageEnd(0.2),
//            lift.stageLift(1.0, height.getValue()).attach(outtake.stageEndAfter(0.5)),
    );}

    AutoModule Forward = new AutoModule(
            Modes.driveMode.ChangeMode(SLOW),
            outtake.stageOpen(0.2),
            outtake.stageStart(0.0),
            lift.stageLift(0.6, 0)
    );

    AutoModule ForwardAuto = new AutoModule(
            outtake.stageStart(0.0),
            lift.stageLift(0.4, 0)
    );


    Independent MoveToPosition = new Independent() { @Override public void define() {
        addAccuracySetpoint(1.0, 0,0,0);
    }};

    Independent MoveToJunction = new Independent() { @Override public void define() {
        addCustomSegment(mecanumJunctionSetpoint, 0.0, 0.0, 0.0);
    }};







    Independent Cycle1st = new Independent() {
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
            addConcurrentAutoModule(ForwardAuto);
            addPause(0.4);
        }
    };


    static Independent Cycle(int i){return new Independent() { @Override public void define() {
        addPause(0.1);
        addScaledWaypoint(0.6, 9.5, 24.5, -52.0);
        addScaledSetpoint(1.0, 20.5, 38.5, -52.0);
        addAutoModule(CloseAuto);
        addScaledWaypoint(0.8, 9.5, 24.5, -35.0);
        addConcurrentAutoModule(BackwardAuto);
        addAccuracyScaledSetpoint(1.0,1.0, 0.01, 0.01, 2);
        addPause(0.2);
        addAutoModule(DropAuto);
        addConcurrentAutoModule(ForwardAuto);
        addPause(0.4);
    }};}

    Machine ScanAndCycle = new Machine()
            .addIndependent(MoveToJunction)
            .addInstruction(odometry::reset, 0.1)
            .addIndependent(Cycle1st)
            .addIndependent(9, AutoModuleUser::Cycle)
    ;


    AutoModule CloseAuto2 = new AutoModule(outtake.stageClose(0.2), outtake.stageEnd(0.0));
    AutoModule CloseAuto = new AutoModule(outtake.stageClose(0.2));
    AutoModule ForwardAuto2 = new AutoModule( outtake.stageStart(0.0),  lift.stageLift(0.9, 0));


    Independent CycleTwo1 = new Independent() {
        @Override
        public void define() {
            addWaypoint(0.0, 0.01, 0.0);
            addAccuracyScaledSetpoint(1.5, 1.2, 0, 10, 0);
            addAutoModule(CloseAuto2);
            addScaledWaypoint(0.6, 0, -5, 0);
            addConcurrentAutoModule(BackwardAuto);
            addSetpoint(0, -9.5,0);
            addPause(0.4);
            addAutoModule(DropAuto);
            addConcurrentAutoModule(ForwardAuto2);
            addPause(0.3);
        }
    };

    Independent CycleTwo2 = new Independent() {
        @Override
        public void define() {
            addWaypoint(0.0,-11.5,0.0);
            addAccuracyScaledSetpoint(1.5, 1.5, 0, 10, 0);
            addAutoModule(CloseAuto2);
            addScaledWaypoint(0.8, 0, -5, 0);
            addConcurrentAutoModule(BackwardAuto);
            addAccuracySetpoint(0.8, 0, -9.5,0);
            addPause(0.2);
            addAutoModule(DropAuto);
            addConcurrentAutoModule(ForwardAuto2);
            addPause(0.3);
        }
    };

    Machine CycleTwo = new Machine().addInstruction(odometry::reset, 0.1).addIndependent(CycleTwo1).addIndependent(9, AutoModuleUser.CycleTwo2);

}

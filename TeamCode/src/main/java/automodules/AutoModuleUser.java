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

    AutoModule Backward = BackwardHeight(HIGH);
    AutoModule DropAuto = DropAuto(0.5);
    AutoModule GrabAuto = GrabAuto(0.5);

    OutputList BackwardAll = new OutputList(Modes.heightMode::get)
            .addOption(LOW, BackwardHeight(LOW))
            .addOption(MIDDLE, BackwardHeight(MIDDLE))
            .addOption(HIGH, BackwardHeight(HIGH));


    static AutoModule BackwardHeight(Modes.HeightMode.Height height){ return new AutoModule(
            Modes.driveMode.ChangeMode(MEDIUM),
            outtake.stageClose(0.3),
            outtake.stageEnd(0.0),
            lift.stageLift(1.0, height.getValue()),
            Modes.driveMode.ChangeMode(SLOW)
    );}

    AutoModule Forward = new AutoModule(
            Modes.driveMode.ChangeMode(MEDIUM),
            outtake.stageOpen(0.2),
            outtake.stageStart(0.0),
            lift.stageLift(0.7, 0),
            Modes.driveMode.ChangeMode(SLOW)
    );


    default AutoModule ForwardAuto(int i){return new AutoModule(
            outtake.stageStart(0.0),
            lift.stageLift(0.7, Math.max(18.0 - (i*3.6), 0)),
            outtake.stageStart(0.0), outtake.stageOpen(0.0)
    );}


    AutoModule BackwardAuto = new AutoModule(
            outtake.stageEnd(0.0),
            lift.stageLift(0.9, HIGH.getValue()-1)
    );

    static AutoModule DropAuto(double time){ return new AutoModule(outtake.stageOpen(time)); }
    static AutoModule GrabAuto(double time){ return new AutoModule(outtake.stageClose(time),  outtake.stageReadyStart(0.0), lift.stageLift(0.8, 35)); }


    Independent MoveForward = new Independent() { @Override public void define() {
//        addSetpoint(0, 0, 0);
        addConcurrentAutoModule(Backward);
//        addWaypoint(0,0,0);
        addAccuracySetpoint(2.0, 0,0,0);
    }};


}

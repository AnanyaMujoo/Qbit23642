package automodules;

import org.checkerframework.checker.units.qual.A;

import automodules.stage.Main;
import automodules.stage.Stage;
import elements.FieldSide;
import elements.Level;
import global.Modes;
import robot.RobotUser;
import robotparts.RobotPart;
import robotparts.hardware.Lift;
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

    OutputList BackwardAll = new OutputList(Modes.heightMode::get)
            .addOption(LOW, BackwardHeight(LOW))
            .addOption(MIDDLE, BackwardHeight(MIDDLE))
            .addOption(HIGH, BackwardHeight(HIGH));


    static AutoModule BackwardHeight(Modes.HeightMode.Height height){ return new AutoModule(
            Modes.driveMode.ChangeMode(MEDIUM),
            outtake.stageClose(0.3),
            outtake.stageEnd().attach(lift.stageLift(1.0, height.getValue())),
            outtake.stageEnd(0.0),
            Modes.driveMode.ChangeMode(SLOW)
    );}

    AutoModule Forward = new AutoModule(
            Modes.driveMode.ChangeMode(MEDIUM),
            outtake.stageOpen(0.2),
            outtake.stageStart().attach(lift.stageLift(0.6, 0)),
            outtake.stageStart(0.0),
            outtake.stageOpen(0.0),
            Modes.driveMode.ChangeMode(SLOW)
    );

}

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
import static global.Modes.OuttakeMode.ALLIANCE;
import static global.Modes.OuttakeMode.SHARED;
import static global.Modes.getHeightMode;


public interface AutoModuleUser extends RobotUser{

    AutoModule Backward = BackwardHeight(Modes.HeightMode.HIGH);

    OutputList BackwardAll = new OutputList(Modes::getHeightMode)
            .addOption(Modes.HeightMode.LOW, BackwardHeight(Modes.HeightMode.LOW))
            .addOption(Modes.HeightMode.MEDIUM, BackwardHeight(Modes.HeightMode.MEDIUM))
            .addOption(Modes.HeightMode.HIGH, BackwardHeight(Modes.HeightMode.HIGH));


    static AutoModule BackwardHeight(Modes.HeightMode mode){ return new AutoModule(
            outtake.stageEnd().attach(lift.stageLift(1.0, mode.getValue())),
            outtake.stageEnd(0.0),
            Modes.ChangeDrive(Modes.DriveMode.SLOW)
    );}

    AutoModule Forward = new AutoModule(
            Modes.ChangeDrive(Modes.DriveMode.MEDIUM),
            outtake.stageStart().attach(lift.stageLift(0.6, 1)),
            outtake.stageStart(0.0),
            outtake.stageOpen(0.0)
    );

}

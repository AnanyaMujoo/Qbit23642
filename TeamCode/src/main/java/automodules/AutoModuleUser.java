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


public interface AutoModuleUser extends RobotUser{

    AutoModule Backward = new AutoModule(
            lift.stageLift(1.0, Lift.maxPosition)
    );

    AutoModule Forward = new AutoModule(
            lift.stageLift(0.6, 0)
    );






}

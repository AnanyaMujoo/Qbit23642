package automodules;

import org.checkerframework.checker.units.qual.A;

import automodules.stage.Main;
import automodules.stage.Stage;
import elements.FieldSide;
import elements.Level;
import global.Modes;
import robot.RobotUser;
import robotparts.RobotPart;
import unused.auto.AutoModuleUserOld;
import util.condition.DecisionList;
import util.condition.OutputList;

import static global.General.bot;
import static global.General.fieldSide;
import static global.Modes.OuttakeMode.ALLIANCE;
import static global.Modes.OuttakeMode.SHARED;


public interface AutoModuleUser extends RobotUser{

    AutoModule Backward = new AutoModule(
            lift.stageLift(0.4, 20)
    );

    AutoModule Forward = new AutoModule(
            lift.stageLift(0.4, 0)
    );






}

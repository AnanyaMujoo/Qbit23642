package teleutil.independent;


import automodules.AutoModuleUser;
import global.Modes;
import robot.RobotUser;
import util.condition.DecisionList;

import static global.General.bot;

public class Independents implements RobotUser, AutoModuleUser {

    // TODO 4 NEW Independents clean up, create new
    // Why do we need Independent Runner?

    public Independent MoveForAllianceForward(){return new Independent(i -> {
        i.addCancelAutoModules();
        i.addConcurrentAutoModule(ResetLiftAndOuttake);
        i.addPause(0.5);
        i.addWaypoint(30,-70,10);
        i.addWaypoint(-3,-60,0);
        i.addWaypoint(-3,0,-5);
        i.addConcurrentAutoModule(IntakeCombined);
    });}

    public Independent MoveForAllianceBackward() {return new Independent(i -> {
        i.addWaypoint(-5,-50,5);
        i.addDecision(SetUpForBoth);
        i.addSetpoint(60, -80, -60);
    });}

    public Independent MoveForSharedForward(){return new Independent(i -> {
//        i.addCancelAutoModules();
//        i.addConcurrentAutoModule(ResetLiftAndOuttake);
//        i.addPause(0.6);
//        i.addWaypoint(-15,-40,0);
//        i.addWaypoint(5,-40,5);
//        i.addWaypoint(5,0,5);
//        i.addConcurrentAutoModule(IntakeCombined);
    });}

    public Independent MoveForSharedBackward(){return new Independent(i -> {
//        i.addWaypoint(5,-30,-5);
//        i.addAutomodule(SetUpForBoth);
//        i.addWaypoint(5,-40,-5);
//        i.addWaypoint(-15,-40,0);
//        i.addSetpoint(-30, -50, 30);
    });}

    public DecisionList MoveForForward = new DecisionList(mecanumOuttake::getOuttakeMode)
            .addOption(Modes.OuttakeMode.ALLIANCE, () -> bot.addIndependent(MoveForAllianceForward()))
            .addOption(Modes.OuttakeMode.SHARED, () -> bot.addIndependent(MoveForSharedForward()));

    public DecisionList MoveForBackward = new DecisionList(mecanumOuttake::getOuttakeMode)
            .addOption(Modes.OuttakeMode.ALLIANCE, () -> bot.addIndependent(MoveForAllianceBackward()))
            .addOption(Modes.OuttakeMode.SHARED, () -> bot.addIndependent(MoveForSharedBackward()));

    public DecisionList Forward = new DecisionList(mecanumDrive::getIndependentMode)
            .addOption(Modes.IndependentMode.MANUAL, () -> {
                bot.cancelAutoModules();
                bot.addAutoModule(ResetLiftAndOuttake);
            })
            .addOption(Modes.IndependentMode.USING, () -> {
                bot.independentRunner.enableIndependent();
                MoveForForward.check();
            });

    public DecisionList Backward = new DecisionList(mecanumDrive::getIndependentMode)
            .addOption(Modes.IndependentMode.MANUAL, SetUpForBoth::check)
            .addOption(Modes.IndependentMode.USING, () -> {
                bot.independentRunner.enableIndependent();
                odometry.reset();
                MoveForBackward.check();
            });

}

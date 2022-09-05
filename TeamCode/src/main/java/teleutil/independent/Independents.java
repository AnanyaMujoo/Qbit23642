package teleutil.independent;

import robotparts.sensors.TwoOdometry;
import teleutil.Modes;
import teleutil.independent.Independent;
import util.User;
import util.condition.DecisionList;
import util.condition.DecisionList.Decision;

import static global.General.automodules;
import static global.General.bot;
import static global.General.independents;

public class Independents {

    public Independent MoveForAllianceForward(){return new Independent(i -> {
        i.addCancelAutoModules();
        i.addConcurrentAutoModule(automodules.ResetLiftAndOuttake);
        i.addPause(0.5);
        i.addWaypoint(30,-70,10);
        i.addWaypoint(-3,-60,0);
        i.addWaypoint(-3,0,-5);
        i.addConcurrentAutoModule(automodules.IntakeCombined);
    });}

    public Independent MoveForAllianceBackward() {return new Independent(i -> {
        i.addWaypoint(-5,-50,5);
        i.addDecision(automodules.SetUpForBoth);
        i.addSetpoint(60, -80, -60);
    });}

    public Independent MoveForSharedForward(){return new Independent(i -> {
//        i.addCancelAutoModules();
//        i.addConcurrentAutoModule(automodules.ResetLiftAndOuttake);
//        i.addPause(0.6);
//        i.addWaypoint(-15,-40,0);
//        i.addWaypoint(5,-40,5);
//        i.addWaypoint(5,0,5);
//        i.addConcurrentAutoModule(automodules.IntakeCombined);
    });}

    public Independent MoveForSharedBackward(){return new Independent(i -> {
//        i.addWaypoint(5,-30,-5);
//        i.addAutomodule(automodules.SetUpForBoth);
//        i.addWaypoint(5,-40,-5);
//        i.addWaypoint(-15,-40,0);
//        i.addSetpoint(-30, -50, 30);
    });}

    public DecisionList MoveForForward = new DecisionList(bot.outtake::getOuttakeMode)
            .addOption(Modes.OuttakeMode.ALLIANCE, () -> bot.addIndependent(MoveForAllianceForward()))
            .addOption(Modes.OuttakeMode.SHARED, () -> bot.addIndependent(MoveForSharedForward()));

    public DecisionList MoveForBackward = new DecisionList(bot.outtake::getOuttakeMode)
            .addOption(Modes.OuttakeMode.ALLIANCE, () -> bot.addIndependent(MoveForAllianceBackward()))
            .addOption(Modes.OuttakeMode.SHARED, () -> bot.addIndependent(MoveForSharedBackward()));

    public DecisionList Forward = new DecisionList(bot.drive::getIndependentMode)
            .addOption(Modes.IndependentMode.MANUAL, () -> {
                bot.cancelAutoModules();
                bot.addAutoModule(automodules.ResetLiftAndOuttake);
            })
            .addOption(Modes.IndependentMode.USING, () -> {
                bot.independentRunner.enableIndependent();
                MoveForForward.check();
            });

    public DecisionList Backward = new DecisionList(bot.drive::getIndependentMode)
            .addOption(Modes.IndependentMode.MANUAL, automodules.SetUpForBoth::check)
            .addOption(Modes.IndependentMode.USING, () -> {
                bot.independentRunner.enableIndependent();
                bot.odometry.reset();
                MoveForBackward.check();
            });

}

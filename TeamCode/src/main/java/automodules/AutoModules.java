package automodules;

import automodules.stage.Main;
import automodules.stage.Stage;
import elements.FieldSide;
import elements.Level;
import robotparts.RobotPart;
import global.Modes;
import util.condition.DecisionList;

import static global.General.bot;
import static global.General.fieldSide;
import static global.Modes.OuttakeMode.ALLIANCE;
import static global.Modes.OuttakeMode.SHARED;

public class AutoModules {

    public StageList Pause(double secs){ return new StageList(new Stage(RobotPart.exitTime(secs))); }
    public StageList Cancel(){return new StageList(new Stage(new Main(bot::cancelAutoModules), RobotPart.exitAlways()));}

    public StageList IntakeOut = new StageList(bot.intake.outtakeTime(10));

    public StageList OneDuck = new StageList(bot.carousel.spinOneDuck(2,0.4,0.7));

    public StageList OneDuckAutoBlue = new StageList(bot.carousel.spinOneDuckMoving(6,0.4,0.4, 1.0, 0.0, 0.14, 0.0));
    public StageList OneDuckAutoRed = new StageList(bot.carousel.spinOneDuckMoving(6,0.4,0.4,-1.0, 0.0, 0.14, 0.0));

    public StageList LiftUpTopFast = new StageList(bot.lift.liftEncoderUp(1.0, 42));
    public StageList LiftUpMiddleFast = new StageList(bot.lift.liftEncoderUp(1.0, 20));
    public StageList LiftUpBottomFast = new StageList(bot.lift.liftEncoderUp(1.0, 5));

    public StageList LiftUpShared = new StageList(bot.lift.liftEncoderUp(0.5, 15));
    public StageList LiftReset = new StageList(bot.lift.liftEncoderDown(-0.5, 0));

    public StageList changeDrive(Modes.DriveMode driveMode){return bot.drive.mainChangeDrive(driveMode);}

    public StageList OuttakeHorizontal = new StageList(bot.outtake.stageTurnToHorizontal(0.15));
    public StageList OuttakeHorizontalFast = new StageList(bot.outtake.stageTurnToHorizontal(0.05));
    public StageList OuttakeAlliance = new StageList(bot.outtake.stageCenterTurret(0.05));
    public StageList OuttakeSharedRight = new StageList(bot.outtake.stageSharedTurretRight(0.5));
    public StageList OuttakeSharedLeft = new StageList(bot.outtake.stageSharedTurretLeft(0.5));
    public StageList OuttakeDrop = new StageList(bot.outtake.stageDrop(0.25));
    public StageList OuttakeDropFast = new StageList(bot.outtake.stageDrop(0.05));
    public StageList OuttakeLock = new StageList(bot.outtake.stageLock(0.25));
    public StageList OuttakeLockFast = new StageList(bot.outtake.stageLock(0.05));

    public StageList OuttakeReset = new StageList(
            bot.outtake.stageDrop(0.15),
            bot.outtake.stageCenterTurret(0.25),
            bot.outtake.stageLock(0.05),
            bot.outtake.stageTurnToStart(0.05)
    );

    public StageList IntakeOutAndLock = new StageList(bot.outtake.stageLock(0.05), bot.intake.intakeOutAndLock());
    public StageList IntakeUntilFreight = new StageList(bot.intake.intakeUntilFreight());

    public StageList AllianceLiftUp(StageList liftUp){return new StageList().add(OuttakeLockFast, OuttakeHorizontalFast, liftUp, OuttakeAlliance, changeDrive(Modes.DriveMode.SLOW));}

    public DecisionList SetUpForAllianceShippingHub = new DecisionList(() -> bot.lift.getLevelMode())
            .addOption(Level.TOP, () -> bot.addAutoModule(AllianceLiftUp(LiftUpTopFast)))
            .addOption(Level.MIDDLE, () -> bot.addAutoModule(AllianceLiftUp(LiftUpMiddleFast)))
            .addOption(Level.BOTTOM, () -> bot.addAutoModule(AllianceLiftUp(LiftUpBottomFast)));

    public StageList SetUpForSharedShippingHubRight = new StageList().add(OuttakeLockFast, OuttakeHorizontal, LiftUpShared, OuttakeSharedRight, changeDrive(Modes.DriveMode.SLOW));
    public StageList SetUpForSharedShippingHubLeft = new StageList().add(OuttakeLockFast,OuttakeHorizontal, LiftUpShared, OuttakeSharedLeft, changeDrive(Modes.DriveMode.SLOW));
    public StageList SetUpForSharedShippingHubCenter = new StageList().add(OuttakeLockFast, OuttakeHorizontal, LiftUpShared, changeDrive(Modes.DriveMode.SLOW));


    public DecisionList SetUpForSharedShippingHubBoth = new DecisionList(() -> fieldSide)
            .addOption(FieldSide.BLUE, () -> bot.addAutoModule(SetUpForSharedShippingHubLeft))
            .addOption(FieldSide.RED, () -> bot.addAutoModule(SetUpForSharedShippingHubRight));

    public DecisionList SetUpForSharedShippingHubAll = new DecisionList(bot.outtake::getSharedMode)
            .addOption(Modes.SharedMode.NORMAL, SetUpForSharedShippingHubBoth::check)
            .addOption(Modes.SharedMode.CENTER, () -> bot.addAutoModule(SetUpForSharedShippingHubCenter));

    public DecisionList SetUpForBoth = new DecisionList(bot.outtake::getOuttakeMode)
            .addOption(ALLIANCE, SetUpForAllianceShippingHub::check)
            .addOption(SHARED, SetUpForSharedShippingHubAll::check);

    public StageList ResetLiftAndOuttake = new StageList().add(changeDrive(Modes.DriveMode.FAST), OuttakeReset, LiftReset);

    public StageList IntakeCombined = new StageList().add(changeDrive(Modes.DriveMode.MEDIUM), OuttakeDropFast, IntakeUntilFreight, IntakeOutAndLock);

}

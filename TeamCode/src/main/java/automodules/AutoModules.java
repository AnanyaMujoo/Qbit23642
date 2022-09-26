package automodules;

import automodules.stage.Main;
import automodules.stage.Stage;
import elements.FieldSide;
import elements.Level;

import robot.RobotUser;
import robotparts.RobotPart;
import global.Modes;
import util.condition.DecisionList;

import static global.General.bot;
import static global.General.fieldSide;
import static global.Modes.OuttakeMode.ALLIANCE;
import static global.Modes.OuttakeMode.SHARED;

public class AutoModules implements RobotUser {

    public StageList Pause(double secs){ return new StageList(new Stage(RobotPart.exitTime(secs))); }
    public StageList Cancel(){return new StageList(new Stage(new Main(bot::cancelAutoModules), RobotPart.exitAlways()));}

    public StageList IntakeOut = new StageList(mecanumIntake.outtakeTime(10));

    public StageList OneDuck = new StageList(mecanumCarousel.spinOneDuck(2,0.4,0.7));

    public StageList OneDuckAutoBlue = new StageList(mecanumCarousel.spinOneDuckMoving(6,0.4,0.4, 1.0, 0.0, 0.14, 0.0));
    public StageList OneDuckAutoRed = new StageList(mecanumCarousel.spinOneDuckMoving(6,0.4,0.4,-1.0, 0.0, 0.14, 0.0));

    public StageList LiftUpTopFast = new StageList(mecanumLift.liftEncoderUp(1.0, 42));
    public StageList LiftUpMiddleFast = new StageList(mecanumLift.liftEncoderUp(1.0, 20));
    public StageList LiftUpBottomFast = new StageList(mecanumLift.liftEncoderUp(1.0, 5));

    public StageList LiftUpShared = new StageList(mecanumLift.liftEncoderUp(0.5, 15));
    public StageList LiftReset = new StageList(mecanumLift.liftEncoderDown(-0.5, 0));

    public StageList changeDrive(Modes.DriveMode driveMode){return mecanumDrive.mainChangeDrive(driveMode);}

    public StageList OuttakeHorizontal = new StageList(mecanumOuttake.stageTurnToHorizontal(0.15));
    public StageList OuttakeHorizontalFast = new StageList(mecanumOuttake.stageTurnToHorizontal(0.05));
    public StageList OuttakeAlliance = new StageList(mecanumOuttake.stageCenterTurret(0.05));
    public StageList OuttakeSharedRight = new StageList(mecanumOuttake.stageSharedTurretRight(0.5));
    public StageList OuttakeSharedLeft = new StageList(mecanumOuttake.stageSharedTurretLeft(0.5));
    public StageList OuttakeDrop = new StageList(mecanumOuttake.stageDrop(0.25));
    public StageList OuttakeDropFast = new StageList(mecanumOuttake.stageDrop(0.05));
    public StageList OuttakeLock = new StageList(mecanumOuttake.stageLock(0.25));
    public StageList OuttakeLockFast = new StageList(mecanumOuttake.stageLock(0.05));

    public StageList OuttakeReset = new StageList(
            mecanumOuttake.stageDrop(0.15),
            mecanumOuttake.stageCenterTurret(0.25),
            mecanumOuttake.stageLock(0.05),
            mecanumOuttake.stageTurnToStart(0.05)
    );

    public StageList IntakeOutAndLock = new StageList(mecanumOuttake.stageLock(0.05), mecanumIntake.intakeOutAndLock());
    public StageList IntakeUntilFreight = new StageList(mecanumIntake.intakeUntilFreight());

    public StageList AllianceLiftUp(StageList liftUp){return new StageList().add(OuttakeLockFast, OuttakeHorizontalFast, liftUp, OuttakeAlliance, changeDrive(Modes.DriveMode.SLOW));}

    public DecisionList SetUpForAllianceShippingHub = new DecisionList(() -> mecanumLift.getLevelMode())
            .addOption(Level.TOP, () -> bot.addAutoModule(AllianceLiftUp(LiftUpTopFast)))
            .addOption(Level.MIDDLE, () -> bot.addAutoModule(AllianceLiftUp(LiftUpMiddleFast)))
            .addOption(Level.BOTTOM, () -> bot.addAutoModule(AllianceLiftUp(LiftUpBottomFast)));

    public StageList SetUpForSharedShippingHubRight = new StageList().add(OuttakeLockFast, OuttakeHorizontal, LiftUpShared, OuttakeSharedRight, changeDrive(Modes.DriveMode.SLOW));
    public StageList SetUpForSharedShippingHubLeft = new StageList().add(OuttakeLockFast,OuttakeHorizontal, LiftUpShared, OuttakeSharedLeft, changeDrive(Modes.DriveMode.SLOW));
    public StageList SetUpForSharedShippingHubCenter = new StageList().add(OuttakeLockFast, OuttakeHorizontal, LiftUpShared, changeDrive(Modes.DriveMode.SLOW));


    public DecisionList SetUpForSharedShippingHubBoth = new DecisionList(() -> fieldSide)
            .addOption(FieldSide.BLUE, () -> bot.addAutoModule(SetUpForSharedShippingHubLeft))
            .addOption(FieldSide.RED, () -> bot.addAutoModule(SetUpForSharedShippingHubRight));

    public DecisionList SetUpForSharedShippingHubAll = new DecisionList(mecanumOuttake::getSharedMode)
            .addOption(Modes.SharedMode.NORMAL, SetUpForSharedShippingHubBoth::check)
            .addOption(Modes.SharedMode.CENTER, () -> bot.addAutoModule(SetUpForSharedShippingHubCenter));

    public DecisionList SetUpForBoth = new DecisionList(mecanumOuttake::getOuttakeMode)
            .addOption(ALLIANCE, SetUpForAllianceShippingHub::check)
            .addOption(SHARED, SetUpForSharedShippingHubAll::check);

    public StageList ResetLiftAndOuttake = new StageList().add(changeDrive(Modes.DriveMode.FAST), OuttakeReset, LiftReset);

    public StageList IntakeCombined = new StageList().add(changeDrive(Modes.DriveMode.MEDIUM), OuttakeDropFast, IntakeUntilFreight, IntakeOutAndLock);

}

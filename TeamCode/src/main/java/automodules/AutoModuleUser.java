package automodules;

import automodules.stage.Main;
import automodules.stage.Stage;
import elements.FieldSide;
import elements.Level;
import global.Modes;
import robot.RobotUser;
import robotparts.RobotPart;
import util.condition.DecisionList;
import util.condition.OutputList;

import static global.General.bot;
import static global.General.fieldSide;
import static global.Modes.OuttakeMode.ALLIANCE;
import static global.Modes.OuttakeMode.SHARED;

public interface AutoModuleUser extends RobotUser {


    AutoModule DriveForward = new AutoModule(drive.moveTime(0.3, 0, 0, 1));
    AutoModule DriveBackward = new AutoModule(drive.moveTime(-0.3, 0, 0, 1));

    OutputList DriveField = new OutputList(() -> fieldSide)
            .addOption(FieldSide.UNKNOWN, DriveForward)
            .addOption(FieldSide.RED, DriveBackward);

    AutoModule IntakeUntilFreightNew = new AutoModule(intake.intakeUntilFreight(1));



    public AutoModule IntakeOut = new AutoModule(mecanumIntake.outtakeTime(10));

    public AutoModule OneDuck = new AutoModule(mecanumCarousel.spinOneDuck(2,0.4,0.7));

    public AutoModule OneDuckAutoBlue = new AutoModule(mecanumCarousel.spinOneDuckMoving(6,0.4,0.4, 1.0, 0.0, 0.14, 0.0));
    public AutoModule OneDuckAutoRed = new AutoModule(mecanumCarousel.spinOneDuckMoving(6,0.4,0.4,-1.0, 0.0, 0.14, 0.0));

    public AutoModule LiftUpTopFast = new AutoModule(mecanumLift.liftEncoderUp(1.0, 42));
    public AutoModule LiftUpMiddleFast = new AutoModule(mecanumLift.liftEncoderUp(1.0, 20));
    public AutoModule LiftUpBottomFast = new AutoModule(mecanumLift.liftEncoderUp(1.0, 5));

    public AutoModule LiftUpShared = new AutoModule(mecanumLift.liftEncoderUp(0.5, 15));
    public AutoModule LiftReset = new AutoModule(mecanumLift.liftEncoderDown(-0.5, 0));

    default AutoModule changeDrive(Modes.DriveMode driveMode){return mecanumDrive.mainChangeDrive(driveMode);}
    static AutoModule changeDrive2(Modes.DriveMode driveMode){return mecanumDrive.mainChangeDrive(driveMode);}

    public AutoModule OuttakeHorizontal = new AutoModule(mecanumOuttake.stageTurnToHorizontal(0.15));
    public AutoModule OuttakeHorizontalFast = new AutoModule(mecanumOuttake.stageTurnToHorizontal(0.05));
    public AutoModule OuttakeAlliance = new AutoModule(mecanumOuttake.stageCenterTurret(0.05));
    public AutoModule OuttakeSharedRight = new AutoModule(mecanumOuttake.stageSharedTurretRight(0.5));
    public AutoModule OuttakeSharedLeft = new AutoModule(mecanumOuttake.stageSharedTurretLeft(0.5));
    public AutoModule OuttakeDrop = new AutoModule(mecanumOuttake.stageDrop(0.25));
    public AutoModule OuttakeDropFast = new AutoModule(mecanumOuttake.stageDrop(0.05));
    public AutoModule OuttakeLock = new AutoModule(mecanumOuttake.stageLock(0.25));
    public AutoModule OuttakeLockFast = new AutoModule(mecanumOuttake.stageLock(0.05));

    public AutoModule OuttakeReset = new AutoModule(
            mecanumOuttake.stageDrop(0.15),
            mecanumOuttake.stageCenterTurret(0.25),
            mecanumOuttake.stageLock(0.05),
            mecanumOuttake.stageTurnToStart(0.05)
    );

    public AutoModule IntakeOutAndLock = new AutoModule(mecanumOuttake.stageLock(0.05), mecanumIntake.intakeOutAndLock());
    public AutoModule IntakeUntilFreight = new AutoModule(mecanumIntake.intakeUntilFreight());

    default AutoModule AllianceLiftUp(AutoModule liftUp){return new AutoModule().add(OuttakeLockFast, OuttakeHorizontalFast, liftUp, OuttakeAlliance, changeDrive(Modes.DriveMode.SLOW));}
    static AutoModule AllianceLiftUp2(AutoModule liftUp){return new AutoModule().add(OuttakeLockFast, OuttakeHorizontalFast, liftUp, OuttakeAlliance, changeDrive2(Modes.DriveMode.SLOW));}

    public DecisionList SetUpForAllianceShippingHub = new DecisionList(mecanumLift::getLevelMode)
            .addOption(Level.TOP, () -> bot.addAutoModule(AllianceLiftUp2(LiftUpTopFast)))
            .addOption(Level.MIDDLE, () -> bot.addAutoModule(AllianceLiftUp2(LiftUpMiddleFast)))
            .addOption(Level.BOTTOM, () -> bot.addAutoModule(AllianceLiftUp2(LiftUpBottomFast)));

    public AutoModule SetUpForSharedShippingHubRight = new AutoModule().add(OuttakeLockFast, OuttakeHorizontal, LiftUpShared, OuttakeSharedRight, changeDrive2(Modes.DriveMode.SLOW));
    public AutoModule SetUpForSharedShippingHubLeft = new AutoModule().add(OuttakeLockFast,OuttakeHorizontal, LiftUpShared, OuttakeSharedLeft, changeDrive2(Modes.DriveMode.SLOW));
    public AutoModule SetUpForSharedShippingHubCenter = new AutoModule().add(OuttakeLockFast, OuttakeHorizontal, LiftUpShared, changeDrive2(Modes.DriveMode.SLOW));


    public DecisionList SetUpForSharedShippingHubBoth = new DecisionList(() -> fieldSide)
            .addOption(FieldSide.BLUE, () -> bot.addAutoModule(SetUpForSharedShippingHubLeft))
            .addOption(FieldSide.RED, () -> bot.addAutoModule(SetUpForSharedShippingHubRight));

    public DecisionList SetUpForSharedShippingHubAll = new DecisionList(mecanumOuttake::getSharedMode)
            .addOption(Modes.SharedMode.NORMAL, SetUpForSharedShippingHubBoth::check)
            .addOption(Modes.SharedMode.CENTER, () -> bot.addAutoModule(SetUpForSharedShippingHubCenter));

    public DecisionList SetUpForBoth = new DecisionList(mecanumOuttake::getOuttakeMode)
            .addOption(ALLIANCE, SetUpForAllianceShippingHub::check)
            .addOption(SHARED, SetUpForSharedShippingHubAll::check);

    public AutoModule ResetLiftAndOuttake = new AutoModule().add(changeDrive2(Modes.DriveMode.FAST), OuttakeReset, LiftReset);

    public AutoModule IntakeCombined = new AutoModule().add(changeDrive2(Modes.DriveMode.MEDIUM), OuttakeDropFast, IntakeUntilFreight, IntakeOutAndLock);




    default AutoModule Pause(double secs){ return new AutoModule(new Stage(RobotPart.exitTime(secs))); }
    default AutoModule Cancel(){return new AutoModule(new Stage(new Main(bot::cancelAutoModules), RobotPart.exitAlways()));}
}

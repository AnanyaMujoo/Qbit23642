package automodules;

import org.checkerframework.checker.units.qual.A;

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


    AutoModule IntakeNew = new AutoModule(
            outtake.stageDrop(0.05),
            intake.intakeUntilFreight(1),
            outtake.stageLock(0.05),
            intake.moveTime(-1, 1)
    );

    AutoModule BackwardNew = new AutoModule(
            outtake.stageLock(0.05),
            outtake.stageTurnToHorizontal(0.05),
            lift.stageLift(1.0, 26),
            outtake.stageCenterTurret(0.05)
    );

    AutoModule ForwardNew = new AutoModule(
            outtake.stageDrop(0.15),
            outtake.stageCenterTurret(0.25),
            outtake.stageLock(0.05),
            outtake.stageTurnToStart(0.05),
            lift.stageLift(-0.5, 0)
    );

    AutoModule DuckNew = new AutoModule(
            carousel.spinOneDuck(2,0.4,0.7)
    );







    AutoModule IntakeOut = new AutoModule(mecanumIntake.outtakeTime(10));


    AutoModule OneDuck = new AutoModule(mecanumCarousel.spinOneDuck(2,0.4,0.7));


    AutoModule OneDuckAutoBlue = new AutoModule(mecanumCarousel.spinOneDuckMoving(6,0.4,0.4, 1.0, 0.0, 0.14, 0.0));

    AutoModule OneDuckAutoRed = new AutoModule(mecanumCarousel.spinOneDuckMoving(6,0.4,0.4,-1.0, 0.0, 0.14, 0.0));


    AutoModule LiftUpTopFast = new AutoModule(mecanumLift.liftEncoderUp(1.0, 42));

    AutoModule LiftUpMiddleFast = new AutoModule(mecanumLift.liftEncoderUp(1.0, 20));

    AutoModule LiftUpBottomFast = new AutoModule(mecanumLift.liftEncoderUp(1.0, 5));


    AutoModule LiftUpShared = new AutoModule(mecanumLift.liftEncoderUp(0.5, 15));

    AutoModule LiftReset = new AutoModule(mecanumLift.liftEncoderDown(-0.5, 0));

    default AutoModule changeDrive(Modes.DriveMode driveMode){return mecanumDrive.mainChangeDrive(driveMode);}
    static AutoModule changeDrive2(Modes.DriveMode driveMode){return mecanumDrive.mainChangeDrive(driveMode);}


    AutoModule OuttakeHorizontal = new AutoModule(mecanumOuttake.stageTurnToHorizontal(0.15));

    AutoModule OuttakeHorizontalFast = new AutoModule(mecanumOuttake.stageTurnToHorizontal(0.05));

    AutoModule OuttakeAlliance = new AutoModule(mecanumOuttake.stageCenterTurret(0.05));

    AutoModule OuttakeSharedRight = new AutoModule(mecanumOuttake.stageSharedTurretRight(0.5));

    AutoModule OuttakeSharedLeft = new AutoModule(mecanumOuttake.stageSharedTurretLeft(0.5));

    AutoModule OuttakeDrop = new AutoModule(mecanumOuttake.stageDrop(0.25));

    AutoModule OuttakeDropFast = new AutoModule(mecanumOuttake.stageDrop(0.05));

    AutoModule OuttakeLock = new AutoModule(mecanumOuttake.stageLock(0.25));

    AutoModule OuttakeLockFast = new AutoModule(mecanumOuttake.stageLock(0.05));


    AutoModule OuttakeReset = new AutoModule(
            mecanumOuttake.stageDrop(0.15),
            mecanumOuttake.stageCenterTurret(0.25),
            mecanumOuttake.stageLock(0.05),
            mecanumOuttake.stageTurnToStart(0.05)
    );


    AutoModule IntakeOutAndLock = new AutoModule(mecanumOuttake.stageLock(0.05), mecanumIntake.intakeOutAndLock());

    AutoModule IntakeUntilFreight = new AutoModule(mecanumIntake.intakeUntilFreight());

    default AutoModule AllianceLiftUp(AutoModule liftUp){return new AutoModule().add(OuttakeLockFast, OuttakeHorizontalFast, liftUp, OuttakeAlliance, changeDrive(Modes.DriveMode.SLOW));}
    static AutoModule AllianceLiftUp2(AutoModule liftUp){return new AutoModule().add(OuttakeLockFast, OuttakeHorizontalFast, liftUp, OuttakeAlliance, changeDrive2(Modes.DriveMode.SLOW));}


    DecisionList SetUpForAllianceShippingHub = new DecisionList(mecanumLift::getLevelMode)
            .addOption(Level.TOP, () -> bot.addAutoModule(AllianceLiftUp2(LiftUpTopFast)))
            .addOption(Level.MIDDLE, () -> bot.addAutoModule(AllianceLiftUp2(LiftUpMiddleFast)))
            .addOption(Level.BOTTOM, () -> bot.addAutoModule(AllianceLiftUp2(LiftUpBottomFast)));


    AutoModule SetUpForSharedShippingHubRight = new AutoModule().add(OuttakeLockFast, OuttakeHorizontal, LiftUpShared, OuttakeSharedRight, changeDrive2(Modes.DriveMode.SLOW));

    AutoModule SetUpForSharedShippingHubLeft = new AutoModule().add(OuttakeLockFast,OuttakeHorizontal, LiftUpShared, OuttakeSharedLeft, changeDrive2(Modes.DriveMode.SLOW));

    AutoModule SetUpForSharedShippingHubCenter = new AutoModule().add(OuttakeLockFast, OuttakeHorizontal, LiftUpShared, changeDrive2(Modes.DriveMode.SLOW));



    DecisionList SetUpForSharedShippingHubBoth = new DecisionList(() -> fieldSide)
            .addOption(FieldSide.BLUE, () -> bot.addAutoModule(SetUpForSharedShippingHubLeft))
            .addOption(FieldSide.RED, () -> bot.addAutoModule(SetUpForSharedShippingHubRight));


    DecisionList SetUpForSharedShippingHubAll = new DecisionList(mecanumOuttake::getSharedMode)
            .addOption(Modes.SharedMode.NORMAL, SetUpForSharedShippingHubBoth::check)
            .addOption(Modes.SharedMode.CENTER, () -> bot.addAutoModule(SetUpForSharedShippingHubCenter));


    DecisionList SetUpForBoth = new DecisionList(mecanumOuttake::getOuttakeMode)
            .addOption(ALLIANCE, SetUpForAllianceShippingHub::check)
            .addOption(SHARED, SetUpForSharedShippingHubAll::check);


    AutoModule ResetLiftAndOuttake = new AutoModule().add(changeDrive2(Modes.DriveMode.FAST), OuttakeReset, LiftReset);


    AutoModule IntakeCombined = new AutoModule().add(changeDrive2(Modes.DriveMode.MEDIUM), OuttakeDropFast, IntakeUntilFreight, IntakeOutAndLock);




    default AutoModule Pause(double secs){ return new AutoModule(new Stage(RobotPart.exitTime(secs))); }
    default AutoModule Cancel(){return new AutoModule(new Stage(new Main(bot::cancelAutoModules), RobotPart.exitAlways()));}
}

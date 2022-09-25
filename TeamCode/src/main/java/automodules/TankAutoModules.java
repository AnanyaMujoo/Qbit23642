package automodules;

import static global.General.*;

import robotparts.RobotPart;

@Deprecated
public class TankAutoModules {
    /**
     * Define AutoModules here as a stage list
     */
            // Remove the inputted RobotParts
    public StageList IntakeAuto = new StageList(
        bot.tankLift.liftEncoder(0.4, 0),
        bot.tankIntake.intakeUntilFreight(1),
        bot.tankOuttake.outtakeLock(1)
    );

    public StageList IntakeTele = new StageList(
        bot.tankOuttake.outtakeReset(0.05),
        bot.tankTurret.turretEncoder(1, 0),
        bot.tankLift.liftEncoder(0.4, 0),
        bot.tankIntake.intakeUntilFreightLiftDown(1),
        bot.tankOuttake.outtakeLockAndIntake(1),
        bot.tankIntake.intakeTime(-1, 0.8)
    );

    public StageList Forward = new StageList(
        bot.tankOuttake.outtakeDrop(0.6),
        bot.tankTurret.turretEncoder(1, 0),
        bot.tankLift.liftEncoder(0.4, 10),
        bot.tankOuttake.outtakeReset(0.7)
    );

    public StageList Backward = new StageList(
        bot.tankLift.liftEncoder(1, 48),
        bot.tankTurret.turretEncoderTarget(1)
    );

    public StageList BackwardTele() {
//        if (bot.turret.freightPlaced == 0) {
//            return new StageList(bot.lift, bot.turret).define(
//                bot.lift.liftEncoder(1, 48),
//                bot.turret.turretEncoderTarget(1),
//                bot.lift.liftEncoder(0.4, 20)
//            );
//        }
        return new StageList(
            bot.tankLift.liftEncoder(1, 48),
            bot.tankTurret.turretEncoderTarget(1)
        );
    }

    public StageList ForwardTele() {
//        if (bot.turret.freightPlaced == 1) {
//            return new StageList(bot.outtake, bot.turret, bot.lift).define(
//                bot.outtake.outtakeDrop(0.6),
//                bot.lift.liftEncoder(1, 48),
//                bot.turret.turretEncoder(1, 0),
//                bot.lift.liftEncoder(0.4, 10),
//                bot.outtake.outtakeReset(0.7)
//            );
//        }
        return new StageList(
            bot.tankOuttake.outtakeDrop(0.6),
            bot.tankTurret.turretEncoder(1, 0),
            bot.tankLift.liftEncoder(0.4, 10),
            bot.tankOuttake.outtakeReset(0.7)
        );
    }

    public StageList SpinCarousel = new StageList(
        bot.tankCarousel.spin(3)
    );

    public StageList ResetTurretAndLift = new StageList(
        bot.tankTurret.turretEncoder(1, 0),
        RobotPart.pause(1),
        bot.tankLift.liftTime(-0.2, 3)
    );

    public StageList MoveForwardTime(double time) {
        return MoveTime(0.5, 0, time);
    }

    public StageList MoveBackwardTime(double time) {
        return MoveTime(-0.5, 0, time);
    }

    public StageList MoveCWTime(double time) {
        return MoveTime(0, 0.4, time);
    }

    public StageList MoveCCWTime(double time) {
        return MoveTime(0, -0.5, time);
    }

    public StageList MoveTime(double forward, double turn, double time) {
        return new StageList(
            bot.tankDrive.moveTime(forward, turn, time)
        );
    }

    public StageList LiftOdometry = new StageList(
        bot.tankDrive.liftOdo()
    );

    public StageList pause(double time) {
        return new StageList(
            RobotPart.pause(time)
        );
    }
}

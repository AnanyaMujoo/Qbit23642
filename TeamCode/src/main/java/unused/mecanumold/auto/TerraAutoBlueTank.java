package unused.mecanumold.auto;

import automodules.AutoModule;
import auton.Auto;
import elements.FieldPlacement;
import unused.tankold.TankExecutor;
import elements.FieldSide;
import robotparts.RobotPart;
import util.Timer;

import static java.lang.Math.*;
import static global.General.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

// TOD4 FIX
// CONVERT TO COMPLETE AUTO

@Disabled
@Deprecated
@Autonomous(name="TerraAutoBlueTank")
public class TerraAutoBlueTank extends Auto {
    Timer timer = new Timer();

    @Override
    public void initAuto() {
        activate();
    }

    @Override
    public void runAuto() {
        TankExecutor executor = new TankExecutor();
//        executor.addSetpoint(70, 30, PI/2, AngleType.RADIANS);
        executor.addUnsynchronizedRF(SpinCarousel);
        executor.addSynchronizedRF(IntakeAuto);
//        executor.addSetpoint(-50, 43, -PI/2, AngleType.RADIANS);
//        executor.addSetpoint(-65, 43, -PI/2, AngleType.RADIANS);
        executor.addUnsynchronizedRF(Backward);
        executor.addPause(2);
        executor.addUnsynchronizedRF(Forward);
//        executor.addSetpoint(-40, 43, -PI/2, AngleType.RADIANS);
//        executor.addSetpoint(-70, 15, -PI/2, AngleType.RADIANS);
//        executor.addSetpoint(-90, 15, -PI/2, AngleType.RADIANS);
        executor.addUnsynchronizedRF(LiftOdometry);
        executor.addUnsynchronizedRF(MoveCWTime(0.5));
        executor.addUnsynchronizedRF(MoveForwardTime(2));
        executor.addUnsynchronizedRF(ResetTurretAndLift);
//
//        executor.complete();

        waitForStart();

        ready();
        bot.tankOuttake.lockCube();
//        executor.resumeMove();

        while (opModeIsActive() && !executor.finished()) {
            executor.update();
            if (bot.tankLift.getPower()[0] == 0) {
                bot.tankLift.move(0);
            }
            update(true);
        }
        bot.tankDrive.raise();
        moveTime(-0.3, 0.0, 1);
        moveTime(0.0, -0.5, 0.8);
        moveTime(0.3, 0.0, 1);
        moveTime(0.0, 0.5, 0.7);
        moveTime(1, 0.0, 1.3);
        bot.tankDrive.move(0, 0);
    }

    private void moveTime(double f, double t, double time){
        timer.reset();
        while (opModeIsActive() && timer.seconds() < time) {
            bot.tankLift.move(0);
            bot.tankDrive.move(f,t);
        }
    }

    public AutoModule IntakeAuto = new AutoModule(
            bot.tankLift.liftEncoder(0.4, 0),
            bot.tankIntake.intakeUntilFreight(1),
            bot.tankOuttake.outtakeLock(1)
    );

    public AutoModule IntakeTele = new AutoModule(
            bot.tankOuttake.outtakeReset(0.05),
            bot.tankTurret.turretEncoder(1, 0),
            bot.tankLift.liftEncoder(0.4, 0),
            bot.tankIntake.intakeUntilFreightLiftDown(1),
            bot.tankOuttake.outtakeLockAndIntake(1),
            bot.tankIntake.intakeTime(-1, 0.8)
    );

    public AutoModule Forward = new AutoModule(
            bot.tankOuttake.outtakeDrop(0.6),
            bot.tankTurret.turretEncoder(1, 0),
            bot.tankLift.liftEncoder(0.4, 10),
            bot.tankOuttake.outtakeReset(0.7)
    );

    public AutoModule Backward = new AutoModule(
            bot.tankLift.liftEncoder(1, 48),
            bot.tankTurret.turretEncoderTarget(1)
    );

    public AutoModule BackwardTele() {
//        if (bot.turret.freightPlaced == 0) {
//            return new StageList(bot.lift, bot.turret).define(
//                bot.lift.liftEncoder(1, 48),
//                bot.turret.turretEncoderTarget(1),
//                bot.lift.liftEncoder(0.4, 20)
//            );
//        }
        return new AutoModule(
                bot.tankLift.liftEncoder(1, 48),
                bot.tankTurret.turretEncoderTarget(1)
        );
    }

    public AutoModule ForwardTele() {
//        if (bot.turret.freightPlaced == 1) {
//            return new StageList(bot.outtake, bot.turret, bot.lift).define(
//                bot.outtake.outtakeDrop(0.6),
//                bot.lift.liftEncoder(1, 48),
//                bot.turret.turretEncoder(1, 0),
//                bot.lift.liftEncoder(0.4, 10),
//                bot.outtake.outtakeReset(0.7)
//            );
//        }
        return new AutoModule(
                bot.tankOuttake.outtakeDrop(0.6),
                bot.tankTurret.turretEncoder(1, 0),
                bot.tankLift.liftEncoder(0.4, 10),
                bot.tankOuttake.outtakeReset(0.7)
        );
    }

    public AutoModule SpinCarousel = new AutoModule(
            bot.tankCarousel.spin(3)
    );

    public AutoModule ResetTurretAndLift = new AutoModule(
            bot.tankTurret.turretEncoder(1, 0),
            RobotPart.pause(1),
            bot.tankLift.liftTime(-0.2, 3)
    );

    public AutoModule MoveForwardTime(double time) {
        return MoveTime(0.5, 0, time);
    }

    public AutoModule MoveBackwardTime(double time) {
        return MoveTime(-0.5, 0, time);
    }

    public AutoModule MoveCWTime(double time) {
        return MoveTime(0, 0.4, time);
    }

    public AutoModule MoveCCWTime(double time) {
        return MoveTime(0, -0.5, time);
    }

    public AutoModule MoveTime(double forward, double turn, double time) {
        return new AutoModule(
                bot.tankDrive.moveTime(forward, turn, time)
        );
    }

    public AutoModule LiftOdometry = new AutoModule(
            bot.tankDrive.liftOdo()
    );

}

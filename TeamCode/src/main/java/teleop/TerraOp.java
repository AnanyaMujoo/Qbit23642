package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import elements.FieldSide;
import teleutil.button.Button;
import teleutil.button.OnNotHeldEventHandler;
import teleutil.button.OnPressEventHandler;
import teleutil.button.OnTurnOffEventHandler;
import teleutil.button.OnTurnOnEventHandler;

import static global.General.bot;
import static global.General.gph1;
import static global.General.*;


public class TerraOp extends Tele{

    // TODO large on press handler support/default?

    @Override
    public void initTele() {

        gph1.link(Button.X, OnPressEventHandler.class, () -> {
            bot.cancelAutoModules();
            bot.cancelIndependent();
        });

//        gph1.link(Button.DPAD_DOWN, OnPressEventHandler.class, bot::pauseAutoModules);
//        gph1.link(Button.DPAD_UP, OnPressEventHandler.class, bot::resumeAutoModules);

        gph1.link(Button.LEFT_TRIGGER, automodules.OneDuck);
        gph1.link(Button.RIGHT_TRIGGER, OnPressEventHandler.class, bot.lift::cycleLevelUp);
        gph1.link(Button.RIGHT_BUMPER, OnPressEventHandler.class, bot.outtake::cycleOuttakeMode);
        gph1.link(Button.LEFT_BUMPER, OnPressEventHandler.class, bot.outtake::cycleSharedMode);
        gph1.link(Button.RIGHT_STICK_BUTTON, OnPressEventHandler.class, bot.drive::cycleDriveUp);
        gph1.link(Button.LEFT_STICK_BUTTON, OnPressEventHandler.class, () -> bot.intake.moveRaw(-1));

        gph1.link(Button.A, automodules.IntakeCombined);

        gph1.link(Button.B, independents.Backward);
        gph1.link(Button.Y, independents.Forward);

        gph1.link(Button.DPAD_RIGHT,OnPressEventHandler.class, bot.outtake::collectCap);
        gph1.link(Button.DPAD_DOWN,OnPressEventHandler.class, bot.outtake::readyCap);
        gph1.link(Button.DPAD_LEFT,OnPressEventHandler.class, bot.outtake::dropCap);
        gph1.link(Button.DPAD_UP, OnPressEventHandler.class, bot.outtake::startCap);

        gph2.link(Button.RIGHT_TRIGGER, OnPressEventHandler.class, bot.outtake::lock);
        gph2.link(Button.LEFT_TRIGGER, OnPressEventHandler.class, bot.outtake::drop);
        gph2.link(Button.RIGHT_BUMPER, OnTurnOnEventHandler.class, () -> bot.intake.move(1));
        gph2.link(Button.RIGHT_BUMPER, OnTurnOffEventHandler.class, () -> bot.intake.move(0));
        gph2.link(Button.LEFT_BUMPER, OnPressEventHandler.class, () -> bot.intake.move(-1));
        gph2.link(Button.LEFT_BUMPER, OnNotHeldEventHandler.class, () -> bot.intake.move(0));
        gph2.link(Button.RIGHT_STICK_BUTTON, OnPressEventHandler.class, bot.outtake::turnToHorizontal);
        gph2.link(Button.LEFT_STICK_BUTTON, OnPressEventHandler.class, bot.outtake::turnToStart);
        gph2.link(Button.DPAD_RIGHT, OnPressEventHandler.class, bot.outtake::sharedTurretRight);
        gph2.link(Button.DPAD_DOWN, OnPressEventHandler.class, bot.outtake::turretCenter);
        gph2.link(Button.DPAD_LEFT, OnPressEventHandler.class, bot.outtake::sharedTurretLeft);

        bot.lift.resetEncoder();

        bot.intake.scale = 0.8;

//        bot.drive.setIndependentMode(Modes.IndependentMode.USING);
    }

    @Override
    public void startTele() {
        bot.outtake.midCap();
    }

    @Override
    public void loopTele() {
        bot.drive.moveSmoothTele(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

        if(gamepad2.right_stick_y == 0){
            bot.lift.holdPosition();
        }else {
            bot.lift.move(-gamepad2.right_stick_y);
        }

        log.show("OuttakeMode", bot.outtake.getOuttakeMode());
        log.show("SharedMode", bot.outtake.getSharedMode());
        log.show("DriveMode", bot.drive.getDriveMode());
        log.show("LevelMode", bot.lift.getLevelMode());
        log.show("IndependentMode", bot.drive.getIndependentMode());




//        log.show("wasrun", bot.independentRunner.wasRun);
//        log.show("numsegs", bot.independentRunner.numSegs);
//        log.show("Access", bot.drive.checkAccess(User.BACK));
//        log.show("odmety pos", Arrays.toString(bot.odometry.getPose()));
//        log.show("horz", bot.odometry.getHorizontalEncoderPosition());
//        log.show("vert", bot.odometry.getVerticalEncoderPosition());
//        log.show("Other pos", bot.lift.getPositionDown());
//        log.show("Current Power", bot.lift.motorUp.getPower());
//        log.show("PositionUp", bot.lift.getPositionUp());
//        log.show("Velocity", bot.lift.positionHolder.getVelocity());
//        log.show("Power", bot.lift.positionHolder.getOutput());



    }

    /**
     * Define the two teleops for each side
     */
    @TeleOp(name = "TerraOpBlue", group = "TeleOp")
    public static class TerraOpBlue extends TerraOp{{ fieldSide = FieldSide.BLUE; }}
    @TeleOp(name = "TerraOpRed", group = "TeleOp")
    public static class TerraOpRed extends TerraOpBlue{{ fieldSide = FieldSide.RED; }}
}



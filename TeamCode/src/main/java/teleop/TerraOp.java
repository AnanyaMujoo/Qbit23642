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
import static teleutil.button.Button.*;



public class TerraOp extends Tele{



    @Override
    public void initTele() {

        gph1.link(X, () -> bot.cancelAll());

//        gph1.link(Button.DPAD_DOWN,  bot::pauseAutoModules);
//        gph1.link(Button.DPAD_UP,  bot::resumeAutoModules);

        gph1.link(Button.LEFT_TRIGGER, automodules.OneDuck);
        gph1.link(Button.RIGHT_TRIGGER, lift::cycleLevelUp);
        gph1.link(Button.RIGHT_BUMPER, outtake::cycleOuttakeMode);
        gph1.link(Button.LEFT_BUMPER, outtake::cycleSharedMode);
        gph1.link(Button.RIGHT_STICK_BUTTON,  drive::cycleDriveUp);
        gph1.link(Button.LEFT_STICK_BUTTON,  () -> intake.moveRaw(-1));

        gph1.link(Button.A, automodules.IntakeCombined);

        gph1.link(Button.B, independents.Backward);
        gph1.link(Button.Y, independents.Forward);

        gph1.link(Button.DPAD_RIGHT, outtake::collectCap);
        gph1.link(Button.DPAD_DOWN, outtake::readyCap);
        gph1.link(Button.DPAD_LEFT, outtake::dropCap);
        gph1.link(Button.DPAD_UP,  outtake::startCap);

        gph2.link(Button.RIGHT_TRIGGER,  outtake::lock);
        gph2.link(Button.LEFT_TRIGGER,  outtake::drop);
        gph2.link(Button.RIGHT_BUMPER, OnTurnOnEventHandler.class, () -> intake.move(1));
        gph2.link(Button.RIGHT_BUMPER, OnTurnOffEventHandler.class, () -> intake.move(0));
        gph2.link(Button.LEFT_BUMPER,  () -> intake.move(-1));
        gph2.link(Button.LEFT_BUMPER, OnNotHeldEventHandler.class, () -> intake.move(0));
        gph2.link(Button.RIGHT_STICK_BUTTON,  outtake::turnToHorizontal);
        gph2.link(Button.LEFT_STICK_BUTTON,  outtake::turnToStart);
        gph2.link(Button.DPAD_RIGHT,  outtake::sharedTurretRight);
        gph2.link(Button.DPAD_DOWN,  outtake::turretCenter);
        gph2.link(Button.DPAD_LEFT,  outtake::sharedTurretLeft);

        lift.resetEncoder();

        intake.scale = 0.8;

//        drive.setIndependentMode(Modes.IndependentMode.USING);
    }

    @Override
    public void startTele() {
        outtake.midCap();
    }

    @Override
    public void loopTele() {
        drive.moveSmoothTele(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

        if(gamepad2.right_stick_y == 0){
            lift.holdPosition();
        }else {
            lift.move(-gamepad2.right_stick_y);
        }

        log.show("OuttakeMode", outtake.getOuttakeMode());
        log.show("SharedMode", outtake.getSharedMode());
        log.show("DriveMode", drive.getDriveMode());
        log.show("LevelMode", lift.getLevelMode());
        log.show("IndependentMode", drive.getIndependentMode());




//        log.show("wasrun", independentRunner.wasRun);
//        log.show("numsegs", independentRunner.numSegs);
//        log.show("Access", drive.checkAccess(User.BACK));
//        log.show("odmety pos", Arrays.toString(odometry.getPose()));
//        log.show("horz", odometry.getHorizontalEncoderPosition());
//        log.show("vert", odometry.getVerticalEncoderPosition());
//        log.show("Other pos", lift.getPositionDown());
//        log.show("Current Power", lift.motorUp.getPower());
//        log.show("PositionUp", lift.getPositionUp());
//        log.show("Velocity", lift.positionHolder.getVelocity());
//        log.show("Power", lift.positionHolder.getOutput());



    }

    /**
     * Define the two teleops for each side
     */
    @TeleOp(name = "TerraOpBlue", group = "TeleOp")
    public static class TerraOpBlue extends TerraOp{{ fieldSide = FieldSide.BLUE; }}
    @TeleOp(name = "TerraOpRed", group = "TeleOp")
    public static class TerraOpRed extends TerraOpBlue{{ fieldSide = FieldSide.RED; }}
}



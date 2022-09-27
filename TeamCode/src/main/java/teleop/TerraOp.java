package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import elements.FieldSide;
import teleutil.button.Button;
import teleutil.button.OnNotHeldEventHandler;
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

        gph1.link(Button.LEFT_TRIGGER, OneDuck);
        gph1.link(Button.RIGHT_TRIGGER, mecanumLift::cycleLevelUp);
        gph1.link(Button.RIGHT_BUMPER, mecanumOuttake::cycleOuttakeMode);
        gph1.link(Button.LEFT_BUMPER, mecanumOuttake::cycleSharedMode);
        gph1.link(Button.RIGHT_STICK_BUTTON,  mecanumDrive::cycleDriveUp);
        gph1.link(Button.LEFT_STICK_BUTTON,  () -> mecanumIntake.moveRaw(-1));

        gph1.link(Button.A, IntakeCombined);

        gph1.link(Button.B, independents.Backward);
        gph1.link(Button.Y, independents.Forward);

        gph1.link(Button.DPAD_RIGHT, mecanumOuttake::collectCap);
        gph1.link(Button.DPAD_DOWN, mecanumOuttake::readyCap);
        gph1.link(Button.DPAD_LEFT, mecanumOuttake::dropCap);
        gph1.link(Button.DPAD_UP,  mecanumOuttake::startCap);

        gph2.link(Button.RIGHT_TRIGGER,  mecanumOuttake::lock);
        gph2.link(Button.LEFT_TRIGGER,  mecanumOuttake::drop);
        gph2.link(Button.RIGHT_BUMPER, OnTurnOnEventHandler.class, () -> mecanumIntake.move(1));
        gph2.link(Button.RIGHT_BUMPER, OnTurnOffEventHandler.class, () -> mecanumIntake.move(0));
        gph2.link(Button.LEFT_BUMPER,  () -> mecanumIntake.move(-1));
        gph2.link(Button.LEFT_BUMPER, OnNotHeldEventHandler.class, () -> mecanumIntake.move(0));
        gph2.link(Button.RIGHT_STICK_BUTTON,  mecanumOuttake::turnToHorizontal);
        gph2.link(Button.LEFT_STICK_BUTTON,  mecanumOuttake::turnToStart);
        gph2.link(Button.DPAD_RIGHT,  mecanumOuttake::sharedTurretRight);
        gph2.link(Button.DPAD_DOWN,  mecanumOuttake::turretCenter);
        gph2.link(Button.DPAD_LEFT,  mecanumOuttake::sharedTurretLeft);

        mecanumLift.resetEncoder();

        mecanumIntake.scale = 0.8;

//        drive.setIndependentMode(Modes.IndependentMode.USING);
    }

    @Override
    public void startTele() {
        mecanumOuttake.midCap();
    }

    @Override
    public void loopTele() {
        mecanumDrive.moveSmoothTele(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

        if(gamepad2.right_stick_y == 0){
            mecanumLift.holdPosition();
        }else {
            mecanumLift.move(-gamepad2.right_stick_y);
        }

        log.show("OuttakeMode", mecanumOuttake.getOuttakeMode());
        log.show("SharedMode", mecanumOuttake.getSharedMode());
        log.show("DriveMode", mecanumDrive.getDriveMode());
        log.show("LevelMode", mecanumLift.getLevelMode());
        log.show("IndependentMode", mecanumDrive.getIndependentMode());




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



package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import automodules.AutoModuleUser;
import elements.FieldSide;
import global.Constants;
import global.Modes;
import teleutil.button.Button;
import teleutil.button.OnTurnOffEventHandler;
import teleutil.button.OnTurnOnEventHandler;

import static autoutil.reactors.MecanumJunctionReactor.junctionScanner;
import static global.General.bot;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;
import static global.General.voltageScale;
import static global.Modes.DriveMode.Drive.FAST;
import static global.Modes.DriveMode.Drive.MEDIUM;
import static global.Modes.GamepadMode.AUTOMATED;
import static global.Modes.HeightMode.Height.*;
import static teleutil.button.Button.DPAD_DOWN;
import static teleutil.button.Button.DPAD_LEFT;
import static teleutil.button.Button.DPAD_RIGHT;
import static teleutil.button.Button.DPAD_UP;
import static teleutil.button.Button.LEFT_BUMPER;
import static teleutil.button.Button.LEFT_TRIGGER;
import static teleutil.button.Button.RIGHT_BUMPER;
import static teleutil.button.Button.RIGHT_TRIGGER;

public class TerraOp extends Tele {

    @Override
    public void initTele() {
        gph1.link(Button.B, BackwardAllTele);
        gph1.link(Button.Y, ForwardTele);
        gph1.link(Button.X, bot::cancelMovements);
        gph1.link(Button.RIGHT_STICK_BUTTON, Modes.driveMode::cycleUp);
        gph1.link(Button.BACK, CycleMachine);

        gph1.link(LEFT_BUMPER, MoveToZero, AUTOMATED); // TODO TEST
        gph1.link(RIGHT_BUMPER, odometry::reset, AUTOMATED);

        gph1.link(RIGHT_TRIGGER, () -> lift.adjustHolderTarget(2.0));
        gph1.link(LEFT_TRIGGER,  () -> lift.adjustHolderTarget(-2.0));

        gph1.link(DPAD_UP, () -> lift.setHolderTarget(HIGH));
        gph1.link(DPAD_RIGHT, () ->  lift.setHolderTarget(MIDDLE));
        gph1.link(DPAD_LEFT, () ->  lift.setHolderTarget(MIDDLE));
        gph1.link(DPAD_DOWN, () -> lift.setHolderTarget(LOW));
        gph1.link(Button.A, OnTurnOnEventHandler.class, () -> Modes.driveMode.set(FAST));
        gph1.link(Button.A, OnTurnOffEventHandler.class, () -> Modes.driveMode.set(MEDIUM));

        gph2.link(DPAD_LEFT, outtake::closeClaw);
        gph2.link(DPAD_RIGHT, outtake::openClaw);

        gph2.link(DPAD_UP, outtake::flip);
        gph2.link(DPAD_DOWN, outtake::unFlip);

        lift.move(-0.12);
        camera.setScanner(junctionScanner);
        camera.start(false);
    }

    @Override
    public void startTele() {
        outtake.moveStart();
        bot.loadPose(); // TODO TEST
    }

    @Override
    public void loopTele() {

        drive.moveSmooth(gph1.ry, gph1.rx, gph1.lx);

        lift.move(gph2.ry);

        log.show("DriveMode", Modes.driveMode.get());
        log.show("HeightMode", Modes.heightMode.get());
//        log.show("Right", lift.motorRight.getPosition());
//        log.show("Left", lift.motorLeft.getPosition());
        log.show("Pose", odometry.getPose());
//        log.show("Voltage", bot.getVoltage());
//        log.show("Pitch", gyro.getPitch());
        log.show("GamepadMode", gamepad1.back ? AUTOMATED : Modes.GamepadMode.NORMAL);
    }


    @TeleOp(name = "TerraOpBlue", group = "TeleOp")
    public static class TerraOpBlue extends TerraOp {{ fieldSide = FieldSide.BLUE; }}
    @TeleOp(name = "TerraOpRed", group = "TeleOp")
    public static class TerraOpRed extends TerraOpBlue{{ fieldSide = FieldSide.RED; }}
}



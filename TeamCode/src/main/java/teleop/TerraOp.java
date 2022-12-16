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
import static global.General.fieldSide;
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

@TeleOp(name = "TerraOp", group = "TeleOp")
public class TerraOp extends Tele {

    @Override
    public void initTele() {

        /**
         * Gamepad 1 Normal
         */
        gph1.link(Button.B, BackwardAllTele);
        gph1.link(Button.Y, ForwardTele);
        gph1.link(Button.X, bot::cancelMovements);
        gph1.link(Button.A, () -> Modes.driveMode.set(MEDIUM));
        gph1.link(Button.RIGHT_STICK_BUTTON, Modes.driveMode::cycleUp);

        gph1.link(DPAD_UP, () -> lift.setHolderTarget(HIGH));
        gph1.link(DPAD_LEFT, () ->  lift.setHolderTarget(MIDDLE));
        gph1.link(DPAD_RIGHT, () ->  lift.setHolderTarget(LOW));
        gph1.link(DPAD_DOWN, () -> lift.setHolderTarget(GROUND));
        gph1.link(RIGHT_BUMPER, () -> lift.adjustHolderTarget(2.5));
        gph1.link(LEFT_BUMPER,  () -> lift.adjustHolderTarget(-2.5));
        gph1.link(RIGHT_TRIGGER, () -> {bot.cancelAutoModules(); if(lift.stackedMode < 5){ bot.addAutoModule(ForwardStackTele(lift.stackedMode)); lift.stackedMode++;}});
        gph1.link(LEFT_TRIGGER, () -> {bot.cancelAutoModules();  if(lift.stackedMode > 0){ bot.addAutoModule(ForwardStackTele(lift.stackedMode)); lift.stackedMode--;}});

        /**
         * Gamepad 1 Automated
         */
        gph1.link(Button.A, MoveToCycleStart, AUTOMATED);
        gph1.link(RIGHT_TRIGGER, CycleMachine, AUTOMATED);
        gph1.link(LEFT_TRIGGER, CycleMediumMachine, AUTOMATED);

        gph1.link(Button.B, () -> {odometry.reset(); bot.cancelIndependents(); bot.addIndependent(Cycle);}, AUTOMATED);
        gph1.link(Button.Y, () -> {odometry.reset(); bot.cancelIndependents(); bot.addIndependent(CycleMedium);}, AUTOMATED);
        gph1.link(Button.X, () -> {lift.circuitMode = true; Modes.gameplayMode = Modes.GameplayMode.CIRCUIT_PICK;}, () -> {lift.circuitMode = false; Modes.gameplayMode = Modes.GameplayMode.CYCLE;});

        gph1.link(RIGHT_BUMPER, odometry::reset, AUTOMATED);
        gph1.link(LEFT_BUMPER, MoveToZero, AUTOMATED);


        /**
         * Gamepad 2 Manual
         */
        gph2.link(RIGHT_BUMPER, outtake::closeClaw);
        gph2.link(LEFT_BUMPER, outtake::openClaw);
        gph2.link(RIGHT_TRIGGER, outtake::flip);
        gph2.link(LEFT_TRIGGER, outtake::unFlip);


        /**
         * Start code
         */
        lift.move(-0.12);
        bot.loadLocationOnField();
    }

    @Override
    public void startTele() {
        outtake.readyStart();
        outtake.unFlip();
        bot.loadPose();
    }

    @Override
    public void loopTele() {

        drive.moveSmooth(gph1.ry, gph1.rx, gph1.lx);

        lift.move(gph2.ry);

        log.show("DriveMode", Modes.driveMode.get());
        log.show("HeightMode", Modes.heightMode.get());
        log.show("GamepadMode", gph1.isBackPressed() ? AUTOMATED : Modes.GamepadMode.NORMAL);
        log.show("StackedMode", lift.stackedMode == 0 ? "N/A" : 6-lift.stackedMode);
        log.show("GameplayMode", Modes.gameplayMode);

//        log.show("Right", lift.motorRight.getPosition());
//        log.show("Left", lift.motorLeft.getPosition());
//        log.show("Pose", odometry.getPose());
//        log.show("SavedPose", bot.getSavedPose());
//        log.show("Voltage", bot.getVoltage());
//        log.show("Pitch", gyro.getPitch());
    }
}



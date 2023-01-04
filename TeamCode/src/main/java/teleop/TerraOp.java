package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import automodules.AutoModule;
import autoutil.vision.JunctionScanner;
import autoutil.vision.JunctionScannerAll;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Modes;
import math.polynomial.Linear;
import teleutil.button.Button;
import util.template.Mode;
import util.template.Precision;

import static global.General.bot;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;
import static global.Modes.AttackMode.STICKY;
import static global.Modes.GamepadMode.AUTOMATED;
import static global.Modes.GamepadMode.NORMAL;
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

    private final Pose target = new Pose(0,18.5, -5);



    private final JunctionScannerAll junctionScannerAll = new JunctionScannerAll();

    @Override
    public void initTele() {

        /**
         * Gamepad 1 Normal
         */
        gph1.link(Button.B, BackwardAllTele);
        gph1.link(Button.Y, () -> {if(lift.circuitMode) { gameplayMode.set(GameplayMode.CIRCUIT_PICK);} bot.addAutoModule(ForwardAll.check());});
        gph1.link(Button.X, bot::cancelMovements);
        gph1.link(Button.A, () -> driveMode.set(Drive.MEDIUM));
        gph1.link(Button.RIGHT_STICK_BUTTON, driveMode::cycleUp);

        gph1.link(DPAD_UP, LiftHigh);
        gph1.link(DPAD_LEFT, LiftMiddle);
        gph1.link(DPAD_RIGHT, LiftLow);
        gph1.link(DPAD_DOWN, LiftGround);
        gph1.link(RIGHT_BUMPER, () -> lift.adjustHolderTarget(2.5));
        gph1.link(LEFT_BUMPER,  () -> lift.adjustHolderTarget(-2.5));
        gph1.link(RIGHT_TRIGGER, new AutoModule(attackMode.ChangeMode(NORMAL)));
        gph1.link(LEFT_TRIGGER, new AutoModule(attackMode.ChangeMode(STICKY)));

        /**
         * Gamepad 1 Automated
         */
        gph1.link(Button.A, MoveToCycleStart, AUTOMATED);
        gph1.link(Button.B, CycleMachine, AUTOMATED);
        gph1.link(Button.Y, CycleMediumMachine, AUTOMATED);
        gph1.link(Button.X, () -> {lift.circuitMode = true; gameplayMode.set(GameplayMode.CIRCUIT_PICK);}, () -> {lift.circuitMode = false; gameplayMode.set(GameplayMode.CYCLE);}, AUTOMATED);
        gph1.link(RIGHT_TRIGGER, UprightCone, AUTOMATED);

        gph1.link(RIGHT_BUMPER, odometry::reset, AUTOMATED);
        gph1.link(LEFT_BUMPER, MoveToZero, AUTOMATED);
        gph1.link(DPAD_UP, ResetLift, AUTOMATED);


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
        lift.move(-0.2);
        bot.loadLocationOnField();

        camera.setScanner(junctionScannerAll); camera.startAndResume(false);
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

        log.show("DriveMode", driveMode.get());
        log.show("HeightMode", heightMode.get());
        log.show("GameplayMode", gameplayMode.get());
        log.show("AttackMode", attackMode.get());
        log.show("GamepadMode", gph1.isBackPressed() ? AUTOMATED : GamepadMode.NORMAL);


//        junctionScannerAll.message();
//        log.show("Right", lift.motorRight.getPosition());
//        log.show("Left", lift.motorLeft.getPosition());
//        log.show("Pose", odometry.getPose());
//        log.show("SavedPose", bot.getSavedPose());
//        log.show("Voltage", bot.getVoltage());
//        log.show("Pitch", gyro.getPitch());
    }

    @Override
    public void stopTele() {
        camera.halt();
    }
}



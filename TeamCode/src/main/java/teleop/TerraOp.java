package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import automodules.AutoModule;
import autoutil.vision.JunctionScannerAll;
import teleutil.button.Button;
import teleutil.button.OnNotHeldEventHandler;

import static global.General.bot;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;
import static global.Modes.AttackMode.ON_BY_DEFAULT;
import static global.Modes.AttackMode.PRESS_TO_ENABLE;
import static global.Modes.AttackStatus.ATTACK;
import static global.Modes.AttackStatus.REST;
import static global.Modes.Drive.FAST;
import static global.Modes.Drive.MEDIUM;
import static global.Modes.Drive.SLOW;
import static global.Modes.GamepadMode.AUTOMATED;
import static global.Modes.GamepadMode.NORMAL;
import static global.Modes.OuttakeStatus.DRIVING;
import static global.Modes.OuttakeStatus.PLACING;
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

    private final JunctionScannerAll junctionScannerAll = new JunctionScannerAll();

    @Override
    public void initTele() {

        outtake.arml.changePosition("start", 0.06);
        outtake.armr.changePosition("start", 0.06);

        /**
         * Gamepad 1 Normal
         */
        gph1.link(Button.B, BackwardAllTele);
        gph1.link(Button.Y, ForwardAll);
        gph1.link(Button.X, bot::cancelMovements);

        gph1.link(Button.A, () -> driveMode.set(Drive.FAST));
        gph1.link(Button.RIGHT_STICK_BUTTON, () -> driveMode.set(MEDIUM));

        gph1.link(DPAD_UP, () -> outtakeStatus.modeIs(PLACING), LiftHigh, High);
        gph1.link(DPAD_LEFT, () -> outtakeStatus.modeIs(PLACING), LiftMiddle, Middle);
        gph1.link(DPAD_RIGHT,  () -> outtakeStatus.modeIs(PLACING), LiftLow, Low);
        gph1.link(DPAD_DOWN,  () -> outtakeStatus.modeIs(PLACING), LiftGround, Ground);

        gph1.link(RIGHT_BUMPER, () -> outtakeStatus.modeIs(PLACING),() -> lift.adjustHolderTarget(2.5), () -> lift.adjustHolderTarget(5.0));
        gph1.link(LEFT_BUMPER, () -> outtakeStatus.modeIs(PLACING),() -> lift.adjustHolderTarget(-2.5), () -> lift.adjustHolderTarget(-5.0));


        gph1.link(LEFT_TRIGGER, () -> {bot.cancelAutoModules(); if(lift.stackedMode < 5){ bot.addAutoModule(ForwardStackTele(lift.stackedMode)); lift.stackedMode++;}});
        gph1.link(RIGHT_TRIGGER, () -> driveMode.set(FAST));
        gph1.link(RIGHT_TRIGGER, OnNotHeldEventHandler.class, () -> driveMode.set(MEDIUM));

//        gph1.link(LEFT_TRIGGER, () -> attackStatus.toggle(ATTACK, REST)); // LIFT MOVE
//        gph1.link(LEFT_TRIGGER, () -> attackMode.toggle(PRESS_TO_ENABLE, ON_BY_DEFAULT));

        /**
         * Gamepad 1 Automated
         */
        gph1.link(Button.A, MoveToCycleStart, AUTOMATED);
        gph1.link(Button.B, MachineCycle2, AUTOMATED);
        gph1.link(Button.Y, CycleMediumMachine, AUTOMATED);
        gph1.link(Button.X, () -> {lift.circuitMode = true; gameplayMode.set(GameplayMode.CIRCUIT_PICK); driveMode.set(MEDIUM);}, () -> {lift.circuitMode = false; gameplayMode.set(GameplayMode.CYCLE); driveMode.set(SLOW);}, AUTOMATED);
        gph1.link(RIGHT_TRIGGER, UprightCone, AUTOMATED);
        gph1.link(LEFT_TRIGGER, TakeOffCone, AUTOMATED);

        gph1.link(RIGHT_BUMPER, odometry::reset, AUTOMATED);
        gph1.link(LEFT_BUMPER, MoveToZero, AUTOMATED);
        gph1.link(DPAD_DOWN, ResetLift, AUTOMATED);


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

//        bot.loadLocationOnField();
//        camera.setScanner(junctionScannerAll);
//        camera.startAndResume(false);
//        JunctionScannerAll.resume();
    }

    @Override
    public void startTele() {
        outtake.readyStart();
        outtake.openClaw();
        bot.loadPose();
    }

    @Override
    public void loopTele() {

        drive.moveSmooth(gph1.ry, gph1.rx, gph1.lx);

        lift.move(gph2.ry);

        log.show("DriveMode", driveMode.get());
        log.show("HeightMode", heightMode.get());
        log.show("GameplayMode", gameplayMode.get());
        log.show("StackedMode", lift.stackedMode == 0 ? "N/A" : 6-lift.stackedMode);

//        log.show("OuttakeStatus", outtakeStatus.get());

//        log.show("GamepadMode", gph1.isBackPressed() ? AUTOMATED : GamepadMode.NORMAL);

        //        log.show("AttackStatus", attackStatus.get());
//        log.show("AttackMode", attackMode.get());

//        log.show("Is", bot.indHandler.isIndependentRunning());

//        junctionScannerAll.message();
//        log.show("Right", lift.motorRight.getPosition());
//        log.show("Left", lift.motorLeft.getPosition());
//        log.show("Pose", odometry.getPose());
//        log.show("SavedPose", bot.getSavedPose());
//        log.show("Voltage", bot.getVoltage());
//        log.show("Pitch", gyro.getPitch());
    }

//    @Override
//    public void stopTele() {
//        camera.halt();
//    }
}



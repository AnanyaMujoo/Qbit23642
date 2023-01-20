package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import automodules.AutoModule;
import automodules.AutoModuleUser;
import autoutil.vision.JunctionScannerAll;
import math.polynomial.Linear;
import robotparts.electronics.output.OLed;
import teleutil.TeleTrack;
import teleutil.button.Button;
import teleutil.button.OnNotHeldEventHandler;
import util.codeseg.CodeSeg;

import static global.General.bot;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;
import static global.Modes.Drive.*;
import static global.Modes.GamepadMode.*;
import static global.Modes.GameplayMode.*;
import static global.Modes.OuttakeStatus.*;
import static global.Modes.Height.*;
import static teleutil.button.Button.*;
import static teleutil.TeleTrack.*;

@TeleOp(name = "TerraOp", group = "TeleOp")
public class TerraOp extends Tele {

    @Override
    public void initTele() {

        kappaBefore.disable(); kappaAfter.disable();

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


        gph1.link(LEFT_TRIGGER, () -> {bot.cancelAutoModules(); if(lift.stackedMode < 5){ bot.addAutoModule(AutoModuleUser.ForwardStackTele(lift.stackedMode)); lift.stackedMode++;}});
        gph1.link(RIGHT_TRIGGER, () -> driveMode.set(FAST));
        gph1.link(RIGHT_TRIGGER, OnNotHeldEventHandler.class, () -> driveMode.set(MEDIUM));

        /**
         * Gamepad 1 Automated
         */
//        gph1.link(Button.A, MoveToCycleStart, AUTOMATED);
        gph1.link(Button.Y, ForwardCircuitTelePlaceAll, AUTOMATED);
        gph1.link(Button.B, MachineCycle2, AUTOMATED);
//        gph1.link(Button.Y, CycleMediumMachine, AUTOMATED);
        gph1.link(Button.X, () -> {lift.circuitMode = true; gameplayMode.set(GameplayMode.CIRCUIT_PICK); driveMode.set(MEDIUM);}, () -> {lift.circuitMode = false; gameplayMode.set(GameplayMode.CYCLE); driveMode.set(SLOW);}, AUTOMATED);
        gph1.link(RIGHT_TRIGGER, UprightCone, AUTOMATED);
        gph1.link(LEFT_TRIGGER, TakeOffCone, AUTOMATED);

//        gph1.link(RIGHT_BUMPER, odometry::reset, AUTOMATED);
//        gph1.link(LEFT_BUMPER, MoveToZero, AUTOMATED);

        gph1.link(RIGHT_BUMPER, AutoModuleUser::enableKappa, AUTOMATED);
        gph1.link(LEFT_BUMPER, AutoModuleUser::disableKappa, AUTOMATED);

        gph1.link(DPAD_DOWN, ResetLift, AUTOMATED);
        gph1.link(DPAD_UP, RetractOdometry, AUTOMATED);
        gph1.link(DPAD_RIGHT, EngageOdometry, AUTOMATED);


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
    }

    @Override
    public void startTele() {
        outtake.readyStart();
        outtake.openClaw();
    }

    @Override
    public void loopTele() {
        double cutoff = 90;
        if(time > cutoff){
            if(time < cutoff+20) {
                Linear rate = new Linear(0.5, 1.0, 20.0);
                leds.pulse(OLed.LEDColor.GREEN, OLed.LEDColor.RED, rate.f(time - cutoff));
            }else{
                leds.setColor(OLed.LEDColor.RED);
            }
        }else if(!gameplayMode.modeIs(CYCLE)){
            if(heightMode.modeIs(HIGH)){
                leds.pulse(OLed.LEDColor.GREEN, 0.5);
            }else if(heightMode.modeIs(MIDDLE)){
                leds.pulse(OLed.LEDColor.ORANGE, 0.5);
            }else if(heightMode.modeIs(LOW)){
                leds.pulse(OLed.LEDColor.RED, 0.5);
            }else if(heightMode.modeIs(GROUND)){
                leds.setColor(OLed.LEDColor.OFF);
            }
        }


        drive.moveSmooth(gph1.ry, gph1.rx, gph1.lx);

        lift.move(gph2.ry);

        log.show("DriveMode", driveMode.get());
        log.show("HeightMode", heightMode.get());
        log.show("GameplayMode", gameplayMode.get());
        log.show("StackedMode", lift.stackedMode == 0 ? "N/A" : 6-lift.stackedMode);
        log.show("TrackStatus", kappaBefore.isEnabled() ? "Kappa" : "None");

//        log.show("OuttakeStatus", outtakeStatus.get());

//        log.show("Kappa Size", kappaBefore.steps.size());
//        log.show("Kappa #", kappaBefore.stepNumber);

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

}



package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import automodules.AutoModule;
import automodules.AutoModuleUser;
import autoutil.vision.JunctionScannerAll;
import elements.FieldPlacement;
import math.polynomial.Linear;
import robotparts.electronics.output.OLed;
import teleutil.TeleTrack;
import teleutil.button.Button;
import teleutil.button.OnNotHeldEventHandler;
import util.codeseg.CodeSeg;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;
import static global.Modes.GamepadMode.*;
import static global.Modes.OuttakeStatus.*;
import static global.Modes.Height.*;
import static teleutil.button.Button.*;
import static teleutil.TeleTrack.*;

@TeleOp(name = "TerraOp", group = "TeleOp")
public class TerraOp extends Tele {

//    private final JunctionScannerAll junctionScannerAll = new JunctionScannerAll();

    @Override
    public void initTele() {

        bot.loadLocationOnField();

        fieldPlacement = FieldPlacement.LOWER;

        outtake.changeArmPosition("start", 0.06);

        /**
         * Gamepad 1 Normal
         */
        gph1.link(Button.Y, heightMode.isMode(HIGH), () -> {if(lift.high){bot.cancelAutoModules(); bot.addAutoModule(BackwardPlaceHighTele);}else{bot.addAutoModule(BackwardHighTele.check());}}, () -> bot.addAutoModule(BackwardGrabHighTele));
        gph1.link(Button.X, heightMode.isMode(MIDDLE), () -> bot.addAutoModule(BackwardMiddleTele.check()), () -> bot.addAutoModule(BackwardGrabMiddleTele));
        gph1.link(Button.B, heightMode.isMode(LOW), () -> bot.addAutoModule(BackwardLowTele.check()), () -> bot.addAutoModule(BackwardGrabLowTele));
        gph1.link(Button.A, heightMode.isMode(GROUND), () -> {if(lift.ground){bot.cancelAutoModules(); bot.addAutoModule(BackwardPlaceGroundTele);}else{bot.addAutoModule(BackwardGroundTele.check());}}, () -> bot.addAutoModule(BackwardGrabGroundTele));


        gph1.link(DPAD_DOWN, () -> !bot.isMachineRunning(), () -> {bot.cancelAutoModules(); bot.addAutoModule(ForwardTeleBottom);}, () -> odometry.adjustDown(1.0));
        gph1.link(DPAD_UP, () -> !bot.isMachineRunning(), () -> {bot.cancelAutoModules(); bot.addAutoModule(UprightCone);}, () -> odometry.adjustUp(1.0));
        gph1.link(DPAD_LEFT, () -> !bot.isMachineRunning(), () -> {lift.high = true; bot.addAutoModule(TakeOffCone);}, () -> odometry.adjustLeft(1.0));
        gph1.link(DPAD_RIGHT, () -> !bot.isMachineRunning(), () -> {}, () -> odometry.adjustRight(1.0));

        gph1.link(RIGHT_BUMPER, () -> outtakeStatus.modeIs(PLACING), () -> lift.adjustHolderTarget(2.5), () -> lift.adjustHolderTarget(5.0));
        gph1.link(LEFT_BUMPER, () -> outtakeStatus.modeIs(PLACING), () -> lift.adjustHolderTarget(-2.5), () -> lift.adjustHolderTarget(-5.0));


        gph1.link(LEFT_TRIGGER, () -> !MachineCycle.isRunning(), () -> {bot.cancelAutoModules(); if(lift.stackedMode < 5){ lift.stacked = true; bot.addAutoModule(AutoModuleUser.ForwardStackTele(lift.stackedMode)); lift.stackedMode++;}else{lift.stackedMode = 0; }}, MachineCycle::skipToLast);
        gph1.link(RIGHT_TRIGGER, () -> !bot.isMachineRunning(), () -> drive.slow = !drive.slow, bot::pauseOrPlayMachine);

//        gph1.link(RIGHT_TRIGGER, () -> drive.slow = true);
//        gph1.link(RIGHT_TRIGGER, OnNotHeldEventHandler.class, () -> drive.slow = false);

        /**
         * Gamepad 1 Automated
         */
        gph1.link(Button.A, MoveToCycleStart, AUTOMATED);
        gph1.link(Button.B, MachineCycle, AUTOMATED);
        gph1.link(Button.X, bot::cancelMovements, AUTOMATED);
        gph1.link(Button.Y, MachineCycleExtra, AUTOMATED);

        gph1.link(RIGHT_BUMPER, odometry::reset, AUTOMATED);
        gph1.link(LEFT_BUMPER, MoveToZero, AUTOMATED);
//
//        gph1.link(RIGHT_TRIGGER, AutoModuleUser::enableKappa, AUTOMATED);
//        gph1.link(LEFT_TRIGGER, AutoModuleUser::disableKappa, AUTOMATED);

        gph1.link(DPAD_DOWN, () -> {bot.cancelAutoModules(); bot.addAutoModule(ResetLift);}, AUTOMATED);

//        gph1.link(DPAD_UP, RetractOdometry, AUTOMATED);
//        gph1.link(DPAD_RIGHT, EngageOdometry, AUTOMATED);


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

        outtake.readyStart();
        outtake.openClaw();

//        camera.setScanner(junctionScannerAll);
//        camera.start(false);
//        camera.resume();
    }
//
//    @Override
//    public void startTele() {
//        outtake.readyStart();
//        outtake.openClaw();
//    }

    @Override
    public void loopTele() {
//        double cutoff = 90;
//        if(time > cutoff){
//            if(time < cutoff+20) {
//                Linear rate = new Linear(0.5, 1.0, 20.0);
//                leds.pulse(OLed.LEDColor.GREEN, OLed.LEDColor.RED, rate.f(time - cutoff));
//            }else{
//                leds.setColor(OLed.LEDColor.RED);
//            }
//        }else if(!bot.indHandler.isIndependentRunning()){
//            if(heightMode.modeIs(HIGH)){
//                leds.pulse(OLed.LEDColor.GREEN, 0.5);
//            }else if(heightMode.modeIs(MIDDLE)){
//                leds.pulse(OLed.LEDColor.ORANGE, 0.5);
//            }else if(heightMode.modeIs(LOW)){
//                leds.pulse(OLed.LEDColor.RED, 0.5);
//            }else if(heightMode.modeIs(GROUND)){
//                leds.setColor(OLed.LEDColor.OFF);
//            }
//        }

        drive.moveSmooth(gph1.ry, gph1.rx, gph1.lx);

        lift.move(gph2.ry);

        log.show("DriveMode", drive.slow ? "SLOW" : "FAST");
        log.show("StackedMode", lift.stackedMode == 0 ? "N/A" : 6-lift.stackedMode);
//        log.show("TrackStatus", kappaBefore.isEnabled() ? "Kappa" : "None");
//        log.show("OuttakeStatus", outtakeStatus.get());

//        log.show("Kappa Size", kappaBefore.steps.size());
//        log.show("Kappa #", kappaBefore.stepNumber);

//        log.show("GamepadMode", gph1.isBackPressed() ? AUTOMATED : GamepadMode.NORMAL);


//        log.show("Is", bot.indHandler.isIndependentRunning());
//
//        log.show("heading", gyro.getHeading());

//        junctionScannerAll.message();
//        log.show("Right", lift.motorRight.getPosition());
//        log.show("Left", lift.motorLeft.getPosition());
//        log.show("Pose", odometry.getPose());
//        log.show("SavedPose", bot.getSavedPose());
//        log.show("Voltage", bot.getVoltage());
//        log.show("Pitch", gyro.getPitch());
    }

}



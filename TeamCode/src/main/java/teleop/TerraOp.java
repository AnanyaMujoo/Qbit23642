package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import automodules.AutoModule;
import automodules.AutoModuleUser;
import autoutil.vision.JunctionScannerAll;
import elements.FieldPlacement;
import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import math.polynomial.Linear;
import robotparts.electronics.output.OLed;
import teleutil.TeleTrack;
import teleutil.button.Button;
import teleutil.button.OnNotHeldEventHandler;
import util.codeseg.CodeSeg;
import util.template.Precision;

import static global.General.bot;
import static global.General.cameraMonitorViewId;
import static global.General.fieldPlacement;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;
import static global.Modes.Drive.MEDIUM;
import static global.Modes.Drive.SLOW;
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
        gph1.link(Button.Y, heightMode.isMode(HIGH), () -> {if(lift.high){ bot.addAutoModuleWithCancel(BackwardPlaceHighTele);}else{if(outtakeStatus.modeIs(DRIVING)){ driveMode.set(MEDIUM);  bot.addAutoModuleWithCancel(BackwardGrabHighTele);}else{ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(ForwardTeleHigh);}}}, () -> {driveMode.set(MEDIUM);  bot.addAutoModuleWithCancel(BackwardGrabHighTele);});
        gph1.link(Button.X, heightMode.isMode(MIDDLE), () -> {if(lift.mid){ bot.addAutoModuleWithCancel(BackwardPlaceMiddleTele);}else{if(outtakeStatus.modeIs(DRIVING)){ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabMiddleTele);}else{ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(ForwardTeleMiddle);}}}, () -> {driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabMiddleTele);});
        gph1.link(Button.B, heightMode.isMode(LOW), () -> {if(lift.low){ bot.addAutoModuleWithCancel(BackwardPlaceLowTele);}else{ if(outtakeStatus.modeIs(DRIVING)){ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabLowTele);}else{ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(ForwardTeleLow);}}}, () -> {driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabLowTele);});
        gph1.link(Button.A, heightMode.isMode(GROUND), () -> {if(lift.ground){ driveMode.set(SLOW); bot.addAutoModuleWithCancel(BackwardPlaceGroundTele);}else{if(outtakeStatus.modeIs(DRIVING)){ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabGroundTele);}else{ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(ForwardTeleGround);}}}, () -> {driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabGroundTele);});


        gph1.link(DPAD_DOWN, () -> !bot.isMachineRunning(), () -> {bot.cancelAutoModules(); if(lift.upright){lift.upright = false; bot.addAutoModule(FixCone);}else{bot.addAutoModule(ForwardTeleBottom);}}, () -> odometry.adjustUp(MachineCycleExtra.isRunning() ? 2.0 : 1.0));
        gph1.link(DPAD_UP, () -> !bot.isMachineRunning(), () -> {bot.cancelAutoModules(); lift.upright = true; bot.addAutoModule(UprightCone);}, () -> odometry.adjustDown(MachineCycleExtra.isRunning() ? 2.0 : 1.0));
        gph1.link(DPAD_LEFT, () -> !bot.isMachineRunning(), () -> {lift.high = true; bot.addAutoModule(TakeOffCone);}, () -> odometry.adjustRight(MachineCycleExtra.isRunning() ? 2.0 : 1.0));
        gph1.link(DPAD_RIGHT, () -> !bot.isMachineRunning(), () -> {bot.cancelAutoModules(); if(!lift.cap){bot.addAutoModule(CapGrab); lift.cap = true; }else{bot.addAutoModule(CapPick); lift.cap = false;}}, () -> odometry.adjustLeft(MachineCycleExtra.isRunning() ? 2.0 : 1.0));

        gph1.link(RIGHT_BUMPER, () -> lift.adjustHolderTarget(2.5));
        gph1.link(LEFT_BUMPER, () -> lift.adjustHolderTarget(-2.5));


        gph1.link(LEFT_TRIGGER, () -> !bot.isMachineRunning(), () -> {bot.cancelAutoModules(); if(lift.stackedMode < 5){ lift.stacked = true; bot.addAutoModule(AutoModuleUser.ForwardStackTele(lift.stackedMode)); lift.stackedMode++;}else{lift.stackedMode = 0; }}, () -> {if(MachineCycle.isRunning()){ bot.skipToLastMachine();}else if(MachineCycleExtra.isRunning()){ lift.skipping = true; bot.skipToLastImmediate();}});
        gph1.link(RIGHT_TRIGGER, () -> !bot.isMachineRunning(), () -> {if(!driveMode.modeIs(SLOW)){ drive.noStrafeLock = true; driveMode.set(SLOW);}else{ drive.noStrafeLock = false; driveMode.set(MEDIUM);}},() -> {if(MachineCycle.isRunning()){ bot.pauseOrPlayMachine(); }else{ bot.skipToNextMachine(); }});

//        gph1.link(RIGHT_TRIGGER, () -> drive.slow = true);
//        gph1.link(RIGHT_TRIGGER, OnNotHeldEventHandler.class, () -> drive.slow = false);

        /**
         * Gamepad 1 Automated
         */
        gph1.link(Button.A, MoveToCycleStart, AUTOMATED);
        gph1.link(Button.B, MachineCycle, AUTOMATED);
        gph1.link(Button.X, () -> {driveMode.set(MEDIUM); bot.cancelMovements();}, AUTOMATED);
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
    @Override
    public void startTele() {
        lift.resetLift();
    }

    @Override
    public void loopTele() {

        if(bot.isMachineRunning() && bot.getMachine().pause && lift.adjust){

            if(gph1.ry > 0.9 || gph1.rx > 0.9 || gph1.lx > 0.9){
                lift.adjust = false;
                driveMode.set(MEDIUM);
            }

            Pose pose = bot.getMachine().getCurrentIndependent().getEndPose();
            Pose error = pose.getSubtracted(odometry.getPose());
            Linear rt = new Linear(0.05, 0.003);
            Vector power = new Vector();
            Linear rm = new Linear(0.15, 0.008);
            if(error.getLength() > 0.3) {
                double d = Precision.clip(error.getLength(), 5);
                power = error.getVector().getUnitVector().getScaled(rm.fodd(d)).getRotated(-odometry.getHeading());
            }
            drive.move(Precision.clip(power.getY(), 0.2)+(gph1.ry*0.4), Precision.clip(power.getX(), 0.2)+(gph1.rx*0.4), Precision.clip(-rt.fodd(pose.getAngle()-odometry.getHeading()), 0.2)+(gph1.lx*0.4));
        }else{
            drive.moveSmooth(gph1.ry, gph1.rx, gph1.lx);
        }


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



        lift.move(gph2.ry);

        log.show("DriveMode", driveMode.get());
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
//        log.show("TargetRight", lift.motorRight.getPositionHolder().getTarget());
//        log.show("TargetLeft", lift.motorLeft.getPositionHolder().getTarget());
//        log.show("Pose", odometry.getPose());
//        log.show("SavedPose", bot.getSavedPose());
//        log.show("Voltage", bot.getVoltage());
//        log.show("Pitch", gyro.getPitch());
    }

}



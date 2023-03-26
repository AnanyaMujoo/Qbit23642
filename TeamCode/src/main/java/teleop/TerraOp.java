package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import javax.crypto.Mac;

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
import util.Timer;
import util.codeseg.CodeSeg;
import util.template.Precision;

import static global.General.bot;
import static global.General.cameraMonitorViewId;
import static global.General.fault;
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

    private Timer moveTimer = new Timer();

    @Override
    public void initTele() {

        bot.loadLocationOnField();

        fieldPlacement = FieldPlacement.LOWER;

        outtake.changeArmPosition("start", 0.06);


        /**
         * Gamepad 1 Normal
         */

        gph1.link(Button.Y, heightMode.isMode(HIGH), () -> { driveMode.set(MEDIUM); if(outtakeStatus.modeIs(DRIVING)){ bot.addAutoModuleWithCancel(BackwardGrabHighTele2);}else{ bot.addAutoModuleWithCancel(ForwardTeleHigh);}}, () -> {driveMode.set(MEDIUM); bot.addAutoModule(BackwardGrabHighTele2);});
        gph1.link(Button.X, heightMode.isMode(MIDDLE), () -> { driveMode.set(MEDIUM);if(outtakeStatus.modeIs(DRIVING)){  bot.addAutoModuleWithCancel(BackwardGrabMiddleTele2);}else{ bot.addAutoModuleWithCancel(ForwardTeleMiddle);}}, () -> {driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabMiddleTele2);});
        gph1.link(Button.B, heightMode.isMode(LOW), () -> {  driveMode.set(MEDIUM); if(outtakeStatus.modeIs(DRIVING)){ bot.addAutoModuleWithCancel(BackwardGrabLowTele2);}else{ bot.addAutoModuleWithCancel(ForwardTeleLow);}}, () -> {driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabLowTele2);});


//
//
//        gph1.link(Button.Y, heightMode.isMode(HIGH), () -> {if(lift.high){ bot.addAutoModuleWithCancel(BackwardPlaceHighTele);}else{if(outtakeStatus.modeIs(DRIVING)){ driveMode.set(MEDIUM);  bot.addAutoModuleWithCancel(BackwardGrabHighTele);}else{ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(ForwardTeleHigh);}}}, () -> {driveMode.set(MEDIUM);  bot.addAutoModuleWithCancel(BackwardGrabHighTele);});
//        gph1.link(Button.X, heightMode.isMode(MIDDLE), () -> {if(lift.mid){ bot.addAutoModuleWithCancel(BackwardPlaceMiddleTele);}else{if(outtakeStatus.modeIs(DRIVING)){ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabMiddleTele);}else{ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(ForwardTeleMiddle);}}}, () -> {driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabMiddleTele);});
//        gph1.link(Button.B, heightMode.isMode(LOW), () -> {if(lift.low){ bot.addAutoModuleWithCancel(BackwardPlaceLowTele);}else{ if(outtakeStatus.modeIs(DRIVING)){ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabLowTele);}else{ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(ForwardTeleLow);}}}, () -> {driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabLowTele);});
        gph1.link(Button.A, heightMode.isMode(GROUND), () -> {if(lift.ground){ driveMode.set(SLOW); bot.addAutoModuleWithCancel(BackwardPlaceGroundTele);}else{if(outtakeStatus.modeIs(DRIVING)){ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabGroundTele);}else{ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(ForwardTeleGround);}}}, () -> {driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabGroundTele);});


        gph1.link(DPAD_DOWN, () -> !bot.isMachineRunning(), () -> {bot.cancelAutoModules(); if(lift.upright){lift.upright = false; bot.addAutoModule(FixCone);}else{bot.addAutoModule(ForwardTeleBottom);}}, () -> {if(MachineCycle.isRunning()){odometry.adjustUp(1.0); }else{ lift.adjust = 1; }});
        gph1.link(DPAD_UP, () -> !bot.isMachineRunning(), () -> {bot.cancelAutoModules(); lift.upright = true; bot.addAutoModule(UprightCone);}, () -> {if(MachineCycle.isRunning()){odometry.adjustDown(1.0); }else{ lift.adjust = 2; } });
        gph1.link(DPAD_LEFT, () -> !bot.isMachineRunning(), () -> {lift.high = true; bot.addAutoModule(TakeOffCone);}, () -> {if(MachineCycle.isRunning()){odometry.adjustRight(1.0); }else{ lift.adjust = 3; }});
        gph1.link(DPAD_RIGHT, () -> !bot.isMachineRunning(), () -> {bot.cancelAutoModules(); if(!lift.cap){bot.addAutoModule(CapGrab); lift.cap = true; }else{bot.addAutoModule(CapPick); lift.cap = false;}}, () -> {if(MachineCycle.isRunning()){odometry.adjustLeft(1.0); }else{ lift.adjust = 4; }});

        gph1.link(RIGHT_BUMPER, () -> lift.adjustHolderTarget(2.5));
        gph1.link(LEFT_BUMPER, () -> lift.adjustHolderTarget(-2.5));

        gph1.link(LEFT_TRIGGER, () -> !bot.isMachineRunning(), () -> {bot.cancelAutoModules(); if(lift.stackedMode < 5){ lift.stacked = true; bot.addAutoModule(AutoModuleUser.ForwardStackTele(lift.stackedMode)); lift.stackedMode++;}else{lift.stackedMode = 0; }}, () -> {if(MachineCycle.isRunning()){ bot.skipToLastMachine();}else if(MachineCycleExtra.isRunning()){ lift.skipping = true; bot.skipToLastImmediate();}});
        gph1.link(RIGHT_TRIGGER, () -> !bot.isMachineRunning(), () -> {if(!driveMode.modeIs(SLOW)){ drive.noStrafeLock = true; driveMode.set(SLOW);}else{ drive.noStrafeLock = false; driveMode.set(MEDIUM);}}, () -> {if(MachineCycle.isRunning()){ bot.pauseOrPlayMachine(); }else{ lift.adjusting = false; lift.adjust = 0; bot.skipToNextMachine(); }});


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

        gph1.link(DPAD_DOWN, () -> {bot.cancelAutoModules(); driveMode.set(MEDIUM); bot.addAutoModule(ResetLift);}, AUTOMATED);

//        gph1.link(DPAD_UP, RetractOdometry, AUTOMATED);
//        gph1.link(DPAD_RIGHT, EngageOdometry, AUTOMATED);


        /**
         * Gamepad 2 Manual
         */
        gph2.link(RIGHT_BUMPER, outtake::closeClaw);
        gph2.link(LEFT_BUMPER, outtake::openClaw);
        gph2.link(RIGHT_TRIGGER, outtake::flip);
        gph2.link(LEFT_TRIGGER, outtake::unFlip);

        MachineCycleExtra.reset();
        MachineCycle.reset();



        /**
         * Start code
         */
        lift.move(-0.2);

        outtake.readyStart();
        outtake.openClaw();

        moveTimer.reset();

//        camera.setScanner(junctionScannerAll);
//        camera.start(false);
//        camera.resume();
    }
//
    @Override
    public void startTele() {
        lift.reset();
    }


    @Override
    public void loopTele() {

        if(MachineCycleExtra.isRunning() && bot.getMachine().pause && lift.adjust != 0){
            if(!lift.adjusting){
                lift.adjusting = true;
                moveTimer.reset();
            }
            Pose pose = bot.getMachine().getCurrentIndependent().getEndPose();
            double he = pose.getAngle()-odometry.getHeading();
            Linear h = new Linear(0.03, 0.005);
            double hp = 0;
            if(Math.abs(he) > 1.0){
                hp = -h.fodd(he);
            }
            Vector dir = new Vector();
            switch (lift.adjust){
                case 1: dir = new Vector(0, 1); break;
                case 2: dir = new Vector(0, -1); break;
                case 3: dir = new Vector(1, 0); break;
                case 4: dir = new Vector(-1, 0); break;
            }
            dir.rotate(-odometry.getHeading());
            dir.scaleX(0.4);
            dir.scaleY(0.2);
            drive.move(dir.getY(), dir.getX(), hp);
            if(moveTimer.seconds() > 0.1){
                odometry.adjust(odometry.getPose().getSubtracted(pose).getVector());
                lift.adjusting = false;
                lift.adjust = 0;
            }


//            Pose error = pose.getSubtracted(odometry.getPose());
//            double he = pose.getAngle()-odometry.getHeading();
//            Vector power = new Vector();
//            double hp = 0;
//            if(error.getLength() > 0.5) {
//                power = error.getVector().getUnitVector();
//                power.scaleX(0.2);
//                power.scaleY(0.1);
//                power = power.getRotated(-odometry.getHeading());
//            }
//            if(Math.abs(he) > 1.0){
//                hp = -Math.signum(he)*0.08;
//            }
//            drive.move(power.getY()+(gph1.ry*0.4), power.getX()+(gph1.rx*0.4), hp+(gph1.lx*0.4));
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

//
//        log.show("Running", MachineCycleExtra.isRunning());
//        log.show("Paused", MachineCycleExtra.pause);
//        log.show("Waiting",MachineCycleExtra.waiting);
//        log.show("Quit", MachineCycleExtra.quit);

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



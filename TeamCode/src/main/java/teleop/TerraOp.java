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
import static global.General.voltageScale;
import static global.Modes.Drive.MEDIUM;
import static global.Modes.Drive.SLOW;
import static global.Modes.GamepadMode.*;
import static global.Modes.OuttakeStatus.*;
import static global.Modes.Height.*;
import static teleutil.button.Button.*;
import static teleutil.TeleTrack.*;

@TeleOp(name = "TerraOp", group = "TeleOp")
public class TerraOp extends Tele {

    @Override
    public void initTele() {
        voltageScale = 1;

        /**
         * Gamepad 1 Normal
         */

        gph1.link(Button.Y, heightMode.isMode(HIGH).and(outtakeStatus.isMode(PLACING)), ForwardTeleHigh, BackwardGrabHighTele);
        gph1.link(Button.X, heightMode.isMode(MIDDLE).and(outtakeStatus.isMode(PLACING)), ForwardTeleMiddle, BackwardGrabMiddleTele);
        gph1.link(Button.B, heightMode.isMode(LOW).and(outtakeStatus.isMode(PLACING)), ForwardTeleLow, BackwardGrabLowTele);
        gph1.link(Button.A, heightMode.isMode(GROUND), () -> {if(lift.ground){ driveMode.set(SLOW); bot.addAutoModuleWithCancel(BackwardPlaceGroundTele);}else{if(outtakeStatus.modeIs(DRIVING)){ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabGroundTele);}else{ driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(ForwardTeleGround);}}}, () -> {driveMode.set(MEDIUM); bot.addAutoModuleWithCancel(BackwardGrabGroundTele);});

        gph1.link(DPAD_DOWN, () -> {if(lift.upright){lift.upright = false; bot.addAutoModuleWithCancel(FixCone);}else{bot.addAutoModuleWithCancel(ForwardTeleBottom);}});
        gph1.link(DPAD_UP, () -> {lift.upright = true; bot.addAutoModuleWithCancel(UprightCone);});
        gph1.link(DPAD_LEFT, () -> bot.addAutoModuleWithCancel(TakeOffCone));
        gph1.link(DPAD_RIGHT, () -> {if(!lift.cap){bot.addAutoModuleWithCancel(CapGrab); lift.cap = true; }else{bot.addAutoModuleWithCancel(CapPick); lift.cap = false;}});

        gph1.link(RIGHT_BUMPER, () -> lift.adjustHolderTarget(2.5));
        gph1.link(LEFT_BUMPER, () -> lift.adjustHolderTarget(-2.5));

        gph1.link(RIGHT_TRIGGER, () -> {if(lift.stackedMode < 5){ lift.stacked = true; bot.addAutoModuleWithCancel(AutoModuleUser.ForwardStackTele(lift.stackedMode)); lift.stackedMode++;}else{lift.stackedMode = 0; }});

        /**
         * Gamepad 1 Automated
         */
        gph1.link(Button.X, bot::cancelMovements, AUTOMATED);
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
        lift.move(-0.15);
        outtake.readyStart();
        outtake.openClaw();
    }

    @Override
    public void startTele() {
        lift.reset();
    }

    @Override
    public void loopTele() {

        drive.moveSmooth(gph1.ry, gph1.rx, gph1.lx);
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



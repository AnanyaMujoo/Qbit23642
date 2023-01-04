package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import automodules.AutoModuleUser;
import autoutil.vision.JunctionScanner;
import autoutil.vision.JunctionScanner2;
import elements.FieldSide;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Constants;
import global.Modes;
import math.polynomial.Linear;
import teleutil.button.Button;
import teleutil.button.OnTurnOffEventHandler;
import teleutil.button.OnTurnOnEventHandler;
import util.User;
import util.template.Precision;

import static autoutil.reactors.MecanumJunctionReactor.junctionScanner;
import static global.General.bot;
import static global.General.fieldSide;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;
import static global.General.voltageScale;
import static global.Modes.GamepadMode.AUTOMATED;
import static global.Modes.Height.GROUND;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;
import static global.Modes.Height.MIDDLE;
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



    private final JunctionScanner2 junctionScanner = new JunctionScanner2();

    @Override
    public void initTele() {

        /**
         * Gamepad 1 Normal
         */
        gph1.link(Button.B, BackwardAllTele);
        gph1.link(Button.Y, () -> {if(lift.circuitMode) { Modes.gameplayMode.set(Modes.GameplayMode.CIRCUIT_PICK);} bot.addAutoModule(ForwardAll.check());});
        gph1.link(Button.X, bot::cancelMovements);
        gph1.link(Button.A, () -> Modes.driveMode.set(Modes.Drive.MEDIUM));
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
        gph1.link(Button.B, CycleMachine, AUTOMATED);
        gph1.link(Button.Y, CycleMediumMachine, AUTOMATED);
        gph1.link(Button.X, () -> {lift.circuitMode = true; Modes.gameplayMode.set(Modes.GameplayMode.CIRCUIT_PICK);}, () -> {lift.circuitMode = false; Modes.gameplayMode.set(Modes.GameplayMode.CYCLE);}, AUTOMATED);
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
        lift.move(-0.12);
        bot.loadLocationOnField();

        camera.setScanner(junctionScanner);
        camera.start(false);
        JunctionScanner.resume();
    }

    @Override
    public void startTele() {
        outtake.readyStart();
        outtake.unFlip();
        bot.loadPose();
    }

    @Override
    public void loopTele() {

        Linear yCurve = new Linear(0.018, 0.06);
        Linear hCurve = new Linear(0.007, 0.04);


        Pose error = junctionScanner.getError();

        Vector pow = new Vector(0, yCurve.fodd(error.getY()));
        pow.rotate(-error.getAngle());
        double h = hCurve.fodd(error.getAngle());

        Pose power = drive.getMoveSmoothPower(gph1.ry, gph1.rx, gph1.lx);
        drive.move(Precision.clip(pow.getY(), 0.5) + power.getX(),  power.getY(), Precision.clip(h, 0.5) + power.getAngle());

        lift.move(gph2.ry);

        log.show("DriveMode", Modes.driveMode.get());
        log.show("HeightMode", Modes.heightMode.get());
        log.show("GameplayMode", Modes.gameplayMode.get());
        log.show("StackedMode", lift.stackedMode == 0 ? "N/A" : 6-lift.stackedMode);
        log.show("GamepadMode", gph1.isBackPressed() ? AUTOMATED : Modes.GamepadMode.NORMAL);

        // CREATE STICKY MODE

//        junctionScanner.message();

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



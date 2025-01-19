package teleop;

import static java.lang.Math.abs;
import static global.General.bot;
import static global.General.fieldSide;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Arrays;

import automodules.AutoModule;
import automodules.stage.Stage;
import elements.FieldSide;
import elements.SampleColor;
import teleutil.button.Button;
import util.Timer;
import util.template.Iterator;


public class TestOp extends Tele{

    @Override
    public void initTele() {
        claw.hold();
//        gph2.link(Button.X, bucket::bottom);
//        gph2.link(Button.Y, bucket::top);
//
//        gph2.link(Button.A, claw::hold);
//        gph2.link(Button.B, claw::release);

        //gph2.link(Button.Y, LiftDown);


        gph1.link(Button.Y, () ->{
            intake.intakeMode = true;
            bot.addAutoModule(Intake);
        });
        gph1.link(Button.B, () -> {
            intake.stopSpin = true;
            if (!intake.shimmy) {
                bot.addAutoModule(MoveIntake);
            } else {
                bot.addAutoModule(Shimmy);
            }
            intake.shimmy = !intake.shimmy;
        });
        gph1.link(Button.X, () -> {
            intake.stopSpin = true;
            intake.shimmy = false;
            bot.cancelAutoModules();
            bot.addAutoModule(MoveIntakeOut2);
        });
        gph1.link(Button.A, () -> {
            intake.stopSpin = true;
            intake.shimmy = false;
            intake.intakeMode = false;
            bot.addAutoModule(IntakeIn);
        });
        gph1.link(Button.RIGHT_BUMPER, () ->{
            if (intake.intakeMode) {
                intake.shimmy = false;
                intake.stopSpin = true;
                bot.addAutoModule(IntakeDeltaOut);
            } else {
                if (driveMode.modeIs(Drive.FAST)) {
                    driveMode.set(Drive.SLOW);
                } else {
                    driveMode.set(Drive.FAST);
                }
            }
        });
        gph1.link(Button.LEFT_BUMPER, () -> {
            if(intake.intakeMode) {
                intake.shimmy = false;
                intake.stopSpin = true;
                bot.addAutoModule(IntakeDeltaIn);
            }else {
                bot.cancelAutoModules();
                bot.addAutoModule(Dance);
            }
        });



        gph1.link(Button.RIGHT_TRIGGER, () -> {
            bot.addAutoModule(PrepareBucket);
        });
        gph1.link(Button.LEFT_TRIGGER, () -> {
            bot.addAutoModule(Deposit);
        });


        gph1.link(Button.DPAD_DOWN, () -> {
            bot.addAutoModule(Specimen);
        });
        gph1.link(Button.DPAD_LEFT, () -> {
            bot.addAutoModule(EmergencyBucket);
        });
        gph1.link(Button.DPAD_UP, () -> {
            bot.addAutoModule(PrepareRam);
        });
        gph1.link(Button.DPAD_RIGHT, () -> {
            bot.addAutoModule(Ram);
        });
//        gph1.link(Button.DPAD_LEFT, EmergencyBucket);
//        gph1.link(Button.DPAD_UP, PrepareRam);
//        gph1.link(Button.DPAD_RIGHT, Ram);

//        gph1.link(Button.DPAD_UP, () -> drive.machineMode = true, () -> drive.machineMode = false);
//
//        gph2.link(Button.A, () -> leds.setLED(true), () -> leds.setLED(false));



//        gph1.link(Button.RIGHT_BUMPER, () -> driveMode.set(Drive.SLOW), () -> driveMode.set(Drive.FAST));
//        gph1.link(Button.LEFT_BUMPER, () -> driveMode.set(Drive.SLOW), () -> driveMode.set(Drive.FAST));










//        gph2.link(Button.RIGHT_BUMPER, claw::disable);
//        gph2.link(Button.LEFT_BUMPER, claw::ready);
//        gph2.link(Button.DPAD_UP, claw::squeeze);
//        gph2.link(Button.DPAD_DOWN, bucket::hold);
//        gph2.link(Button.LEFT_TRIGGER, intake::flipOut);
//        gph2.link(Button.RIGHT_TRIGGER, intake::flipIn);


//        gph2.link(Button.X, () -> drive.machineMode = true, () -> drive.machineMode = false);

//        gph2.link(Button.LEFT_BUMPER, intake::openDoor);
//        gph2.link(Button.RIGHT_BUMPER, intake::closeDoor);

//        fieldSide = FieldSide.RED;

//        colorSensors.enableLED(false);

        intake.flipIn();
        bucket.specimen();

    }

    @Override
    public void startTele() {
//        act = false;
//        drive.machineMode = true;
    }

    @Override
    public void loopTele() {

        if(intake.sampleLoaded){
            intake.sampleLoaded = false;
            intake.stopSpin = true;
            intake.shimmy = false;
            if(intake.code == 2){
                intake.intakeMode = false;
//                bot.addAutoModule(new AutoModule(intake.moveTime(1, 0.1)));
                bot.addAutoModule(IntakeIn);
            }else if(intake.code == 1){
                bot.addAutoModule(MoveIntakeOut);
            }
            intake.code = 0;
        }



        liftOuttake.move(gph2.ly*0.2);
        liftIntake.move(gph2.lx*0.2);
        intake.move(gph2.ry*0.8);
        double speed;
        if (driveMode.modeIs(Drive.FAST)) {
            speed = 0.8;
        }else{
            speed = 0.3;
        }

        if(!bot.isIndependentRunning()) {
            drive.move(gph1.ry * speed, gph1.rx * speed, gph1.lx * speed * 0.6);
        }

//        log.show("MachineMode", drive.machineMode);
        log.show("DriveMode", driveMode.get());
        log.show("SampleColor", intake.color);
        log.show("IsSampleVertical", intake.isSampleVertical);
//        log.show("Pause", bot.machine.pause);
//        log.show("Skipping to next", bot.machine.skippingToNext);
        log.show("Odo", odometry.getPose());

//        log.show("Colors", Arrays.toString(colorSensors.getOuttakeColorRGB()));
//        log.show("Colors", Arrays.toString(colorSensors.redtolight()));
//        log.show("intake code", intake.code);

//        log.show("Color", colorSensors.getSampleColor());
//        log.show("Distance", colorSensors.getDistance());
//        log.show(Arrays.toString(colorSensors.getOuttakeColorRGB()));

//        log.show("avgcorrect", intake.avgCorrect);
//        log.show("samplecolor", colorSensors.getSampleColor().toString());


//        log.show("stages", bot.rfsHandler.getRfsQueue().size());
//        log.show("stage0", bot.rfsHandler.getRfsQueue().peek().isPause());
////        log.show("stage");
//        log.show("thread", bot.robotFunctionsThread.getStatus());


//
//        log.show("Right pos", liftOuttake.liftRight.getPosition());
//        log.show("Left pos", liftOuttake.liftLeft.getPosition());
//        log.show(liftOuttake.liftLeft.getPower());
//        log.show(liftOuttake.liftRight.getPower());

    }

    @TeleOp(name = "BlueTeleOp", group = "TeleOp")
    public static class BlueTeleOp extends TestOp {{fieldSide = FieldSide.BLUE; }}

    @TeleOp(name = "RedTeleOp", group = "TeleOp")
    public static class RedTeleOp extends TestOp {{fieldSide = FieldSide.RED; }}

}



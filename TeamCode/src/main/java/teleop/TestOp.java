package teleop;

import static java.lang.Math.abs;
import static global.General.bot;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import teleutil.button.Button;

@TeleOp(name = "TestOp", group = "TeleOp")
public class TestOp extends Tele {

    @Override
    public void initTele() {
        claw.hold();
        gph2.link(Button.X, bucket::bottom);
        gph2.link(Button.Y, bucket::top);
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
            if(!intake.shimmy) {
                bot.addAutoModule(MoveIntake);
            }else{
                bot.addAutoModule(Shimmy);
            }
            intake.shimmy = !intake.shimmy;
        });
        gph1.link(Button.X, () -> {
            intake.stopSpin = true;
            intake.shimmy = false;
            bot.addAutoModule(MoveIntakeOut);
        });
        gph1.link(Button.A, () -> {
            intake.stopSpin = true;
            intake.shimmy = false;
            intake.intakeMode = false;
            bot.addAutoModule(IntakeIn);
        });
        gph1.link(Button.RIGHT_BUMPER, () ->{
            if(intake.intakeMode) {
                intake.stopSpin = true;
                bot.addAutoModule(IntakeDeltaOut);
            }else{
                if(driveMode.modeIs(Drive.FAST)) {
                    driveMode.set(Drive.SLOW);
                }else{
                    driveMode.set(Drive.FAST);
                }
            }
        });
        gph1.link(Button.LEFT_BUMPER, () -> {
            if(intake.intakeMode) {
                intake.stopSpin = true;
                bot.addAutoModule(IntakeDeltaIn);
            }else{
                bot.addAutoModule(Dance);
            }
        });



        gph1.link(Button.RIGHT_TRIGGER, PrepareBucket);
        gph1.link(Button.LEFT_TRIGGER, Deposit);


        gph1.link(Button.DPAD_DOWN, Specimen);



        gph1.link(Button.DPAD_UP, PrepareRam);
        gph1.link(Button.DPAD_RIGHT, Ram);



//        gph1.link(Button.RIGHT_BUMPER, () -> driveMode.set(Drive.SLOW), () -> driveMode.set(Drive.FAST));
//        gph1.link(Button.LEFT_BUMPER, () -> driveMode.set(Drive.SLOW), () -> driveMode.set(Drive.FAST));










        gph2.link(Button.RIGHT_BUMPER, claw::disable);
        gph2.link(Button.LEFT_BUMPER, claw::ready);
        gph2.link(Button.DPAD_UP, claw::squeeze);
        gph2.link(Button.DPAD_DOWN, bucket::hold);
        gph2.link(Button.LEFT_TRIGGER, intake::flipOut);
        gph2.link(Button.RIGHT_TRIGGER, intake::flipIn);

//        gph2.link(Button.LEFT_BUMPER, intake::openDoor);
//        gph2.link(Button.RIGHT_BUMPER, intake::closeDoor);




    }

    @Override
    public void startTele() {

    }

    @Override
    public void loopTele() {
        liftOuttake.move(gph2.ly*0.2);
        liftIntake.move(gph2.lx*0.2);
        intake.move(gph2.ry*0.8);
        double speed;
        if (driveMode.modeIs(Drive.FAST)) {
            speed = 0.8;
        }else{
            speed = 0.3;
        }
        drive.move(gph1.ry * speed, gph1.rx * speed, gph1.lx * speed*0.6);

        log.show("DriveMode", driveMode.get());

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

}



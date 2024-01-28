package teleop;

import static java.lang.Math.abs;
import static global.General.bot;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import teleutil.button.Button;


@TeleOp(name = "QbitOp", group = "TeleOp")
public class QbitOp extends Tele {

    @Override
    public void initTele() {

        gph2.link(Button.DPAD_LEFT, outtake::moveFlipStart);
        gph2.link(Button.DPAD_UP, outtake::moveFlipMiddle);
        gph2.link(Button.DPAD_RIGHT, outtake::moveFlipEnd);

        gph1.link(Button.X, bot::cancelAutoModules);

        gph2.link(Button.RIGHT_BUMPER, hang::moveHangStart);
        gph2.link(Button.LEFT_BUMPER, hang::moveHangEnd);

        gph1.link(Button.RIGHT_BUMPER, outtake::moveClawClose);
        gph1.link(Button.LEFT_BUMPER, outtake::moveClawOpen);

        gph1.link(Button.A, drone::moveDroneStart);
        gph1.link(Button.B, drone::moveDroneRelease);

        gph1.linka(Button.DPAD_UP, lift.lifttarget(3));
        gph1.linka(Button.DPAD_DOWN, lift.lifttarget(-3));


        gph2.link(Button.B, Deposit);
        gph2.link(Button.X, Intake);
        gph2.link(Button.Y, Prepare(10));



//TODO --> fix the telemetry for buttons (make it accurate)




    }

    @Override
    public void startTele() {
        gyro.reset();
        oneOdometry.reset();
    }

    @Override
    public void loopTele() {
//        log.show("Flip (buttons):   b:start, y:middle, x:end");
//        log.show("Claw (bumpers):   right:close, left:open");
//        log.show("Drone (dpad):   up:start, down:release");
        drive.move(gph1.ry*0.8,gph1.rx*0.7,gph1.lx*0.4);
   //     drive.move((gph1.rt-gph1.lt)*0.5,gph1.lx*0.5,gph1.rx*0.4);

        intake.move(gph2.ry);
      //  lift.move(gph2.ly*0.2);
        hang.move(gph2.ly);
        if (gph2.rt>0.5){
            outtake.moveClawOpen();
        }
        if (gph2.lt>0.5){
            outtake.moveClawClose();

        }
        // TODO COMMENT THIS OUT WHEN DONE
//        log.show("Right Odometry Distance: ", oneOdometry.getRightOdometryDistance());
//        log.show("Back Odometry: ", oneOdometry.getBackOdometryDistance());
//        log.show("Gyro Heading: ", gyro.getHeading());

        log.show(colorSensors.leftPixelDistance());
        log.show(colorSensors.rightPixelDistance());
        log.show("Claw Left");
        log.show(outtake.clawLeft.getPosition());
        log.show("Claw Right");
        log.show(outtake.clawRight.getPosition());
        log.show(gyro.getHeading());






        log.show("Flip start, 1, B\n" +
                "Flip Middle, 1, Y\n" +
                "Flip End, 1, X\n"+
                "Claw Close, 1, Right Bumper\n" +
                "Claw Open, 1, Left Bumper\n" +
                "Drone Start, 1, Dpad Up\n" +
                "Drone Release, 1, Dpad down\n" +
                "Drive, 1, joysticks\n" +
                "Intake, 2, right up down joystick\n" +
                "Hang Servo, 2, button a\n");
        log.show("B Deposit");
        log.show("Y Prepare");
        log.show("X Intake");
        log.show("Hang Motor, 2, left joystick up down");
        log.show("Cancel Automodule, gamepad 2, dpad down");
        log.show("Lift Target",lift.target);





        log.show(lift.liftLeft.getPosition());
        log.show(lift.liftRight.getPosition());

    }

}



package teleop;

import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import automodules.AutoModuleUser;
import teleutil.button.Button;


@TeleOp(name = "QbitOp", group = "TeleOp")
public class QbitOp extends Tele {

    @Override
    public void initTele() {

        gph1.link(Button.B, outtake::moveFlipStart);
        gph1.link(Button.Y, outtake::moveFlipMiddle);
        gph1.link(Button.X, outtake::moveFlipEnd);

        gph1.link(Button.RIGHT_BUMPER, outtake::moveClawClose);
        gph1.link(Button.LEFT_BUMPER, outtake::moveClawOpen);

        gph1.link(Button.DPAD_UP, drone::moveDroneStart);
        gph1.link(Button.DPAD_DOWN, drone::moveDroneRelease);
//        gph2.link(Button.B, Deposit);
//        gph2.link(Button.X, Intake);
//        gph2.link(Button.Y, Prepare(1));






    }


    @Override
    public void loopTele() {
//        log.show("Flip (buttons):   b:start, y:middle, x:end");
//        log.show("Claw (bumpers):   right:close, left:open");
//        log.show("Drone (dpad):   up:start, down:release");
        drive.move(gph1.ry,gph1.rx,gph1.lx);
        intake.move(gph2.ry);
//        lift.move(gph2.ly);
        //hang.move(gph2.ly);

        log.show(colorSensors.leftPixelDistance());
        log.show(colorSensors.rightPixelDistance());



        log.show("Flip start, 1, B\n" +
                "Flip Middle, 1, Y\n" +
                "Flip End, 1, X\n"+
                "Claw Close, 1, Right Bumper\n" +
                "Claw Open, 1, Left Bumper\n" +
                "Drone Start, 1, Dpad Up\n" +
                "Drone Release, 1, Dpad down\n" +
                "Drive, 1, joysticks\n" +
                "Intake, 2, right up down joystick\n" +
                "Lift, left up down joystick\n");


        log.show(lift.liftLeft.getPosition());
        log.show(lift.liftRight.getPosition());

    }

}



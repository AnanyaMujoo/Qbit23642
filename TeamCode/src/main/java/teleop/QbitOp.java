package teleop;

import static global.General.gph1;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import teleutil.button.Button;


@TeleOp(name = "TestOp", group = "TeleOp")
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




    }


    @Override
    public void loopTele() {
        log.show("Flip (buttons):   b:start, y:middle, x:end");
        log.show("Claw (bumpers):   right:close, left:open");
        log.show("Drone (dpad):   up:start, down:release");

    }

}



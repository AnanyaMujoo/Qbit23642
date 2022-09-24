package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robot.RobotUser;
import robotparts.unused.CustomTestPart;
import teleutil.button.Button;

import static global.General.gph1;

@TeleOp(name = "TestOp", group = "TeleOp")
public class TestOp extends Tele {

    private final CustomTestPart part = customTestPart;


    @Override
    public void initTele() {
        gph1.link(Button.A, part::moveToStart);
        gph1.link(Button.RIGHT_BUMPER, part::closeClaw);
        gph1.link(Button.LEFT_BUMPER, part::openClaw);
        gph1.link(Button.B, part::moveToEnd);
    }

    @Override
    public void loopTele() {

    }
}

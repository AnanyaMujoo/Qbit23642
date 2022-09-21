package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import teleutil.button.Button;

import static global.General.gph1;

@TeleOp(name = "TestOp", group = "TeleOp")
public class TestOp extends Tele{

    @Override
    public void initTele() {
        gph1.link(Button.A, testPart::moveToStart);
        gph1.link(Button.RIGHT_BUMPER, testPart::closeClaw);
        gph1.link(Button.LEFT_BUMPER, testPart::openClaw);
        gph1.link(Button.B, testPart::moveToEnd);
    }

    @Override
    public void loopTele() {

    }
}

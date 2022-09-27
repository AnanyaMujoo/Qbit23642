package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import teleutil.button.Button;

import static global.General.gph1;

@TeleOp(name = "TestOp", group = "TeleOp")
public class TestOp extends Tele {



    @Override
    public void initTele() {
        gph1.link(Button.X, drive.MoveTime(0.3,0,0,1));
        gph1.link(Button.Y, DriveForward);
    }

    @Override
    public void loopTele() {

    }
}

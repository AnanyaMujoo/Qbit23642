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


    }

    @Override
    public void startTele() {

    }

    @Override
    public void loopTele() {
        intake.move(gph2.ry);

    }

}



package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import elements.FieldSide;
import teleutil.button.Button;
import teleutil.button.OnNotHeldEventHandler;
import teleutil.button.OnTurnOffEventHandler;
import teleutil.button.OnTurnOnEventHandler;

import static global.General.bot;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.independents;
import static global.General.log;
import static teleutil.button.Button.X;


public class TerraOp extends Tele {

    @Override
    public void initTele() {
        gph1.link(Button.A, intake.MoveTime(1, 1));
    }

    @Override
    public void loopTele() {
        drive.move(gph1.ry, gph1.rx, gph1.lx);
    }






    @TeleOp(name = "TerraOpBlue", group = "TeleOp")
    public static class TerraOpBlue extends TerraOp {{ fieldSide = FieldSide.BLUE; }}
    @TeleOp(name = "TerraOpRed", group = "TeleOp")
    public static class TerraOpRed extends TerraOpBlue{{ fieldSide = FieldSide.RED; }}
}



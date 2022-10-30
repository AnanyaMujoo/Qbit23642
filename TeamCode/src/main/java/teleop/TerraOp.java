package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.sql.Time;

import elements.FieldSide;
import teleutil.button.Button;
import teleutil.button.OnNotHeldEventHandler;
import teleutil.button.OnTurnOffEventHandler;
import teleutil.button.OnTurnOnEventHandler;
import util.Timer;

import static global.General.bot;
import static global.General.fault;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.independents;
import static global.General.log;
import static teleutil.button.Button.LEFT_BUMPER;
import static teleutil.button.Button.RIGHT_BUMPER;
import static teleutil.button.Button.X;


public class TerraOp extends Tele {

    boolean driveOnly = false;
    boolean slowMode = true;
    Timer timer = new Timer();

    @Override
    public void initTele() {
        if(!driveOnly) {
            gph1.link(Button.A, IntakeNew);
            gph1.link(Button.B, BackwardNew);
            gph1.link(Button.Y, ForwardNew);
        }
        gph1.link(X, () -> bot.cancelAll());
        gph1.link(Button.RIGHT_TRIGGER, DuckNew);
        gph1.link(RIGHT_BUMPER, () -> slowMode = true);
        gph1.link(LEFT_BUMPER, () -> slowMode = false);
    }

    @Override
    public void startTele() {
        timer.reset();
    }

    @Override
    public void loopTele() {
        if(!slowMode) {
            drive.move(gph1.ry, gph1.rx, gph1.lx);
        }else{
            drive.move(gph1.ry/2.0, gph1.rx/1.7, gph1.lx/1.5);
        }
        if(!driveOnly) {
            lift.move(gph2.ry);
        }
        if(timer.seconds() > 45 && driveOnly){
            fault.check("Time's Up!");
        }
    }


    @TeleOp(name = "TerraOpBlue", group = "TeleOp")
    public static class TerraOpBlue extends TerraOp {{ fieldSide = FieldSide.BLUE; }}
    @TeleOp(name = "TerraOpRed", group = "TeleOp")
    public static class TerraOpRed extends TerraOpBlue{{ fieldSide = FieldSide.RED; driveOnly = true; }}

}



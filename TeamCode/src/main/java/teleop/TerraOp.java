package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.sql.Time;

import elements.FieldSide;
import global.Modes;
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
import static teleutil.button.Button.LEFT_TRIGGER;
import static teleutil.button.Button.RIGHT_BUMPER;
import static teleutil.button.Button.RIGHT_TRIGGER;
import static teleutil.button.Button.X;

public class TerraOp extends Tele {

    @Override
    public void initTele() {
        gph1.link(Button.B, Backward);
        gph1.link(Button.Y, Forward);
        gph1.link(Button.X, bot::cancelAutoModules);
        gph1.link(Button.RIGHT_STICK_BUTTON,  Modes.CycleDrive());
        gph2.link(RIGHT_BUMPER, outtake::closeClaw);
        gph2.link(LEFT_BUMPER, outtake::openClaw);
        gph2.link(RIGHT_TRIGGER, outtake::moveEnd);
        gph2.link(LEFT_TRIGGER, outtake::moveStart);
    }

    @Override
    public void loopTele() {
        drive.moveSmooth(gph1.ry, gph1.rx, gph1.lx);
        log.show("DriveMode", Modes.getDriveMode());
    }


    @TeleOp(name = "TerraOpBlue", group = "TeleOp")
    public static class TerraOpBlue extends TerraOp {{ fieldSide = FieldSide.BLUE; }}
    @TeleOp(name = "TerraOpRed", group = "TeleOp")
    public static class TerraOpRed extends TerraOpBlue{{ fieldSide = FieldSide.RED; }}
}



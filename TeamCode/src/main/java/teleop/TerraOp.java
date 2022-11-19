package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.sql.Time;

import automodules.AutoModule;
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
import static teleutil.button.Button.DPAD_DOWN;
import static teleutil.button.Button.DPAD_LEFT;
import static teleutil.button.Button.DPAD_RIGHT;
import static teleutil.button.Button.DPAD_UP;
import static teleutil.button.Button.LEFT_BUMPER;
import static teleutil.button.Button.LEFT_TRIGGER;
import static teleutil.button.Button.RIGHT_BUMPER;
import static teleutil.button.Button.RIGHT_TRIGGER;
import static teleutil.button.Button.X;

public class TerraOp extends Tele {

    @Override
    public void initTele() {
        gph1.link(Button.B, BackwardAll);
        gph1.link(Button.Y, Forward);
        gph1.link(Button.X, bot::cancelAutoModules);
        gph1.link(Button.RIGHT_STICK_BUTTON, Modes::cycleDrive);
        gph1.link(RIGHT_BUMPER, Modes::cycleHeight);

        gph2.link(DPAD_LEFT, outtake::closeClaw);
        gph2.link(DPAD_RIGHT, outtake::openClaw);
        gph2.link(RIGHT_BUMPER, new AutoModule(outtake.stageEnd()));
        gph2.link(LEFT_BUMPER, new AutoModule(outtake.stageStart()));
        gph2.link(RIGHT_TRIGGER, new AutoModule(outtake.stageStart(0.0)));
        gph2.link(LEFT_TRIGGER, new AutoModule(outtake.stageOpen(0.0)));

        gph2.link(DPAD_UP, outtake::flip);
        gph2.link(DPAD_DOWN, outtake::unFlip);
    }

    @Override
    public void loopTele() {
        drive.moveSmooth(gph1.ry, gph1.rx, gph1.lx);

        lift.move(gph2.ry);

        log.show("DriveMode", Modes.getDriveMode());
        log.show("HeightMode", Modes.getHeightMode());
    }


    @TeleOp(name = "TerraOpBlue", group = "TeleOp")
    public static class TerraOpBlue extends TerraOp {{ fieldSide = FieldSide.BLUE; }}
    @TeleOp(name = "TerraOpRed", group = "TeleOp")
    public static class TerraOpRed extends TerraOpBlue{{ fieldSide = FieldSide.RED; }}
}



package teleop;

import static java.lang.Math.abs;
import static global.General.bot;
import static global.General.fieldSide;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Arrays;

import automodules.AutoModule;
import automodules.stage.Stage;
import elements.FieldSide;
import elements.SampleColor;
import robotparts.hardware.CampDrive;
import teleutil.button.Button;
import util.Timer;
import util.template.Iterator;


@TeleOp(name = "CampOp", group = "TeleOp")
public class CampOp extends Tele {

    @Override
    public void initTele() {


    }


    @Override
    public void loopTele() {
        CampDrive.move(gph1.ry,gph1.rx,gph1.lx);
    }
}
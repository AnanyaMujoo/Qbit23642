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

//TODO --> fix the telemetry for buttons (make it accurate)




    }

    @Override
    public void startTele() {

    }

    @Override
    public void loopTele() {
        drive.move(gph1.ry,-gph1.rx,gph1.lx*0.5);
        liftVertical.move(gph2.ry);
        lift.move(gph2.ly);


    }

}



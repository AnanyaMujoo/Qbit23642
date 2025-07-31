package teleop;

import static java.lang.Math.abs;
import static global.General.gph1;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "SummerOp", group = "TeleOp")

public class SummerOp extends Tele{


    @Override
    public void initTele(){
    //gph2.link(Button.RIGHT_TRIGGER, intake::flipIn);

    }

    @Override
    public void startTele() {
    }

    @Override
    public void loopTele() {
        drive.move(gph1.ry*0.5, gph1.rx*0.5, gph1.lx*0.5);
    }

}



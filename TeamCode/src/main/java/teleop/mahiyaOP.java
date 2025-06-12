package teleop;

import static global.General.gph1;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "mahiyaOP", group = "TeleOp")
public class mahiyaOP extends Tele {

    @Override
    public void initTele() {
        mahiya.move(1);
        mahiya.move(gph1.ly);





    }

    @Override
    public void startTele() {
        mahiya.move(0.2);
    }

    @Override
    public void loopTele() {
        mahiya.move(1);
    }
    }


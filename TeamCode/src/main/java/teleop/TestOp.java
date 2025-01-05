package teleop;

import static java.lang.Math.abs;
import static global.General.gph1;
import static global.General.gph2;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import teleutil.button.Button;

@TeleOp(name = "TestOp", group = "TeleOp")
public class TestOp extends Tele {

    @Override
    public void initTele() {
        gph2.link(Button.X, bucket::bottom);
        gph2.link(Button.Y, bucket::top);

        gph2.link(Button.A, claw::hold);
        gph2.link(Button.B, claw::release);



//TODO --> fix the telemetry for buttons (make it accurate)


    }

    @Override
    public void startTele() {

    }

    @Override
    public void loopTele() {
//        liftIntake.move(gph2.ry*0.1);
//        liftOuttake.move(gph2.ly*0.2);
////
////        lift.move(gph2.lx*0.1);
////        liftVertical.move(gph2.ly*0.1);
//        drive.move(gph1.ry*0.1,gph1.rx*0.1,gph1.lx*0.1);


    }

}



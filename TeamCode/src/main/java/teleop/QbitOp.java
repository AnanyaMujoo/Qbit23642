package teleop;

import static java.lang.Math.abs;
import static global.General.gph1;
import static global.General.gph2;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import teleutil.button.Button;

@TeleOp(name = "QbitOp", group = "TeleOp")
public class QbitOp extends Tele {

    @Override
    public void initTele() {

//TODO --> fix the telemetry for buttons (make it accurate)
        gph1.linka(Button.DPAD_RIGHT, lift.lifttarget(3));
        gph1.linka(Button.DPAD_LEFT, lift.lifttarget(-3));
        gph1.linka(Button.DPAD_UP, liftVertical.lifttarget(3));
        gph1.linka(Button.DPAD_DOWN, liftVertical.lifttarget(-3));
        gph1.link(Button.A, BucketDeposit());
        gph1.link(Button.B, ClawDeposit() );
        gph1.link(Button.X, ClawReceive());
        gph1.link(Button.Y, IntakeIn());
        gph1.link(Button.LEFT_BUMPER, IntakeOut());


        gph2.link(Button.X, HorizontalEmergency() );
        gph2.link(Button.Y, VerticalEmergency() );


    }

    @Override
    public void startTele() {

    }

    @Override
    public void loopTele() {
        drive.move(gph1.ry,gph1.rx,gph1.lx*0.5);
        liftVertical.move(gph2.ry);
        lift.move(gph2.rx);
        intake.move(gph2.ly);
        flip.move(gph2.lx);


    }

}



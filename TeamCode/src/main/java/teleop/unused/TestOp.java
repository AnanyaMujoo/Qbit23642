package teleop.unused;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import elements.FieldSide;
import teleop.Tele;
import teleutil.button.Button;

import static global.General.gph1;

@TeleOp(name = "TestOp", group = "TeleOp")
public class TestOp extends Tele {
    {
        fieldSide = FieldSide.RED;
    }



    @Override
    public void initTele() {
        gph1.link(Button.X, drive.MoveTime(0.3,0,0,1));
        gph1.link(Button.Y, DriveForward);
        gph1.link(Button.B, DriveField);
        gph1.link(Button.A, IntakeUntilFreightNew);

    }

    @Override
    public void loopTele() {

    }
}

package unittests.tele.hardware.Mecanum;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.gph1;

import teleutil.button.Button;
import unittests.tele.TeleUnitTest;

public class MecanumIntakeTest extends TeleUnitTest {
    @Override
    public void init() {
//        gph1.link(Button.A, MecanumAutoModules.IntakeUntilFreight);
//        gph1.link(Button.B, MecanumAutoModules.IntakeAndMoveForwardUntilFreight);
    }

    @Override
    protected void loop() {
        intake.move(-gamepad1.right_stick_y);
    }
}

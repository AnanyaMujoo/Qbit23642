package unused.mecanum;

import static global.General.gamepad1;

import unittests.tele.TeleUnitTest;

public class MecanumIntakeTest extends TeleUnitTest {
    @Override
    public void init() {
//        gph1.link(Button.A, MecanumAutoModules.IntakeUntilFreight);
//        gph1.link(Button.B, MecanumAutoModules.IntakeAndMoveForwardUntilFreight);
    }

    @Override
    protected void loop() {
        mecanumIntake.move(-gamepad1.right_stick_y);
    }
}

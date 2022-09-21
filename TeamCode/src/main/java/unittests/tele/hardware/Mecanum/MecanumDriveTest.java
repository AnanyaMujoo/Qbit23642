package unittests.tele.hardware.Mecanum;

import unittests.tele.TeleUnitTest;

import static global.General.*;

/**
 * NOTE: Uncommented
 */
public class MecanumDriveTest extends TeleUnitTest {

    @Override
    protected void loop() {
        log.show("Use right stick y (forward), right stick x (strafe), and left stick x (turn)");
        bot.drive.move(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
    }
}

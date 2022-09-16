package unittests.auto.framework;

import java.util.Arrays;

import unittests.auto.AutoUnitTest;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.log;

// TODO 4 FIX fix this
public class OdometryTest extends AutoUnitTest {

    @Override
    protected void run() {
        whileActive(() -> {
            bot.drive.moveSmooth(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
            log.show("Raw Horizontal Odometry Position", bot.odometry.getHorizontalEncoderPosition());
            log.show("Raw Vertical Odometry Position", bot.odometry.getVerticalEncoderPosition());
            log.show("Odometry Position", Arrays.toString(bot.odometry.getPose()));
        });
    }
}

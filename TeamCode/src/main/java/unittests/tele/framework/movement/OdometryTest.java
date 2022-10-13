package unittests.tele.framework.movement;

import java.util.Arrays;

import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.log;

public class OdometryTest extends TeleUnitTest {
    // TODO 4 NEW Create OdometryTest


    @Override
    public void init() {
        odometry.reset();
    }

    @Override
    protected void loop() {
        bot.mecanumDrive.moveSmooth(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
        log.show("Raw Horizontal Odometry Position", bot.odometry.getHorizontalEncoderPosition());
        log.show("Raw Vertical Odometry Position", bot.odometry.getVerticalEncoderPosition());
        log.show("Odometry Position", Arrays.toString(bot.odometry.getPose()));
    }
}

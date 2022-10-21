package unittests.tele.framework.movement;

import java.util.Arrays;

import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.gph1;
import static global.General.log;

public class OdometryTest extends TeleUnitTest {
    // TODO 4 NEW Create OdometryTest

    @Override
    protected void loop() {
        bot.drive.move(gph1.ry, gph1.rx, gph1.lx);
        log.show("Odometry Pose", twoOdometry);
    }
}

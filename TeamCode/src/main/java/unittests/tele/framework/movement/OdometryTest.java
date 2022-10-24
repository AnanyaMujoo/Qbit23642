package unittests.tele.framework.movement;

import java.util.Arrays;

import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.gph1;
import static global.General.log;

public class OdometryTest extends TeleUnitTest {
    // TODO 4 NEW Create OdometryTest tests Accuracy


    @Override
    protected void start() {
        twoOdometry.reset(); // Why do we need this?
    }

    @Override
    protected void loop() {
        bot.drive.move(gph1.ry/2.0, gph1.rx/2.0, gph1.lx/2.0);
        log.show("Odometry Pose", twoOdometry);
    }
}

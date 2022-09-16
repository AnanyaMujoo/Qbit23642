package unittests.auto.movement;

import static global.General.*;

import unittests.tele.TeleUnitTest;


// TODO 4 NEW Make this better
/**
 * NOTE: Uncommented
 */
public class OdometryTest extends TeleUnitTest {
    @Override
    public void init() {
//        bot.localPlane.add(
//                new Point(0, 0),
//                new Point(10, 0),
//                new Point(0, 10),
//                new Point(-10, 0),
//                new Point(0, -10)
//        );
    }

    @Override
    public void start() {
        // TOD: Display points and pos of robot
    }

    @Override
    protected void loop() {
//        bot.tankDrive.moveSmooth(-gamepad1.right_stick_y, gamepad1.left_stick_x);

        log.show("x", bot.odometry.getCurX());
        log.show("y", bot.odometry.getCurY());
        log.show("h", bot.odometry.getCurThetaDeg());
    }
}

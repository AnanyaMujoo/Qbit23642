package unittests.auto.framework.calibration;

import java.util.ArrayList;

import geometry.position.Pose;
import geometry.position.Vector;
import math.polynomial.Linear;
import unittests.auto.AutoUnitTest;
import util.Timer;
import util.template.Precision;

import static global.General.gamepad1;
import static global.General.log;
import static global.General.storage;

public class RestPowerCalib extends AutoUnitTest {

    @Override
    protected void run() {
        odometry.reset();
        Pose pow = new Pose();
        whileActive(() -> odometry.getY() < 0.5, () -> {
            pow.setY(pow.getY() + 0.001);
            drive.move(pow.getY(), 0, 0);
        });
        drive.halt();
        whileTime(() -> {log.show("Y Rest Power", pow.getY());}, 3);

        odometry.reset();
        whileActive(() -> odometry.getX() < 0.5, () -> {
            pow.setX(pow.getX() + 0.001);
            drive.move(0, pow.getX(), 0);
        });
        drive.halt();
        whileTime(() -> {log.show("X Rest Power", pow.getX());}, 3);

        odometry.reset();
        whileActive(() -> odometry.getHeading() < 1, () -> {
        pow.setAngle(pow.getAngle() + 0.001);
        drive.move(0, 0, -pow.getAngle());
        });
        drive.halt();
        whileTime(() -> {log.show("H Rest Power", pow.getAngle());}, 3);

        log.record("Rest", pow);
        whileTime(() -> {log.show("Rest Power", pow);}, 10);

        Pose target = new Pose(10,10,30);
        whileTime(() -> {
            Pose error = target.getSubtracted(odometry.getPose());
            Vector power = error.getVector().getUnitVector().getScaled(0.07);
            drive.move(power.getY(), power.getX(), -0.06*Math.signum(30-odometry.getHeading()));
        }, 30);

    }
}

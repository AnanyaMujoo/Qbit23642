package unittests.auto.framework.movement;

import unittests.auto.AutoUnitTest;
import util.template.Precision;

public class DistanceSensorStraightenTest extends AutoUnitTest {

    // TODO TEST
    @Override
    protected void run() {
        final double turn = 0.07;
        distanceSensors.ready();
        double initialDistance = distanceSensors.getRightDistance();
        Double[] currentDistance = {initialDistance};
        whileActive(() -> currentDistance[0] == initialDistance, () -> {
            drive.move(0,0,turn);
            currentDistance[0] = distanceSensors.getRightDistance();
        });
        drive.halt();
        double initialDistance2 = currentDistance[0];
        double initialHeading = gyro.getHeading();
        Double[] currentDistance2 = {initialDistance2};
        whileActive(() -> currentDistance2[0] < initialDistance2, () -> {
            drive.move(0,0,turn * Math.signum(initialDistance - initialDistance2));
            currentDistance2[0] = distanceSensors.getRightDistance();
        });
        drive.halt();
        double finalHeading = gyro.getHeading();
        double halfHeading = (finalHeading - initialHeading)/2.0;
        gyro.setHeading(halfHeading);
        pause(0.2);
        whileActive(() -> Precision.range(gyro.getHeading(), 1), () -> {
            drive.move(0,0,turn*Math.signum(gyro.getHeading()));
        });
    }
}

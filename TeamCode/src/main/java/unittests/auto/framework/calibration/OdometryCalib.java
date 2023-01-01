package unittests.auto.framework.calibration;

import java.util.ArrayList;

import math.polynomial.Linear;
import unittests.auto.AutoUnitTest;
import util.Timer;
import util.template.Precision;

import static global.General.gamepad1;
import static global.General.log;
import static global.General.storage;

public class OdometryCalib extends AutoUnitTest {

    boolean autoCalib = false;

    @Override
    protected void run() {
        int numTrials = 20;
        double distance = 150;

        ArrayList<Double> trialNumbers = new ArrayList<>();
        ArrayList<Double> xOffsets = new ArrayList<>();
        double startX;
        for (int i = 0; i < numTrials; i++) {
            log.show("Starting trial", (i+1));
            startX = odometry.getX();
            whileActive(() -> odometry.getY() < distance, () -> {
                drive.move(0.4, -0.1, 0);
            });
            drive.halt();
            double xOffset = odometry.getX() - startX;
            pause(0.5);
            log.show("Ending trial #" + (i+1) + ", xOffset", xOffset);

            Linear yCurve = new Linear(0.02, 0.07);

            whileActive(() -> odometry.getY() > 0, () -> {
                drive.move(-0.4, -0.1, 0);
            });
            drive.halt();
            whileTime(() -> {
                drive.move(0.4*-yCurve.fodd(odometry.getY()), -0.1, 0);
            }, 1.5);
            trialNumbers.add((double) (i+1));
            xOffsets.add(Precision.round(xOffset, 3));
            Timer timer1 = new Timer();
            timer1.reset();
            whileActive(() -> !gamepad1.b && timer1.seconds() < 1, () -> {});
            if(gamepad1.b){
                whileActive(() -> !gamepad1.y, () -> {});
                odometry.reset();
                storage.addData("OdometryXOffset", trialNumbers, xOffsets);
                storage.saveAndClear();
                pause(1);
            }
        }
        storage.addData("OdometryXOffset", trialNumbers, xOffsets);
        storage.saveAndClear();
    }
}

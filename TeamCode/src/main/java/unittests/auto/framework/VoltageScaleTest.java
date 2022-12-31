package unittests.auto.framework;

import java.util.ArrayList;

import math.polynomial.Linear;
import robot.RobotFramework;
import unittests.auto.AutoUnitTest;
import util.Timer;
import util.store.Data;

import static global.General.bot;
import static global.General.log;
import static global.General.storage;
import static global.General.voltageScale;

public class VoltageScaleTest extends AutoUnitTest {

    @Override
    protected void run() {
        int numTrials = 50;
        double distance = 150;
        voltageScale = 1;
        Timer timer = new Timer();
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Double> voltages = new ArrayList<>();
        for (int i = 0; i < numTrials; i++) {
            double voltage = RobotFramework.getBatteryVoltage();
            log.show("Starting trial #" + (i+1) + ", Voltage", voltage);
            pause(1);
            timer.reset();
            Linear yCurve = new Linear(0.02, 0.07);
            Linear xCurve = new Linear(0.02, 0.13);
            Linear hCurve = new Linear(0.01, 0.07);
            whileActive(() -> odometry.getY() < distance,() -> {
                drive.move(1, -xCurve.fodd(odometry.getX()), hCurve.fodd(odometry.getHeading()));
            });
            drive.halt();
            double time = timer.seconds();
            log.show("Ending trial #" + (i+1) + ", Time", time);
            pause(1);
            whileActive(() -> odometry.getY() > 0, () -> {
                drive.move(-Math.signum(odometry.getY()) * Math.min(Math.abs(yCurve.fodd(odometry.getY())), 0.5), -xCurve.fodd(odometry.getX()), hCurve.fodd(odometry.getHeading()));
            });
            drive.halt();
            whileTime(() -> {
                double scale = 0.5;
                drive.move(scale*-yCurve.fodd(odometry.getY()), scale*-xCurve.fodd(odometry.getX()), scale*hCurve.fodd(odometry.getHeading()));
            }, 1.5);
            drive.halt();
            voltages.add((int)(voltage*1000)/1000.0);
            times.add((int)(time*1000)/1000.0);
            pause(1);
            if(voltage < 12.1){
                break;
            }
        }
        log.record("Voltages", voltages);
        log.record("Times", times);
        storage.addData("VoltageScale", voltages, times);
        storage.saveItems();
    }
}

package robotparts.sensors;

import java.util.ArrayList;

import autoutil.profilers.Profiler;
import robot.BackgroundTask;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IGyro;
import util.Timer;

import static global.General.bot;


public class GyroSensors extends RobotPart {
    /**
     * Two gyro sensors from each expansion hub on different sides on the robot
     */
    private IGyro gsr, gsl;

    @Override
    public void init() {
        gsr = create("gsl", ElectronicType.IGYRO);
//        gsl = createGyro("gsl");
    }

    public double getHeading(){ return gsr.getHeading(); }

    public double getDeltaHeading() { return gsr.getDeltaHeading(); }

    public void update(){ gsr.updateHeading(); }

    @Override
    public void reset(){ gsr.reset(); }

}

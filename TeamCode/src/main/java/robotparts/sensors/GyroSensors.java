package robotparts.sensors;

import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IGyro;


public class GyroSensors extends RobotPart {
    /**
     * Two gyro sensors from each expansion hub on different sides on the robot
     */
    private IGyro gsr, gsl;

    @Override
    public void init() {
        gsr = create("gsl", ElectronicType.IGYRO);
//        gsl = createGyro("gsl");
        // TOD 5 Cant turn past 540 degs
    }

    public void setHeading(double heading){ gsr.setHeading(heading); }

    public double getHeading(){ return gsr.getHeading(); }

    public double getDeltaHeading() { return gsr.getDeltaHeading(); }

    public void update(){ gsr.updateHeading(); }

    @Override
    public void reset(){ gsr.reset(); }

}

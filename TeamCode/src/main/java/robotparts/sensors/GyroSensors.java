package robotparts.sensors;

import com.qualcomm.robotcore.hardware.IMU;

import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IGyro;


public class GyroSensors extends RobotPart {
    /**
     * Two gyro sensors from each expansion hub on different sides on the robot
     */
    private IGyro gs;

    @Override
    public void init() {
        gs = create("imu", ElectronicType.IGYRO);


//        gsl = createGyro("gsl");
        // TOD 5 Cant turn past 540 degs
    }

//    public void setHeading(double heading){ gs.setHeading(heading); }

    public double getHeading(){ return -gs.getHeading(); }
//    public double getPitch(){ return gsr.getPitch(); }
//    public double getPitchDerivative(){ return gsr.getPitchDerivative(); }

//    public double getDeltaHeading() { return gs.getDeltaHeading(); }

//    public void update(){ gs.update(); }

    @Override
    public void reset(){ gs.reset(); }

}
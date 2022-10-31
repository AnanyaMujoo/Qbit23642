package robotparts.sensors;

import java.util.ArrayList;

import autoutil.profilers.Profiler;
import robot.BackgroundTask;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IGyro;

import static global.General.bot;


public class GyroSensors extends RobotPart {
    /**
     * Two gyro sensors from each expansion hub on different sides on the robot
     */
    private IGyro gsr, gsl;
    private double lastAngle = 0;
    private double heading, lastHeading, deltaHeading = 0;
    private double start = 0;

    @Override
    public void init() {
        gsr = create("gsl", ElectronicType.IGYRO);
//        gsl = createGyro("gsl");
    }

    public void updateHeading(){
        double currentangle = gsr.getHeading();
        double deltaAngle = currentangle - lastAngle;
        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;
        heading  = heading + deltaAngle;
        deltaHeading = heading - lastHeading;
        lastHeading = heading;
        lastAngle = currentangle;
    }

    public double getHeading(){ return heading; }

    public double getDeltaHeading() { return deltaHeading; }

    /**
     * Get headings in radians and degrees
     * @return heading
     */
    public double getRightHeadingDeg() {
        return getRightHeadingDegRaw()-start;
    }

    public double getRightHeadingDegRaw() {
        double currentangle = -gsr.getHeading();
        double deltaAngle = currentangle - lastAngle;
        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;
        heading += deltaAngle;
        lastAngle = currentangle;
        return heading;
    }

    public double getRightHeadingRad() { return Math.toRadians(getRightHeadingDeg()); }
    public double getLeftHeadingDeg() { return gsl.getHeading(); }
    public double getLeftHeadingRad() { return Math.toDegrees(getRightHeadingDeg()); }

    @Override
    public void reset(){
        heading = 0; lastAngle = 0; deltaHeading = 0;
        start = getRightHeadingDegRaw();
    }




    public static double processThetaError(double error){
        while (error < -180) {
            error += 360;
        }
        while (error > 180) {
            error -= 360;
        }
        return error;
    }

    public static double processTheta(double ang){
        if (ang < -180) {
            ang += 360;
        } else if (ang > 180) {
            ang -= 360;
        }
        return ang;
    }

}

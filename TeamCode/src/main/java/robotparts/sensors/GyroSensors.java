package robotparts.sensors;

import geometry.circles.AngleType;
import robotparts.RobotPart;
import robotparts.electronics.input.IGyro;


public class GyroSensors extends RobotPart {
    /**
     * Two gyro sensors from each expansion hub on different sides on the robot
     */
    private IGyro gsr, gsl;
    private double lastAngle = 0;
    private double heading = 0;
    private double start = 0;

    @Override
    public void init() {
        gsr = createGyro("gsl");
//        gsl = createGyro("gsl");
    }

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

    public double getRightHeadingRad() { return AngleType.degToRad(getRightHeadingDeg()); }
    public double getLeftHeadingDeg() { return gsl.getHeading(); }
    public double getLeftHeadingRad() { return AngleType.degToRad(getRightHeadingDeg()); }

    public void reset(){
        heading = 0;
        lastAngle = 0;
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

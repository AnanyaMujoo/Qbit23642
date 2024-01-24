package robotparts.electronics.input;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import autoutil.Profiler;
import robotparts.Electronic;

/**
 * NOTE: Uncommented
 */

public class IGyro extends Electronic {

    private final IMU gyro;
//    private double heading, lastHeading, deltaHeading, startHeading = 0;
//    private double pitch, startPitch = 0;
//    private final Profiler pitchProfiler = new Profiler(() -> pitch);

    public IGyro(IMU gyro){

        this.gyro = gyro;
//        IMU.Parameters parameters = new IMU.Parameters();
//        parameters.angleUnit = IMU.AngleUnit.DEGREES;
//        parameters.accelUnit = IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        parameters.calibrationDataFile = "IMUCalibration.json";
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
//        this.gyro.initialize(parameters);
        gyro.initialize(new IMU.Parameters(orientationOnRobot));

    }

//    public void update(){
//        double currentHeading = (gyro.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
//        deltaHeading = currentHeading - lastHeading;
//        if (deltaHeading < -180) {
//            deltaHeading += 360;
//        } else if (deltaHeading > 180) {
//            deltaHeading -= 360;
//        }
//        heading += deltaHeading;
//        lastHeading = currentHeading;

//        pitch = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).secondAngle;
//        pitchProfiler.update();


//    public void setHeading(double heading){  startHeading = this.heading-heading; }

    public void reset(){ gyro.resetYaw(); }

    public double getHeading(){
        YawPitchRollAngles orientation = gyro.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
    }
//    public double getPitch(){ return pitch - startPitch; }
//    public double getPitchDerivative(){ return pitchProfiler.getDerivative(); }

//    public double getDeltaHeading(){ return Math.abs(deltaHeading) < 30 ? deltaHeading : 0.0; }
}
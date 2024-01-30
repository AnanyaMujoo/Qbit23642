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
    private double heading = 0;
    private double lastHeading = 0;
    private double startHeading = 0;

    public IGyro(IMU gyro){

        this.gyro = gyro;
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        gyro.initialize(new IMU.Parameters(orientationOnRobot));

    }

    public void setHeading(double heading){ reset(); startHeading = heading; }

    public void reset(){ gyro.resetYaw(); heading = 0; startHeading = 0; }

    public double getHeading(){
        YawPitchRollAngles orientation = gyro.getRobotYawPitchRollAngles();

        double currentHeading = orientation.getYaw(AngleUnit.DEGREES);
        double deltaHeading = currentHeading - lastHeading;
        if (deltaHeading < -180) {
            deltaHeading += 360;
        } else if (deltaHeading > 180) {
            deltaHeading -= 360;
        }
        heading += deltaHeading;
        lastHeading = currentHeading;

        return heading + startHeading;
    }

}
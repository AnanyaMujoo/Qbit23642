package robotparts.electronics.input;

import static global.General.hardwareMap;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import robotparts.Electronic;

/**
 * NOTE: Uncommented
 */

public class IGyro extends Electronic {
    private final BNO055IMU gyro;
    public IGyro(BNO055IMU gyro){
        this.gyro = gyro;
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        //parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        this.gyro.initialize(parameters);
    }
    public double getHeading(){
        return (gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
    }
    public double getAngularVelocity(){
        return gyro.getAngularVelocity().toAngleUnit(AngleUnit.DEGREES).zRotationRate;
    }


}

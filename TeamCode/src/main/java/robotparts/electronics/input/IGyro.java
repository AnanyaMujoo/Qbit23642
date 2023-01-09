package robotparts.electronics.input;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import autoutil.Profiler;
import robotparts.Electronic;

/**
 * NOTE: Uncommented
 */

public class IGyro extends Electronic {

    private final BNO055IMU gyro;
    private double heading, lastHeading, deltaHeading, startHeading = 0;
    private double pitch, startPitch = 0;
    private Profiler pitchProfiler = new Profiler(() -> pitch);

    public IGyro(BNO055IMU gyro){
        this.gyro = gyro;
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        // parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        this.gyro.initialize(parameters);
    }

    public void update(){
        double currentHeading = (gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
        deltaHeading = currentHeading - lastHeading;
        if (deltaHeading < -180)
            deltaHeading += 360;
        else if (deltaHeading > 180)
            deltaHeading -= 360;
        heading += deltaHeading;
        lastHeading = currentHeading;

//        pitch = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).secondAngle;
//        pitchProfiler.update();
    }

    public void setHeading(double heading){ update(); startHeading = this.heading-heading; }

    public void reset(){ update(); startHeading = heading; startPitch = pitch; }

    public double getHeading(){ return heading - startHeading; }
    public double getPitch(){ return pitch - startPitch; }
    public double getPitchDerivative(){ return pitchProfiler.getDerivative(); }

    public double getDeltaHeading(){ return Math.abs(deltaHeading) < 30 ? deltaHeading : 0.0; }
}

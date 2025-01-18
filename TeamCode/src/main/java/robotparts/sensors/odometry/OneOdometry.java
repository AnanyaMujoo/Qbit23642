package robotparts.sensors.odometry;

import static global.General.hardwareMap;
import static robot.RobotFramework.odometryThread;

import com.qualcomm.robotcore.hardware.DcMotor;

import global.Constants;
import robotparts.RobotPart;
import util.codeseg.ExceptionCodeSeg;

public class OneOdometry extends RobotPart {

    public final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::update;
    public DcMotor odo;
    public final double wheelDiameter = 3.5; // cm
    public double odoDistance = 0;
    public double startOdoDistance = 0;
    @Override
    public void init() {
        // right odometry
        odo = hardwareMap.get(DcMotor.class, "br");
        reset();
        odometryThread.setExecutionCode(odometryUpdateCode);
    }

    public void update() {
        odoDistance = getRightCurrentOdoDistance() - startOdoDistance;
    }

    private double getRightCurrentOdoDistance(){
        return odo.getCurrentPosition() * wheelDiameter * Math.PI / Constants.ODOMETRY_ENCODER_TICKS_PER_REV;
    }


    public double getRightOdometryDistance() {
        return odoDistance;
    }

    @Override
    public void reset(){
        startOdoDistance = getRightCurrentOdoDistance();
    }

}

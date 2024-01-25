package robotparts.sensors.odometry;

import static global.General.hardwareMap;
import static robot.RobotFramework.odometryThread;

import com.qualcomm.robotcore.hardware.DcMotor;

import global.Constants;
import robotparts.RobotPart;
import util.codeseg.ExceptionCodeSeg;

public class OneOdometry extends RobotPart {

    public final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::update1;
    public final ExceptionCodeSeg<RuntimeException> odometryUpdate2Code = this::update2;
    public DcMotor odo1;
    public DcMotor odo2;
    public final double wheelDiameter = 3.5; // cm
    public double odo1Distance = 0;
    public double odo2Distance = 0;
    public double startOdo1Distance = 0;
    public double startOdo2Distance =0;
    @Override
    public void init() {
        // right odometry
        odo1 = hardwareMap.get(DcMotor.class, "br");
        odo2 = hardwareMap.get(DcMotor.class, "bl");
        reset();
        //odometryThread.setExecutionCode(odometryUpdateCode);
        odometryThread.setExecutionCode(odometryUpdate2Code);
    }

    public void update1() {
        odo1Distance = getCurrentOdo1Distance() - startOdo1Distance;
    }
    public void update2(){
        odo2Distance = getCurrentOdo2Distance()- startOdo2Distance;
    }

    private double getCurrentOdo1Distance(){
        return odo1.getCurrentPosition() * wheelDiameter * Math.PI / Constants.ODOMETRY_ENCODER_TICKS_PER_REV;
    }

    private double getCurrentOdo2Distance(){
        return odo2.getCurrentPosition() * wheelDiameter * Math.PI / Constants.ODOMETRY_ENCODER_TICKS_PER_REV;
    }

    public double getOdometry1Distance() {
        return odo1Distance;
    }
    public double getOdometry2Distance(){
        return odo2Distance;
    }

    @Override
    public void reset(){
        startOdo1Distance = getCurrentOdo1Distance();
        startOdo2Distance = getCurrentOdo2Distance();
    }

}

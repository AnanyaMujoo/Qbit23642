package robotparts.sensors.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import geometry.position.Pose;
import geometry.position.Vector;
import global.Constants;
import robotparts.RobotPart;
import util.codeseg.ExceptionCodeSeg;
import util.template.Precision;

import static global.General.hardwareMap;
import static robot.RobotFramework.odometryThread;

public class NewOdometry extends RobotPart {

    public double x, y, h, xo, yo, startX, startY, lastX, lastY;
    public final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::update;
    public DcMotor yOdo;
    public DcMotor xOdo;
    public final Vector odometryCenterToRobotCenter = new Vector(-15.5, 17.0);
    public final double wheelDiameter = 3.5; // cm

    @Override
    public void init() {
        xOdo = hardwareMap.get(DcMotor.class, "bl");
        yOdo = hardwareMap.get(DcMotor.class, "br");
        reset();
        odometryThread.setExecutionCode(odometryUpdateCode);
    }

    public void update(){
        double currentX = getEncX();
        double currentY = getEncY();

        double deltaX = currentX - lastX;
        double deltaY = currentY - lastY;

        lastX = currentX;
        lastY = currentY;


        h = gyro.getHeading();



        Vector localDelta = new Vector(deltaX, deltaY);


        Vector globalDelta = localDelta.getRotated(getHeading());

        xo += globalDelta.getX();
        yo += globalDelta.getY();


        Vector globalOdometryCenterToRobotCenter = odometryCenterToRobotCenter.getRotated(getHeading()).getSubtracted(odometryCenterToRobotCenter);

        x = xo + globalOdometryCenterToRobotCenter.getX();
        y = yo + globalOdometryCenterToRobotCenter.getY();


    }


    public double getEncX() { return -(xOdo.getCurrentPosition()-startX) * wheelDiameter * Math.PI / Constants.ODOMETRY_ENCODER_TICKS_PER_REV; }
    public double getEncY() { return (yOdo.getCurrentPosition()-startY) * wheelDiameter * Math.PI / Constants.ODOMETRY_ENCODER_TICKS_PER_REV; }

    public final double getX(){ return x; }
    public final double getY(){ return y; }
    public double getHeading() { return h; }
    public Pose getPose() { return new Pose(x, y, h); }


    public void reset(){
        reset(new Pose(0,0,0));
    }


    public void reset(Pose pose){
        gyro.reset();
        x = pose.getX(); y = pose.getY(); h = pose.getAngle(); xo = pose.getX(); yo = pose.getY();
        startX = xOdo.getCurrentPosition();
        startY = yOdo.getCurrentPosition();
        lastX = 0;
        lastY = 0;
        gyro.setHeading(pose.getAngle());
    }
}

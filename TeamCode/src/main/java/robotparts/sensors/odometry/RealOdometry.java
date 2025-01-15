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

public class RealOdometry extends RobotPart {

    public double x, y, h, xo, yo, startX, startY, lastX, lastY, lastY2;
    public final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::update;
    public DcMotor yOdo;
    public DcMotor xOdo;
    // Where odometry center is the intersection of the parallels to the odometry
    public final Vector globalOdometryCenterToRobotCenter = new Vector(14, -12.9);
    public final double wheelDiameter = 3.5; // cm
    public final Precision precision = new Precision();

    @Override
    public void init() {
        xOdo = hardwareMap.get(DcMotor.class, "fl");
        yOdo = hardwareMap.get(DcMotor.class, "bl");
        reset();
        odometryThread.setExecutionCode(odometryUpdateCode);
    }

    public void update(){
        double currentX = getEncX();
        double currentY = getEncY();
//        double currentY2 = getEncY2();
        double deltaX = currentX - lastX;
        double deltaY = currentY - lastY;
//        double deltaY2 = currentY2 - lastY2;
        lastX = currentX;
        lastY = currentY;
//        lastY2 = currentY2;

//        0.012*deltaX
//        double deltaHeading = (deltaY2-deltaY)*2.7;


//        h += deltaHeading;

//        gyro.update();

        h = gyro.getHeading();

//        precision.throttle(() -> h = gyro.getHeading(), 100);




//        gyro.update();
//        setHeading(gyro.getHeading());

//        deltaX += 0.001*deltaY;


        Vector localDelta = new Vector(deltaX, deltaY).getRotated(2.5);


//        Vector localXFixed = localDelta.getXVector().getRotated(0.1);
//        Vector localDeltaFixed = new Vector(localXFixed.getX(), localDelta.getY());
//        localDelta = localDeltaFixed.getCopy();
//        localDelta = localDeltaFixed.getRotated(-0.21);


        Vector globalDelta = localDelta.getRotated(getHeading());

//        xo += localDelta.getX();
//        yo += localDelta.getY();

        xo += globalDelta.getX();
        yo += globalDelta.getY();


//        x = xo;
//        y = yo;


        Vector globalOdometryCenterToRobotCenter = this.globalOdometryCenterToRobotCenter.getRotated(getHeading()).getSubtracted(this.globalOdometryCenterToRobotCenter);

        x = xo + globalOdometryCenterToRobotCenter.getX();
        y = yo + globalOdometryCenterToRobotCenter.getY();


    }


    public double getEncX() { return (-xOdo.getCurrentPosition()-startX) * wheelDiameter * Math.PI / Constants.ODOMETRY_ENCODER_TICKS_PER_REV; }
    public double getEncY() { return (-yOdo.getCurrentPosition()-startY) * wheelDiameter * Math.PI / Constants.ODOMETRY_ENCODER_TICKS_PER_REV; }

    public final double getX(){ return x; }
    public final double getY(){ return y; }
    public double getHeading() { return h; }
    public Pose getPose() { return new Pose(x, y, h); }


    public void reset(){
        reset(new Pose());
    }


    public void reset(Pose pose){
        gyro.reset();
        precision.reset();
        x = pose.getX(); y = pose.getY(); h = pose.getAngle(); xo = pose.getX(); yo = pose.getY();
        gyro.reset();
        startX = -xOdo.getCurrentPosition();
        startY = -yOdo.getCurrentPosition();
        lastX = 0;
        lastY = 0;
        lastY2 = 0;
        gyro.setHeading(pose.getAngle());
    }
}
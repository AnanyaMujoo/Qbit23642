package robotparts.sensors.odometry;

import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import math.linearalgebra.Matrix2D;
import math.linearalgebra.Matrix3D;
import math.linearalgebra.Vector3D;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;
import util.template.Precision;

import static java.lang.Math.*;

public class ThreeOdometry extends TwoOdometry {
//    private IEncoder enc3; // right
    public final double width = 21.55;
    public int mode = 0;
    public final Point odometryCenter = new Point();
    private final Vector leftOdometryCenterToRobotCenter = new Vector(10.5, 13.0);
    private Point offset = new Point();
    private Vector adjust = new Vector();
    private double headingAdjust = 0;
//    private final Precision precision = new Precision();


    @Override
    public void setCurrentPose(Pose pose) {
        synchronized (odometryCenter) {
            offset = new Point();
            Vector globalOdometryCenterToRobotCenter = leftOdometryCenterToRobotCenter.getRotated(pose.getAngle()).getSubtracted(leftOdometryCenterToRobotCenter);
            odometryCenter.set(pose.getPoint().getSubtracted(globalOdometryCenterToRobotCenter.getPoint()));
            super.setCurrentPose(pose);
        }
    }


//    @Override
//    protected void createEncoders() {
//        super.createEncoders();
////        enc3 = create("frEnc", ElectronicType.IENCODER_NORMAL);
////        precision.reset();
////        addEncoders(enc3);
////        enc3.invert();
//    }

    @Override
    protected void update() {

        double dyl = 0, dyr = 0, dx = 0, dh = 0;

        dyl = enc1.getDeltaPosition();

        dx = enc2.getDeltaPosition();

//        dx = enc2.getDeltaPosition() + (xCorrectionScale*dyl);

//        dyr = enc3.getDeltaPosition() + (yCorrectionScale*dyl);

//        double dhu = toDegrees((dyr-dyl)/width);
//        dh = dhu*1.011;
//        if(dhu > 0){ dh = 1.0025*dhu; }else{ dh = 1.0013*dhu; }

//        dx *= 1.01;

//
//
//        updateCurrentPose(new Vector(dyr, dyl));



//        if(mode == 0) {
//            dx = enc2.getDeltaPosition();
//            dyl = (enc1.getDeltaPosition() - dx*sin(angleLeft));
//        }else if(mode == 1){
//            dx = enc2.getDeltaPosition();
//        }else if(mode == 2){
//            dx = enc2.getDeltaPosition();
//            dyl = (enc3.getDeltaPosition() - dx*sin(angleRight));
//        }
//
//
//        dx = enc2.getDeltaPosition();
//        dyl = (enc1.getDeltaPosition() - dx*sin(angleLeft));
//        dyr = (enc3.getDeltaPosition() - dx*sin(angleRight));
//        dh = toDegrees(((scaleRight*dyr)-dyl)/width);
//

//        localDelta = localDelta.getRotated(0.3);
//        localDelta.scaleY(1.01);
//        localDelta.scaleX(1.01);
//        setHeading(gyro.getHeading());


        Vector localDelta = new Vector(dx, dyl);

//        updateCurrentHeading(dh);

        setHeading(gyro.getHeading());
//        precision.throttle(() -> setHeading(gyro.getHeading()), 100);

        odometryCenter.translate(toGlobalFrame(localDelta));
        Vector globalOdometryCenterToRobotCenter = toGlobalFrame(leftOdometryCenterToRobotCenter).getSubtracted(leftOdometryCenterToRobotCenter);
        setCurrentPose(odometryCenter.getAdded(globalOdometryCenterToRobotCenter.getPoint()));
    }

    @Override
    protected void resetObjects() { odometryCenter.set(new Point()); offset = new Point(); adjust = new Vector(); headingAdjust = 0; }


    @Override
    public double getHeading() {
        return super.getHeading() + headingAdjust;
    }

    public void setHeadingUsingAdjust(double heading) {
        this.headingAdjust = heading - super.getHeading();
    }

    @Override
    public Pose getPose() {
        return super.getPose().getAdded(new Pose(offset, 0)).getSubtracted(new Pose(adjust.getPoint(), 0));
    }

    public void setPointUsingOffset(Point pose){
        setOffset(pose.getSubtracted(super.getPose().getPoint()));
    }

    public void adjust(Vector delta){ this.adjust = this.adjust.getAdded(delta); }
    public void adjustUp(double delta){ adjust(new Vector(0,abs(delta)));}
    public void adjustDown(double delta){ adjust(new Vector(0,-abs(delta)));}
    public void adjustRight(double delta){ adjust(new Vector(abs(delta),0));}
    public void adjustLeft(double delta){ adjust(new Vector(-abs(delta), 0));}
    public Vector getAdjust(){ return adjust; }




    public void setOffset(Point offset){ this.offset = offset.getCopy(); }
}

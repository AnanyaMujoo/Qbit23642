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
    private IEncoder enc3;
    public final double width = 22.8507;
    public double angle1 = -1.2;
    public double angle2 = toRadians(0.5329);
    public double angle3 = toRadians(5.7319);
    public double angle4 = toRadians(2);
    public final double scale1 = 0.9925;
    public final double scale2 = 1.0115*scale1;
    public final double scale3 = 1.0055*scale1;
    public int mode = 0;
    public final Point odometryCenter = new Point();
    private final Vector odometryCenterToRobotCenter = new Vector(11.5, 13.0);

    @Override
    protected void createEncoders() {
        super.createEncoders();
        enc3 = create("brEnc", ElectronicType.IENCODER_NORMAL);
        addEncoders(enc3);
        enc3.invert();
    }

    @Override
    protected void update() {

        double dy, dx, dh = 0;
//
//        if(mode == 0) {
//            dy = enc1.getDeltaPosition()*scale1;
//        }else if(mode == 1){
//            dy = enc2.getDeltaPosition()*scale2;
//        }else if(mode == 2){
//            dy = enc3.getDeltaPosition()*scale3;
//        }else if(mode == 3) {
//            dy = enc1.getDeltaPosition();
//            dx = ((scale2 * enc2.getDeltaPosition()) - sin(angle2)*dy)/cos(angle2);
//            dh = ((scale3 * enc3.getDeltaPosition()) + (dx * sin(angle3)) - (dy * cos(angle3))) / (cos(angle3) * width);
//        }


        dx = scale2*enc2.getDeltaPosition();
        dy = (enc1.getDeltaPosition() - dx*sin(angle4));
//        dx = ((scale2 * enc2.getDeltaPosition()) - sin(angle2)*dy)/cos(angle2);
//        dh = ((scale3 * enc3.getDeltaPosition()) + (dx * sin(angle3)) - (dy * cos(angle3))) / (cos(angle3) * width);
//        dh = Math.toDegrees(dh);
//        localDelta.rotate(angle1);

        Vector localDelta = new Vector(dx, dy);
        setHeading(gyro.getHeading());
        odometryCenter.translate(toGlobalFrame(localDelta));
        Vector globalOdometryCenterToRobotCenter = toGlobalFrame(odometryCenterToRobotCenter).getSubtracted(odometryCenterToRobotCenter);
        setCurrentPose(odometryCenter.getAdded(globalOdometryCenterToRobotCenter.getPoint()));

//        updateCurrentPose(toGlobalFrame(localDelta));

//        if(mode == 0){
//            updateCurrentPose(localDelta);
//        }else{
//
////            localDelta = new Vector(localDelta.getTheta()+getHeading()).getScaled(localDelta.getLength());
////            updateCurrentPose(localDelta);
//        }

//        odometryCenter.translate(localDelta);
//        odometryCenter.translate(toGlobalFrame(localDelta));
//        setCurrentPose(new Pose(odometryCenter.ge));
//        Vector globalOdometryCenterToRobotCenter = toGlobalFrame(odometryCenterToRobotCenter).getSubtracted(odometryCenterToRobotCenter);
//        setCurrentPose(new Pose(odometryCenter.getAdded(globalOdometryCenterToRobotCenter.getPoint()), getHeading() + Math.toDegrees(dh)));
    }

    @Override
    protected void resetObjects() { odometryCenter.set(new Point()); }
}

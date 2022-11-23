package robotparts.sensors.odometry;

import geometry.framework.Point;
import geometry.position.Pose;
import math.linearalgebra.Matrix3D;
import math.linearalgebra.Vector3D;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;
import util.template.Precision;

public class ThreeOdometry extends TwoOdometry {
    private IEncoder enc3;
    private Pose enc3Pose;
    private Matrix3D dYdXdThetaMatrixInverted;
//    private Precision precision;

    @Override
    protected void createEncoders() {
        super.createEncoders();
        enc3 = create("brEnc", ElectronicType.IENCODER_NORMAL);
        addEncoders(enc3);
        enc3.invert();
//        precision = new Precision();
    }

    @Override
    protected void setEncoderPoses() {
        double startX = -10.8; double width = 21.34; double startY = -13.6; double height = 18.6;
        enc1Pose = new Pose(new Point(startX,startY+height), 90);
        enc2Pose = new Pose(new Point(-0.5,startY), 0.0);
        enc3Pose = new Pose(new Point(startX+width,startY+height), 90);
    }

    @Override
    protected void update() {
        Vector3D localEncDelta = new Vector3D(enc1.getDeltaPosition(), enc2.getDeltaPosition(), enc3.getDeltaPosition());
        Vector3D localDelta = dYdXdThetaMatrixInverted.multiply(localEncDelta);
        updateCurrentPose(toGlobalFrame(localDelta.get2D()), Math.toDegrees(localDelta.getZ()));
//        precision.throttle(() -> setHeading(gyro.getHeading()), 300);
    }

    @Override
    protected void setConstantObjects() {
        dYdXdThetaMatrixInverted = new Matrix3D(
                enc1Pose.getAngleUnitVector().getX(), enc1Pose.getAngleUnitVector().getY(), enc1Pose.getVector().getCrossProduct(enc1Pose.getAngleUnitVector()),
                enc2Pose.getAngleUnitVector().getX(), enc2Pose.getAngleUnitVector().getY(), enc2Pose.getVector().getCrossProduct(enc2Pose.getAngleUnitVector()),
                enc3Pose.getAngleUnitVector().getX(), enc3Pose.getAngleUnitVector().getY(), enc3Pose.getVector().getCrossProduct(enc3Pose.getAngleUnitVector())
        ).getInverted();
    }
}

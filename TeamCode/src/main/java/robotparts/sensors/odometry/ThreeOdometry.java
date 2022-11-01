package robotparts.sensors.odometry;

import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import math.linearalgebra.Matrix2D;
import math.linearalgebra.Matrix3D;
import math.linearalgebra.Vector3D;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;

public class ThreeOdometry extends TwoOdometry{
    private IEncoder enc3;
    private Pose enc3Pose;
    private Matrix3D dYdXdThetaMatrixInverted;

    @Override
    protected void createEncoders() {
        super.createEncoders();
        enc3 = create("enc3", ElectronicType.IENCODER_NORMAL);
        addEncoders(enc3);
//        useGyro();
    }

    @Override
    protected void setEncoderPoses() {
        enc1Pose = new Pose(new Point(0,0), 90);
        enc2Pose = new Pose(new Point(0,-12.6), 0);
        enc3Pose = new Pose(new Point(0,0), 90);
    }

    @Override
    protected void update() {
        Vector3D localEncDelta = new Vector3D(enc1.getDeltaPosition(), enc2.getDeltaPosition(), enc3.getDeltaPosition());
        Vector3D localDelta = dYdXdThetaMatrixInverted.multiply(localEncDelta);
        updateCurrentPose(toGlobalFrame(localDelta.get2D()), Math.toDegrees(localDelta.getZ()));
    }

    @Override
    protected void setConstantObjects() {
        dYdXdThetaMatrixInverted = new Matrix3D(
                enc1Pose.getAngleUnitVector().getX(), enc1Pose.getAngleUnitVector().getY(), enc1Pose.getVector().getCrossProduct(enc1Pose.getAngleUnitVector()),
                enc2Pose.getAngleUnitVector().getX(), enc2Pose.getAngleUnitVector().getY(), enc2Pose.getVector().getCrossProduct(enc2Pose.getAngleUnitVector()),
                enc3Pose.getAngleUnitVector().getY(), enc3Pose.getAngleUnitVector().getY(), enc3Pose.getVector().getCrossProduct(enc3Pose.getAngleUnitVector())
        ).getInverted();
    }

}

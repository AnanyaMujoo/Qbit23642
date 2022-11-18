package robotparts.sensors.odometry;

import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import math.linearalgebra.Matrix3D;
import math.linearalgebra.Vector3D;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;
import util.template.Precision;

import static global.General.log;

public class ThreeOdometry extends TwoOdometryV2 {
    private IEncoder enc3;
    private Pose enc3Pose;
    private Matrix3D dYdXdThetaMatrixInverted;
    private Precision precision;

    @Override
    protected void createEncoders() {
        super.createEncoders();
        enc3 = create("brEnc", ElectronicType.IENCODER_NORMAL);
        addEncoders(enc3);
        enc1.invert();
        enc3.invert();
        precision = new Precision();
    }

    @Override
    protected void setEncoderPoses() {
        double start = -11.0; double width = 21.5;
        enc1Pose = new Pose(new Point(start,4), 89);
        enc2Pose = new Pose(new Point(1.5,-14), 0);
        enc3Pose = new Pose(new Point(start+width,4), 90);
    }

    @Override
    protected void update() {
        Vector3D localEncDelta = new Vector3D(enc1.getDeltaPosition(), enc2.getDeltaPosition(), 1.01*enc3.getDeltaPosition());
        Vector3D localDelta = dYdXdThetaMatrixInverted.multiply(localEncDelta);
//        Vector localOffset = new Vector(-Math.abs(Math.toRadians(gyro.getDeltaHeading()))*1.33, 0);
        Vector localOffset = new Vector(0,0);
        Vector globalDelta = toGlobalFrame(localDelta.get2D().getAdded(localOffset));
        updateCurrentPose(globalDelta, Math.toDegrees(localDelta.getZ()));
//        precision.throttle(() -> setHeading(gyro.getHeading()), 100);
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

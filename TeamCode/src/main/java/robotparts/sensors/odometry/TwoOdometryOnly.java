package robotparts.sensors.odometry;

import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import math.linearalgebra.Matrix2D;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;

public class TwoOdometryOnly extends Odometry {
    protected IEncoder enc1, enc2;
    protected Pose enc1Pose, enc2Pose;
    protected Point odometryCenter = new Point(); // Point where perpendiculars of parallels connect
    private Matrix2D dYdXMatrixInverted;
    private final Vector odometryCenterToRobotCenter = new Vector(0.0, 12.6); // TODO CHECK

    @Override
    protected void createEncoders() {
        enc1 = create("flEnc", ElectronicType.IENCODER_NORMAL);
        enc2 = create("blEnc", ElectronicType.IENCODER_NORMAL);
        enc1.invert();
        addEncoders(enc1, enc2);
    }

    @Override
    protected void setEncoderPoses() {
        enc1Pose = new Pose(new Point(0.0,3.0), 90);
        enc2Pose = new Pose(new Point(0.0,-13.5), 0);
    }

    @Override
    protected void setConstantObjects() {
        dYdXMatrixInverted = new Matrix2D(
                enc1Pose.getAngleUnitVector().getX(), enc1Pose.getAngleUnitVector().getY(),
                enc2Pose.getAngleUnitVector().getX(), enc2Pose.getAngleUnitVector().getY()
        ).getInverted();
    }

    @Override
    protected void update() {
        Vector localDelta = dYdXMatrixInverted.multiply(new Vector(enc1.getDeltaPosition(), enc2.getDeltaPosition()));
        odometryCenter.translate(toGlobalFrame(localDelta));
        Vector globalOdometryCenterToRobotCenter = toLocalFrame(odometryCenterToRobotCenter).getSubtracted(odometryCenterToRobotCenter); // offset to match start position
        setCurrentPose(new Pose(odometryCenter.getAdded(globalOdometryCenterToRobotCenter.getPoint()), gyro.getHeading()));
    }
}

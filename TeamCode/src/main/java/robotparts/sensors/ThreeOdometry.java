package robotparts.sensors;

import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import math.linearalgebra.Matrix3D;
import math.linearalgebra.Vector3D;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;

public class ThreeOdometry extends TwoOdometry{
    private IEncoder enc3;
    private Pose enc3Pose;
    private double enc3X, enc3Y = 0;

    @Override
    protected void createEncoders() {
        super.createEncoders();
        enc3 = create("enc3", ElectronicType.IENCODER_NORMAL);
    }

    @Override
    protected void setEncoderPoses() {
        enc1Pose = new Pose(new Point(0,0), 90);
        enc2Pose = new Pose(new Point(0,-12.6), 0);
        enc3Pose = new Pose(new Point(0,0), 90);
    }

    @Override
    protected void resetHardware() {
        enc1.reset(); enc2.reset(); enc3.reset();
    }

    @Override
    protected void update() {
        updateCurrentPose(enc1, enc2, enc3, null);
    }

    @Override
    protected void setConstantMatrixValues() {
        super.setConstantMatrixValues();
        enc3X = enc3Pose.getUnitVector().getDotProduct(Vector.xHat());
        enc3Y = enc3Pose.getUnitVector().getDotProduct(Vector.yHat());
    }

    @Override
    protected Pose updateDeltaPose(Vector3D deltaEnc, Vector headingVector, double deltaHeading) {
        Matrix3D dXdYdThetaMatrix = new Matrix3D(
                enc1X, enc1Y, enc1Pose.getVector().getCrossProduct(headingVector),
                enc2X, enc2Y, enc2Pose.getVector().getCrossProduct(headingVector),
                enc3X, enc3Y, enc3Pose.getVector().getCrossProduct(headingVector)
        );
        Vector3D deltaPose = Matrix3D.solve(dXdYdThetaMatrix, deltaEnc);
        return new Pose(deltaPose.get2D(), deltaHeading == 0 ? deltaPose.getZ() : (deltaPose.getZ() + deltaHeading)/2.0);
    }
}

package robotparts.sensors.odometry;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import math.linearalgebra.Matrix2D;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;

/**
 * Unadvised to use this
 */
@Disabled
public class TwoOdometryV2 extends Odometry {
    protected IEncoder enc1, enc2;
    protected Pose enc1Pose, enc2Pose;
    private Vector dThetaVector;
    private Matrix2D dYdXMatrixInverted;

    @Override
    protected void createEncoders(){
        enc1 = create("flEnc", ElectronicType.IENCODER_NORMAL);
        enc2 = create("blEnc", ElectronicType.IENCODER_NORMAL);
        enc1.invert();
        addEncoders(enc1, enc2); useGyro();
    }

    @Override
    protected void setEncoderPoses(){
        enc1Pose = new Pose(new Point(0.0,3.0), 90);
        enc2Pose = new Pose(new Point(0.0,-13.5), 0);
    }

    @Override
    protected void setConstantObjects(){
        dYdXMatrixInverted = new Matrix2D(
                enc1Pose.getAngleUnitVector().getX(), enc1Pose.getAngleUnitVector().getY(),
                enc2Pose.getAngleUnitVector().getX(), enc2Pose.getAngleUnitVector().getY()
        ).getInverted();
        dThetaVector = new Vector(
                enc1Pose.getVector().getCrossProduct(enc1Pose.getAngleUnitVector()),
                enc2Pose.getVector().getCrossProduct(enc2Pose.getAngleUnitVector())
        );
    }

    @Override
    protected void update(){
        double deltaHeading = Math.toRadians(gyro.getDeltaHeading());
        Vector localEncDelta = new Vector(enc1.getDeltaPosition(), enc2.getDeltaPosition()).getSubtracted(dThetaVector.getScaled(deltaHeading));
        Vector localDelta = dYdXMatrixInverted.multiply(localEncDelta);
        if(deltaHeading != 0.0){
            localDelta = Matrix2D.getIntegratedFromZeroRotationMatrix(deltaHeading).getMultiplied(1.0 / deltaHeading).multiply(localDelta);
        }
        Vector globalDelta = toGlobalFrame(localDelta);
        updateCurrentPose(globalDelta, Math.toDegrees(deltaHeading));
    }
}

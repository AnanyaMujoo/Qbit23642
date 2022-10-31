package robotparts.sensors.odometry;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Constants;
import math.linearalgebra.Matrix2D;
import math.linearalgebra.Vector3D;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;
import robotparts.sensors.GyroSensors;
import util.ExceptionCatcher;
import util.codeseg.ExceptionCodeSeg;

import static global.General.bot;
import static global.General.log;
import static robot.RobotFramework.odometryThread;

public class TwoOdometry extends Odometry {
    protected IEncoder enc1, enc2;
    protected Pose enc1Pose, enc2Pose;
    protected double enc1X, enc2X, enc1Y, enc2Y;
    private Vector dThetaVector;
    private Matrix2D dYdXMatrixInverted;

    @Override
    protected void createEncoders(){
        enc1 = create("flEnc", ElectronicType.IENCODER_NORMAL);
        enc2 = create("blEnc", ElectronicType.IENCODER_NORMAL);
        enc1.invert();
        addEncoders(enc1, enc2);
    }

    @Override
    protected void setEncoderPoses(){
        enc1Pose = new Pose(new Point(0.0,3.0), 90);
        enc2Pose = new Pose(new Point(0.0,-13.5), 0);
    }

    @Override
    protected void setConstantObjects(){
        enc1X = Vector.xHat().getDotProduct(enc1Pose.getAngleUnitVector());
        enc1Y = Vector.yHat().getDotProduct(enc1Pose.getAngleUnitVector());
        enc2X = Vector.xHat().getDotProduct(enc2Pose.getAngleUnitVector());
        enc2Y = Vector.yHat().getDotProduct(enc2Pose.getAngleUnitVector());
        dYdXMatrixInverted = new Matrix2D(enc1X, enc1Y, enc2X, enc2Y).getInverted();
        dThetaVector = new Vector(enc1Pose.getVector().getCrossProduct(enc1Pose.getAngleUnitVector()), enc2Pose.getVector().getCrossProduct(enc2Pose.getAngleUnitVector()));
    }

    @Override
    protected void update(){
        updateCurrentPose(enc1, enc2, null, gyro);
    }


    protected Pose updateDeltaPose(Vector3D deltaEnc, double deltaHeading){
        Vector output = deltaEnc.get2D().getSubtracted(dThetaVector.getScaled(Math.toRadians(deltaHeading)));
        log.show(deltaEnc.get2D());
        log.show(dThetaVector.getScaled(Math.toRadians(deltaHeading)));
        Vector deltaPos = dYdXMatrixInverted.multiply(output);
        return new Pose(deltaPos, deltaHeading);
    }

    protected final void updateCurrentPose(@NonNull IEncoder enc1, @NonNull IEncoder enc2, @Nullable IEncoder enc3, @Nullable GyroSensors gyro){
        enc1.updateNormal(); enc2.updateNormal();
        if(enc3 != null){ enc3.updateNormal(); }
        if(gyro != null){ gyro.updateHeading(); }
        double deltaHeading = gyro != null ? gyro.getDeltaHeading() : 0.0;
        Pose deltaPose = updateDeltaPose(
                new Vector3D(enc1.getDeltaPosition(), enc2.getDeltaPosition(), enc3 != null ? enc3.getDeltaPosition() : 0.0), deltaHeading);
        if(deltaHeading != 0.0){
            deltaPose.setVector(Matrix2D.getIntegratedFromZeroRotationMatrix(Math.toRadians(deltaHeading))
                    .getMultiplied(1.0 / Math.toRadians(deltaHeading)).multiply(deltaPose.getVector()));
        }
        synchronized (currentPose){ currentPose.add(deltaPose.getOnlyPointRotated(getHeading())); }
    }

}

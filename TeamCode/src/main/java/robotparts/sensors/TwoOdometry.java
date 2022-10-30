package robotparts.sensors;

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
import util.ExceptionCatcher;
import util.codeseg.ExceptionCodeSeg;

import static global.General.bot;
import static global.General.log;
import static robot.RobotFramework.odometryThread;

public class TwoOdometry extends RobotPart {
    protected IEncoder enc1, enc2;
    private final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::update;
    private final Pose currentPose = new Pose();
    private static final double encoderWheelDiameter = 3.5; // 3.5 cm
    protected Pose enc1Pose;
    protected Pose enc2Pose;
    protected double enc1X, enc2X, enc1Y, enc2Y;
    private Vector dThetaVector;
    private Matrix2D dYdXMatrixInverted;

    @Override
    public final void init() {
        createEncoders();
        setEncoderPoses();
        setConstantObjects();
        odometryThread.setExecutionCode(odometryUpdateCode);
    }

    protected void createEncoders(){
        enc1 = create("flEnc", ElectronicType.IENCODER_NORMAL);
        enc2 = create("blEnc", ElectronicType.IENCODER_NORMAL);
        enc1.invert();
    }

    protected void setEncoderPoses(){
        enc1Pose = new Pose(new Point(0.0,3.0), 90);
        enc2Pose = new Pose(new Point(0.0,-13.5), 0); // 13.5 cm
    }

    protected void resetHardware(){
        enc1.reset(); enc2.reset();
    }

    protected void update(){
        updateCurrentPose(enc1, enc2, null, gyro);
    }

    protected void setConstantObjects(){
        enc1X = Vector.xHat().getDotProduct(enc1Pose.getAngleUnitVector());
        enc1Y = Vector.yHat().getDotProduct(enc1Pose.getAngleUnitVector());
        enc2X = Vector.xHat().getDotProduct(enc2Pose.getAngleUnitVector());
        enc2Y = Vector.yHat().getDotProduct(enc2Pose.getAngleUnitVector());
        dYdXMatrixInverted = new Matrix2D(enc1X, enc1Y, enc2X, enc2Y).getInverted();
        dThetaVector = new Vector(enc1Pose.getVector().getCrossProduct(enc1Pose.getAngleUnitVector()), enc2Pose.getVector().getCrossProduct(enc2Pose.getAngleUnitVector()));
    }

    protected Pose updateDeltaPose(Vector3D deltaEnc, double deltaHeading){
        Vector output = deltaEnc.get2D().getSubtracted(dThetaVector.getScaled(Math.toRadians(deltaHeading)));
        Vector deltaPos = dYdXMatrixInverted.multiply(output);
        return new Pose(deltaPos, deltaHeading);
    }

    protected final void updateCurrentPose(@NonNull IEncoder enc1, @NonNull IEncoder enc2, @Nullable IEncoder enc3, @Nullable GyroSensors gyro){
        enc1.updateNormal(); enc2.updateNormal();
        if(enc3 != null){ enc3.updateNormal(); }
        if(gyro != null){ gyro.updateHeading(); }
        double deltaHeading = gyro != null ? -gyro.getDeltaHeading() : 0.0;
        Pose deltaPose = updateDeltaPose(
                new Vector3D(enc1.getDeltaPosition(), enc2.getDeltaPosition(), enc3 != null ? enc3.getDeltaPosition() : 0.0)
                        .getScaled(encoderWheelDiameter*Math.PI/Constants.ENCODER_TICKS_PER_REV), deltaHeading);
        if(deltaHeading != 0.0){
            deltaPose.setVector(Matrix2D.getIntegratedFromZeroRotationMatrix(Math.toRadians(deltaHeading))
                    .getMultiplied(1.0 / Math.toRadians(deltaHeading)).multiply(deltaPose.getVector()));
        }
        synchronized (currentPose){ currentPose.add(deltaPose.getOnlyPointRotated(getHeading())); }
    }

    public final Pose getPose(){ return currentPose; }
    public final double getX(){ return currentPose.getX(); }
    public final double getY(){ return currentPose.getY(); }
    public final double getHeading(){ return currentPose.getAngle(); }

    @Override
    public final void reset(){
        resetHardware();
        ExceptionCatcher.catchInterrupted(() -> Thread.sleep(50));
        currentPose.setZero();
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%f, %f, %f", currentPose.getX(), currentPose.getY(), currentPose.getAngle());
    }
}

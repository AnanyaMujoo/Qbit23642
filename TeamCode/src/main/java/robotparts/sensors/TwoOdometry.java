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
import util.codeseg.ExceptionCodeSeg;

import static global.General.log;
import static robot.RobotFramework.odometryThread;

public class TwoOdometry extends RobotPart {
    protected IEncoder enc1, enc2;
    private final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::update;
    private final Pose currentPose = new Pose();
    private static final double encoderWheelDiameter = 3.5; //cm
    protected Pose enc1Pose;
    protected Pose enc2Pose;
    protected Matrix2D dYdXMatrixInverted;
    protected double enc1X, enc2X, enc1Y, enc2Y;

    @Override
    public final void init() {
        createEncoders();
        setEncoderPoses();
        setConstantMatrixValues();
        odometryThread.setExecutionCode(odometryUpdateCode);
        reset();
    }

    protected void createEncoders(){
        enc1 = create("flEnc", ElectronicType.IENCODER_NORMAL);
        enc2 = create("blEnc", ElectronicType.IENCODER_NORMAL);
    }

    // TODO CHECK ANGLES MAYBE NOT 90?
    protected void setEncoderPoses(){
        enc1Pose = new Pose(new Point(0.01,1), 90);
        enc2Pose = new Pose(new Point(-0.01,-11.6), 0);
    }

    protected void resetHardware(){
        enc1.reset(); enc2.reset(); gyro.reset();
    }

    protected void update(){
        updateCurrentPose(enc1, enc2, null, gyro);
    }

    protected void setConstantMatrixValues(){
        enc1X = enc1Pose.getUnitVector().getDotProduct(Vector.xHat());
        enc2X = enc2Pose.getUnitVector().getDotProduct(Vector.xHat());
        enc1Y = enc1Pose.getUnitVector().getDotProduct(Vector.yHat());
        enc2Y = enc2Pose.getUnitVector().getDotProduct(Vector.yHat());
        dYdXMatrixInverted = new Matrix2D(enc1X, enc1X, enc2X, enc2Y).getInverted();
    }

    protected Pose updateDeltaPose(Vector3D deltaEnc, Vector headingVector, double deltaHeading){
        Vector dThetaVector = new Vector(enc1Pose.getVector().getCrossProduct(headingVector), enc2Pose.getVector().getCrossProduct(headingVector));
        Vector output = deltaEnc.get2D().getSubtracted(dThetaVector.getScaled(deltaHeading));
        log.show("output", output);
//        log.show("mat", dYdXMatrixInverted);
        Vector deltaPos = dYdXMatrixInverted.multiply(output);
//        log.show("delta", deltaPos);
        return new Pose(deltaPos, deltaHeading);
    }

    protected final void updateCurrentPose(@NonNull IEncoder enc1, @NonNull IEncoder enc2, @Nullable IEncoder enc3, @Nullable GyroSensors gyro){
        enc1.updateNormal(); enc2.updateNormal();
        if(enc3 != null){ enc3.updateNormal(); }
        if(gyro != null){ gyro.updateHeading(); }
        Pose deltaPose = updateDeltaPose(
                new Vector3D(enc1.getDeltaPosition(), enc2.getDeltaPosition(), enc3 != null ? enc3.getDeltaPosition() : 0.0)
                        .getScaled(encoderWheelDiameter*Math.PI/Constants.ENCODER_TICKS_PER_REV),
                new Vector(getHeading()+90), Math.toRadians(gyro != null ? gyro.getDeltaHeading() : 0.0));
        synchronized (currentPose){ currentPose.add(deltaPose); }
    }

    public final Pose getPose(){ return currentPose; }
    public final double getX(){ return currentPose.getX(); }
    public final double getY(){ return currentPose.getY(); }
    public final double getHeading(){ return currentPose.getAngle(); }

    public final void reset(){
        synchronized (currentPose) { currentPose.setX(0); currentPose.setY(0); currentPose.setAngle(0); }
        resetHardware();
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%f, %f, %f", currentPose.getX(), currentPose.getY(), currentPose.getAngle());
    }
}

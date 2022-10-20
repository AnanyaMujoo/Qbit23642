package robotparts.sensors;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Constants;
import math.linearalgebra.Matrix2D;
import math.linearalgebra.Vector3D;
import robot.BackgroundTask;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;
import util.codeseg.ExceptionCodeSeg;

import static global.General.bot;
import static robot.RobotFramework.odometryThread;

public class TwoOdometry extends RobotPart {
    protected IEncoder enc1, enc2;
    private final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::update;
    private final Pose currentPose = new Pose();
    private static final double encoderWheelDiameter = 3.5; //cm
    protected Pose enc1Pose;
    protected Pose enc2Pose;

    @Override
    public final void init() {
        createEncoders();
        setEncoderPoses();
        bot.addBackgroundTask(new BackgroundTask(this::updateBackground));
        odometryThread.setExecutionCode(odometryUpdateCode);
        reset();
    }

    protected void createEncoders(){
        enc1 = create("enc1", ElectronicType.IENCODER_NORMAL);
        enc2 = create("enc2", ElectronicType.IENCODER_NORMAL);
    }

    protected void setEncoderPoses(){
        enc1Pose = new Pose(new Point(0,0), 90);
        enc2Pose = new Pose(new Point(0,-12.6), 0);
    }

    protected void updateBackground(){
        enc1.updateNormal(); enc2.updateNormal();  gyro.updateHeading();
    }

    protected void resetHardware(){
        enc1.reset(); enc2.reset(); gyro.reset();
    }

    protected void update(){
        update(enc1, enc2, null, gyro);
    }

    protected Pose updateDeltaPose(Vector3D deltaEnc, Vector headingVector, double deltaHeading){
        Vector dThetaVector = new Vector(enc1Pose.getVector().getCrossProduct(headingVector), enc2Pose.getVector().getCrossProduct(headingVector));
        Vector output = deltaEnc.get2D().getSubtracted(dThetaVector);
        Matrix2D dXdYMatrix = new Matrix2D(
                enc1Pose.getUnitVector().getDotProduct(Vector.xHat()), enc1Pose.getUnitVector().getDotProduct(Vector.yHat()),
                enc2Pose.getUnitVector().getDotProduct(Vector.xHat()), enc2Pose.getUnitVector().getDotProduct(Vector.yHat())
        );
        Vector deltaPos = Matrix2D.solve(dXdYMatrix, output);
        return new Pose(deltaPos, deltaHeading);
    }




    protected final void update(@NonNull IEncoder enc1, @NonNull IEncoder enc2, @Nullable IEncoder enc3, @Nullable GyroSensors gyro){
        ArrayList<Double> deltasEnc1 = enc1.getNewDeltaPositions();
        ArrayList<Double> deltasEnc2 = enc2.getNewDeltaPositions();
        ArrayList<Double> deltasEnc3 = enc3 == null ? new ArrayList<>(Collections.nCopies(deltasEnc1.size(), 0.0)) : enc3.getNewDeltaPositions();
        ArrayList<Double> deltasHeading = gyro == null ? new ArrayList<>(Collections.nCopies(deltasEnc1.size(), 0.0)) : gyro.getNewDeltaHeadings();
        for (int i = 0; i < deltasEnc1.size(); i++) {
            updateCurrentPose(deltasEnc1.get(i), deltasEnc2.get(i), deltasEnc3.get(i), deltasHeading.get(i));
        }
    }

    private void updateCurrentPose(double deltaEnc1, double deltaEnc2, double deltaEnc3, double deltaHeading){
        Pose deltaPose = updateDeltaPose(new Vector3D(deltaEnc1, deltaEnc2, deltaEnc3).getScaled(encoderWheelDiameter*Math.PI/Constants.ENCODER_TICKS_PER_REV), new Vector(gyro.getHeading()), deltaHeading);
        synchronized (currentPose){ currentPose.add(deltaPose); }
    }

    public final Pose getPose(){ return currentPose; }

    public final void reset(){
        synchronized (currentPose) { currentPose.setX(0); currentPose.setY(0); currentPose.setAngle(0); }
        resetHardware();
    }

}

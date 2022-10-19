package robotparts.sensors;

import java.util.ArrayList;

import autoutil.profilers.Profiler;
import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Constants;
import math.linearalgebra.Matrix2D;
import math.linearalgebra.Matrix3D;
import math.linearalgebra.Vector3D;
import robot.BackgroundTask;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;
import util.codeseg.ExceptionCodeSeg;
import util.template.Iterator;

import static global.General.bot;
import static global.General.sync;
import static robot.RobotFramework.odometryThread;

public class TwoOdometry extends RobotPart {
    private IEncoder enc1, enc2;
    private final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::update;
    private final Pose currentPose = new Pose();
    private static final double encoderWheelDiameter = 3.5; //cm
    protected Pose enc1Pose;
    protected Pose enc2Pose;

    @Override
    public void init() {
        enc1 = create("enc1", ElectronicType.IENCODER_NORMAL);
        enc2 = create("enc2", ElectronicType.IENCODER_NORMAL);
        setEncoderPoses();
        bot.addBackgroundTask(new BackgroundTask(this::updateBackground));
        odometryThread.setExecutionCode(odometryUpdateCode);
        reset();
    }

    protected void setEncoderPoses(){
        enc1Pose = new Pose(new Point(3,4), 90);
        enc2Pose = new Pose(new Point(-3,4), 90);
    }

    protected void updateBackground(){
        enc1.updateNormal(); enc2.updateNormal();  gyro.updateHeading();
    }

    public final void reset(){
        synchronized (currentPose) { currentPose.setX(0); currentPose.setY(0); currentPose.setAngle(0); }
        resetHardware();
    }

    protected void resetHardware(){
        enc1.reset(); enc2.reset(); gyro.reset();
    }

    protected void update(){
        ArrayList<Double> newDeltaPositionsEnc1 = enc1.getNewDeltaPositions();
        ArrayList<Double> newDeltaPositionsEnc2 = enc2.getNewDeltaPositions();
        ArrayList<Double> newHeadingPositions = gyro.getNewDeltaHeadings();
        for (int i = 0; i < newDeltaPositionsEnc1.size(); i++) {
            update(newDeltaPositionsEnc1.get(i), newDeltaPositionsEnc2.get(i), 0, newHeadingPositions.get(i));
        }
    }

    private void update(double deltaEnc1, double deltaEnc2, double deltaEnc3, double deltaHeading){
        Pose deltaPose = update(new Vector3D(deltaEnc1, deltaEnc2, deltaEnc3).getScaled(encoderWheelDiameter*Math.PI/Constants.ENCODER_TICKS_PER_REV), new Vector(gyro.getHeading()), deltaHeading);
        synchronized (currentPose){ currentPose.add(deltaPose); }
    }

    protected Pose update(Vector3D deltaEnc, Vector headingVector, double deltaHeading){
        Vector dThetaVector = new Vector(enc1Pose.getVector().getCrossProduct(headingVector), enc2Pose.getVector().getCrossProduct(headingVector));
        Vector output = deltaEnc.get2D().getSubtracted(dThetaVector);
        Matrix2D dXdYMatrix = new Matrix2D(
               enc1Pose.getUnitVector().getDotProduct(Vector.xHat()), enc1Pose.getUnitVector().getDotProduct(Vector.yHat()),
               enc2Pose.getUnitVector().getDotProduct(Vector.xHat()), enc2Pose.getUnitVector().getDotProduct(Vector.yHat())
        );
        Vector deltaPos = Matrix2D.solve(dXdYMatrix, output);
        return new Pose(deltaPos, deltaHeading);
    }

    public final Pose getPose(){ return currentPose; }

}

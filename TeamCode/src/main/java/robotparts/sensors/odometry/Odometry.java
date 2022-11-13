package robotparts.sensors.odometry;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import math.linearalgebra.Matrix2D;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;
import robotparts.sensors.GyroSensors;
import util.ExceptionCatcher;
import util.codeseg.ExceptionCodeSeg;
import util.template.Iterator;

import static robot.RobotFramework.odometryThread;

public abstract class Odometry extends RobotPart {
    protected ArrayList<IEncoder> encoders = new ArrayList<>();
    protected final Pose currentPose = new Pose();
    private final ExceptionCodeSeg<RuntimeException> odometryUpdateCode = this::internalUpdate;
    private boolean usingGyro = false;

    @Override
    public final void init() {
        createEncoders();
        setEncoderPoses();
        setConstantObjects();
        odometryThread.setExecutionCode(odometryUpdateCode);
    }

    protected final void addEncoders(IEncoder... encoders){ this.encoders.addAll(Arrays.asList(encoders)); }
    protected final void useGyro(){ usingGyro = true; }
    protected final void unUseGyro(){ usingGyro = false; }

    protected abstract void createEncoders();
    protected abstract void setEncoderPoses();
    protected abstract void setConstantObjects();
    protected abstract void update();
    protected void resetObjects(){}

    private void internalUpdate(){
        Iterator.forAll(encoders, IEncoder::updateNormal);
        if(usingGyro){ gyro.update(); }
        update();
    }

    public final Pose getPose(){ return currentPose; }
    public final double getX(){ return currentPose.getX(); }
    public final double getY(){ return currentPose.getY(); }
    public final double getHeading(){ return currentPose.getAngle(); }

    public final void setCurrentPose(Pose pose){
        synchronized (currentPose) { currentPose.setX(pose.getX()); currentPose.setY(pose.getY()); currentPose.setAngle(pose.getAngle()); }
    }

    public final void updateCurrentPose(Vector delta, double deltaHeading){
        synchronized (currentPose) { currentPose.add(delta); currentPose.setAngle(getHeading()+deltaHeading); }
    }

    protected final void setHeading(double heading){ currentPose.setAngle(heading); }

    protected Vector toGlobalFrame(Vector v){ return v.getRotated(getHeading()); }

    @Override
    public final void reset(){
        if(usingGyro){ gyro.reset(); }
        Iterator.forAll(encoders, IEncoder::reset);
        ExceptionCatcher.catchInterrupted(() -> Thread.sleep(50));
        currentPose.setZero();
        resetObjects();
    }

    @Override
    public final String toString() {
        return String.format(Locale.US, "%f, %f, %f", currentPose.getX(), currentPose.getY(), currentPose.getAngle());
    }

}

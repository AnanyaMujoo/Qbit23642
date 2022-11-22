package robotparts.sensors.odometry;

import java.util.ArrayList;
import java.util.Arrays;

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
//
//    private double deltaEnc1 = 0;
//    private double deltaEnc2 = 0;
//    private double deltaEnc3 = 0;

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
        double start = -11.5; double width = 21.8;
        enc1Pose = new Pose(new Point(start,5.5), 90);
        enc2Pose = new Pose(new Point(1.5,-12.5), 0.0);
        enc3Pose = new Pose(new Point(start+width,5.5), 90);
    }

    @Override
    protected void update() {
//
//        deltaEnc1 += enc1.getDeltaPosition();
//        deltaEnc2 += enc2.getDeltaPosition();
//        deltaEnc3 += enc3.getDeltaPosition();
//
//
//
//        log.show(Arrays.toString(new Double[]{deltaEnc1, deltaEnc2, deltaEnc3}));

////
//        log.show(Arrays.toString(new Double[]{
//            enc1Pose.getAngleUnitVector().getX(), enc1Pose.getAngleUnitVector().getY(), enc1Pose.getVector().getCrossProduct(enc1Pose.getAngleUnitVector()),
//                    enc2Pose.getAngleUnitVector().getX(), enc2Pose.getAngleUnitVector().getY(), enc2Pose.getVector().getCrossProduct(enc2Pose.getAngleUnitVector()),
//                    enc3Pose.getAngleUnitVector().getX(), enc3Pose.getAngleUnitVector().getY(), enc3Pose.getVector().getCrossProduct(enc3Pose.getAngleUnitVector())
//        }));

        Vector3D localEncDelta = new Vector3D(enc1.getDeltaPosition(), enc2.getDeltaPosition(), enc3.getDeltaPosition());
        Vector3D localDelta = dYdXdThetaMatrixInverted.multiply(localEncDelta);
        updateCurrentPose(toGlobalFrame(localDelta.get2D()), Math.toDegrees(localDelta.getZ()));


////        Vector localOffset = new Vector(-Math.abs(Math.toRadians(gyro.getDeltaHeading()))*1.33, 0);
//        Vector localOffset = new Vector(0,0);
//
//        Vector localDelta2D = localDelta.get2D();
//        localDelta2D.scaleY(1.0155);
//        Vector globalDelta = toGlobalFrame(localDelta.get2D());
//        updateCurrentPose(globalDelta, Math.toDegrees(localDelta.getZ()));
//        precision.throttle(() -> setHeading(gyro.getHeading()), 100);

        // TODO 4 FIX Make reliable and accurate
    }

    @Override
    protected void setConstantObjects() {
//        dYdXdThetaMatrixInverted = new Matrix3D(
//                enc1Pose.getAngleUnitVector().getX(), enc1Pose.getAngleUnitVector().getY(), enc1Pose.getVector().getCrossProduct(enc1Pose.getAngleUnitVector()),
//                enc2Pose.getAngleUnitVector().getX(), enc2Pose.getAngleUnitVector().getY(), enc2Pose.getVector().getCrossProduct(enc2Pose.getAngleUnitVector()),
//                enc3Pose.getAngleUnitVector().getX(), enc3Pose.getAngleUnitVector().getY(), enc3Pose.getVector().getCrossProduct(enc3Pose.getAngleUnitVector())
//        ).getInverted();


        dYdXdThetaMatrixInverted = new Matrix3D(
                0, 1, enc1Pose.getVector().getCrossProduct(enc1Pose.getAngleUnitVector()),
                1, 0, enc2Pose.getVector().getCrossProduct(enc2Pose.getAngleUnitVector()),
                0, 1, enc3Pose.getVector().getCrossProduct(enc3Pose.getAngleUnitVector())
        ).getInverted();
//
//        dYdXdThetaMatrixInverted = new Matrix3D(
//                0, 1, -11.5,
//               1, 0, 12.5,
//                0, 1, 10.3
//        ).getInverted();

        // 0 1 -11.5
        // 1 0 12.5
        // 0 1 10


        // enc1       enc3
        //
        //       enc2
    }

    @Override
    protected void resetObjects() {
        super.resetObjects();
//        deltaEnc1 = 0;
//        deltaEnc2 = 0;
//        deltaEnc3 = 0;
    }
}

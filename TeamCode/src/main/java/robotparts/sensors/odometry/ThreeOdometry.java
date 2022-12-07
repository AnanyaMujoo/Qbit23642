package robotparts.sensors.odometry;

import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import math.linearalgebra.Matrix2D;
import math.linearalgebra.Matrix3D;
import math.linearalgebra.Vector3D;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IEncoder;
import util.template.Precision;

import static java.lang.Math.*;

public class ThreeOdometry extends TwoOdometry {
    private IEncoder enc3; // right
    public final double width = 21.4507;
    public double angleLeft = toRadians(1.3);
    public double angleRight = toRadians(1.3);
    public final double scaleX = 1.004;
    public final double scaleY = 1.004;
    public double scaleRight = 1.015;
    public int mode = 0;
    public final Point odometryCenter = new Point();
    private final Vector leftOdometryCenterToRobotCenter = new Vector(11.5, 13.0);
    private Precision precision;

    @Override
    protected void createEncoders() {
        super.createEncoders();
        enc3 = create("brEnc", ElectronicType.IENCODER_NORMAL);
        addEncoders(enc3);
        enc3.invert();
        precision = new Precision();
    }

    @Override
    protected void update() {

        double dyl = 0, dyr = 0, dx = 0, dh = 0;

//        if(mode == 0) {
//            dx = enc2.getDeltaPosition();
//            dyl = (enc1.getDeltaPosition() - dx*sin(angleLeft));
//        }else if(mode == 1){
//            dx = enc2.getDeltaPosition();
//        }else if(mode == 2){
//            dx = enc2.getDeltaPosition();
//            dyl = (enc3.getDeltaPosition() - dx*sin(angleRight));
//        }
//
        dx = enc2.getDeltaPosition();
        dyl = (enc1.getDeltaPosition() - dx*sin(angleLeft));
        dyr = (enc3.getDeltaPosition() - dx*sin(angleRight));
        dh = toDegrees(((scaleRight*dyr)-dyl)/width);

        Vector localDelta = new Vector(dx, dyl);
        localDelta = localDelta.getRotated(0.3);
        localDelta.scaleY(1.01);
        localDelta.scaleX(1.01);
//        setHeading(gyro.getHeading());
//
        updateCurrentHeading(dh);

        precision.throttle(() -> setHeading(gyro.getHeading()), 100);

        odometryCenter.translate(toGlobalFrame(localDelta));
        Vector globalOdometryCenterToRobotCenter = toGlobalFrame(leftOdometryCenterToRobotCenter).getSubtracted(leftOdometryCenterToRobotCenter);
        setCurrentPose(odometryCenter.getAdded(globalOdometryCenterToRobotCenter.getPoint()));
    }

    @Override
    protected void resetObjects() { odometryCenter.set(new Point()); }
}

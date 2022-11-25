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

public class ThreeOdometry extends TwoOdometry {
    private IEncoder enc3;
    public final double width = 20.6;
    public final double angle = Math.toRadians(5);
    public final double angle2 = Math.toRadians(0.5);
    public final Point odometryCenter = new Point();
    private final Vector odometryCenterToRobotCenter = new Vector(11.5, 13.0);

    @Override
    protected void createEncoders() {
        super.createEncoders();
        enc3 = create("brEnc", ElectronicType.IENCODER_NORMAL);
        addEncoders(enc3);
        enc3.invert();
    }

    @Override
    protected void update() {


        double dy = enc1.getDeltaPosition();
        double dx = (enc2.getDeltaPosition() - Math.sin(angle2)*dy)/Math.cos(angle2);
        double dh = (enc3.getDeltaPosition() - (dx*Math.sin(angle)) - (dy*Math.cos(angle)))/(Math.cos(angle)*width);


        Vector localDelta = new Vector(dx, dy);
        localDelta.scaleY(1.015);
        localDelta.scaleX(1.005);

//        if(dh != 0.0){ localDelta = Matrix2D.getIntegratedFromZeroRotationMatrix(dh).getMultiplied(1.0 / dh).multiply(localDelta); }

//        odometryCenter.translate(localDelta);
        odometryCenter.translate(toGlobalFrame(localDelta));
        Vector globalOdometryCenterToRobotCenter = toGlobalFrame(odometryCenterToRobotCenter).getSubtracted(odometryCenterToRobotCenter);
        setCurrentPose(new Pose(odometryCenter.getAdded(globalOdometryCenterToRobotCenter.getPoint()), getHeading() + Math.toDegrees(dh)));
    }

    @Override
    protected void resetObjects() { odometryCenter.set(new Point()); }
}

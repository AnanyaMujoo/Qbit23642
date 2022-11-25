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
    public double angle2 = Math.toRadians(0.5);
    public double angle3 = Math.toRadians(2);
    public final double scale2 = 1.0;
    public final double scale3 = 1.0;
    public int mode = 0;
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

        double dy;
        if(mode == 0) {
            dy = enc1.getDeltaPosition();
        }else if(mode == 1){
            dy = enc2.getDeltaPosition();
        }else{
            dy = enc3.getDeltaPosition();
        }

        double dx = 0;
        double dh = 0;


//
//        double dx = ((scale2*enc2.getDeltaPosition()) - Math.sin(angle2)*dy)/Math.cos(angle2);
//        double dh = ((scale3*enc3.getDeltaPosition()) - (dx*Math.sin(angle3)) - (dy*Math.cos(angle3)))/(Math.cos(angle3)*width);
//
//
        Vector localDelta = new Vector(dx, dy);

//        if(dh != 0.0){ localDelta = Matrix2D.getIntegratedFromZeroRotationMatrix(dh).getMultiplied(1.0 / dh).multiply(localDelta); }

        odometryCenter.translate(localDelta);
//        odometryCenter.translate(toGlobalFrame(localDelta));
        Vector globalOdometryCenterToRobotCenter = toGlobalFrame(odometryCenterToRobotCenter).getSubtracted(odometryCenterToRobotCenter);
        setCurrentPose(new Pose(odometryCenter.getAdded(globalOdometryCenterToRobotCenter.getPoint()), getHeading() + Math.toDegrees(dh)));
    }

    @Override
    protected void resetObjects() { odometryCenter.set(new Point()); }
}

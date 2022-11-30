package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.generators.PoseGenerator;
import autoutil.vision.JunctionScanner;
import geometry.position.Pose;
import geometry.position.Vector;
import util.template.Precision;

public class MecanumJunctionReactor extends MecanumPIDReactor{

    // TODO TEST

    public static final JunctionScanner junctionScanner = new JunctionScanner();
    private static final Pose junctionTargetPose = new Pose(0, 20, 0);
    private Pose startOdometryPose;
    private Pose startJunctionPose;


    @Override
    public Pose getPose() {
        Pose junctionPose = junctionScanner.getPose();
        Pose odometryPose = super.getPose();
//
//        if(junctionPose.getPoint().getDistanceTo(junctionTargetPose.getPoint()) < 5 && startOdometryPose == null){
//            startOdometryPose = odometryPose;
//            startJunctionPose = junctionPose;
//        }
//
//        Vector displacement = new Vector(startOdometryPose.getPoint(), odometryPose.getPoint());
//        double headingDisplacement = odometryPose.getAngle() - startOdometryPose.getAngle();
//
//        Vector startPoint = new Vector(startJunctionPose.getPoint());
//
//        double currentHeading = startJunctionPose.getAngle() + headingDisplacement;
//
//        Vector currentPoint = startPoint.getAdded(displacement.getRotated(startJunctionPose.getAngle() - startOdometryPose.getAngle()));
//
//        return new Pose(currentPoint.getX(), currentPoint.getY(), currentHeading);

        return junctionPose;
    }

    @Override
    public void setTarget(Pose target) { super.setTarget(junctionTargetPose); }


}

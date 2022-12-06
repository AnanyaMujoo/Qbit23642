package autoutil.reactors;

import autoutil.Profiler;
import autoutil.controllers.control2D.Default2D;
import autoutil.generators.PoseGenerator;
import autoutil.vision.JunctionScanner;
import geometry.circles.Circle;
import geometry.framework.Point;
import geometry.position.Line;
import geometry.position.Pose;
import geometry.position.Vector;
import util.template.Precision;

public class MecanumJunctionReactor extends MecanumPIDReactor{

    public static final JunctionScanner junctionScanner = new JunctionScanner();
    private static final Pose junctionTargetPose = new Pose(0, 20, 0);
    private static Point junctionLocation;
    private Pose startOdometryPose;
    private Pose startJunctionPose;
    private Precision precision;
    {
        junctionLocation = new Point(30.5, -122);
    }

    public static void flip(){ junctionLocation.reflectX();junctionLocation.reflectY();}

    @Override
    public void init() { super.init(); precision = new Precision(); }

    @Override
    public Pose getPose() {
        Pose junctionPose = junctionScanner.getPose();
        Pose odometryPose = super.getPose();


        // TODO TEST
//
//        Circle detectionCircle = new Circle(junctionLocation, junctionPose.getPoint().getDistanceToOrigin());
//        Point closestPoint = detectionCircle.getClosestTo(odometryPose.getPoint());
//        Line errorLine = new Line(closestPoint, odometryPose.getPoint());
//        if(precision.isInputTrueForTime(errorLine.getLength() < 5.0, 0.4)) {
//            Point adjustedPoint = errorLine.getMidpoint();
//            odometry.setCurrentPose(adjustedPoint);
//        }
//



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

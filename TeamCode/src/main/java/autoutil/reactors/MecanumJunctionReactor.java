package autoutil.reactors;

import autoutil.Profiler;
import autoutil.controllers.control1D.PID;
import autoutil.controllers.control2D.Default2D;
import autoutil.generators.PoseGenerator;
import autoutil.vision.JunctionScanner;
import geometry.circles.Circle;
import geometry.framework.Point;
import geometry.position.Line;
import geometry.position.Pose;
import geometry.position.Vector;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Precision;

import static global.General.fault;
import static global.General.log;

public class MecanumJunctionReactor extends MecanumReactor{

    public static final JunctionScanner junctionScanner = new JunctionScanner();
    private static final Pose junctionTargetPose = new Pose(0, 25, 0);
    private static Point junctionLocation;
    private Pose startOdometryPose = new Pose(0,0,0);
    private Pose startJunctionPose = junctionTargetPose.getAdded(new Pose(0,5,0));
    private Precision precision;

    public final PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.015, 1000.0, 0.11, 20.0, 5.0);
    public final PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.015, 1000.0, 0.13, 20.0, 5.0);
    public final PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.005, 1000.0, 0.09, 20.0, 5.0);

    private static boolean autoMode = false;

    public MecanumJunctionReactor(){
        hPID.setAccuracy(2.0);
        xPID.setAccuracy(1.0);
        yPID.setAccuracy(1.0);

        hPID.setRestOutput(0.065);
        xPID.setRestOutput(0.095);
        yPID.setRestOutput(0.065);
        setControllers(new Default2D(xPID, yPID), hPID);
    }

    public static void setToAuto(){ autoMode = true; }
    public static void flip(){ junctionLocation.reflectX(); junctionLocation.reflectY();}

    @Override
    public void init() {
        super.init();
        precision = new Precision();
        junctionLocation = new Point(30.5, -122);
        startOdometryPose = new Pose(0,0,0);
        startJunctionPose = junctionTargetPose.getAdded(new Pose(0,5,0));
        JunctionScanner.resume();
    }

    @Override
    public Pose getPose() {
        // TODO TEST

        Pose junctionPose = junctionScanner.getPose();
        Pose odometryPose = super.getPose();

        if(autoMode) {

            Circle detectionCircle = new Circle(junctionLocation, junctionPose.getPoint().getDistanceToOrigin());
            Point closestPoint = detectionCircle.getClosestTo(odometryPose.getPoint());
            Line errorLine = new Line(closestPoint, odometryPose.getPoint());
            if(precision.isInputTrueForTime(errorLine.getLength() < 3.0, 0.6)) {
                Point adjustedPoint = errorLine.getMidpoint();
                double currentAngle = errorLine.getVector().getTheta();
                Pose currentPose = new Pose(adjustedPoint, currentAngle);
                odometry.setCurrentPose(currentPose);
            }

        }else{
            if (!precision.isInputTrueForTime(junctionPose.getPoint().getDistanceTo(junctionTargetPose.getPoint()) > 15, 0.6)) {

                startOdometryPose = odometryPose;
                startJunctionPose = junctionPose;

                Vector displacement = new Vector(startOdometryPose.getPoint(), odometryPose.getPoint());
                double headingDisplacement = odometryPose.getAngle() - startOdometryPose.getAngle();

                Vector startPoint = new Vector(startJunctionPose.getPoint());

                double currentHeading = startJunctionPose.getAngle() + headingDisplacement;

                Vector currentPoint = startPoint.getAdded(displacement.getRotated(startJunctionPose.getAngle() - startOdometryPose.getAngle()));

//        return new Pose(currentPoint.getX(), currentPoint.getY(), currentHeading);
                return new Pose(0, currentPoint.getY(), currentHeading);
            } else {
                fault.warn("Exiting, farther than 15 cm from target", Expectation.EXPECTED, Magnitude.MINOR);
            }
        }
        return junctionTargetPose;
    }

    @Override
    public void setTarget(Pose target) { super.setTarget(junctionTargetPose); }

    @Override
    public void nextTarget() { super.nextTarget(); JunctionScanner.pause(); }
}

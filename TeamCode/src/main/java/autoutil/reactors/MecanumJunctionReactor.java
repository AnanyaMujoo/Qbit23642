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
import util.Timer;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Precision;

import static global.General.fault;
import static global.General.log;

public class MecanumJunctionReactor extends MecanumPIDReactor{

    public static final JunctionScanner junctionScanner = new JunctionScanner();
    private static final Pose junctionTargetPose = new Pose(0, 18, 0);
    private static Point junctionLocation;
    private Pose startOdometryPose = new Pose(0,0,0);
    private Pose startJunctionPose = junctionTargetPose.getAdded(new Pose(0,5,0));
    private Precision precision;
    private Pose lastJunctionPose = startJunctionPose;

    private static boolean autoMode = false;
    private static boolean waiting = true;
    private static final Timer timer = new Timer();

    public static void setToAuto(){ autoMode = true; }
    public static void flip(){ junctionLocation.reflectX(); junctionLocation.reflectY();}

    @Override
    public void init() {
        super.init();
        precision = new Precision();
        junctionLocation = new Point(30.5, -122);
        startOdometryPose = new Pose(0,0,0);
        startJunctionPose = junctionTargetPose.getAdded(new Pose(0,5,0));
        lastJunctionPose = startJunctionPose;
        waiting = true;
        timer.reset();
        JunctionScanner.resume();
    }

    @Override
    public Pose getPose() {

        Pose junctionPose = junctionScanner.getPose();
        Pose odometryPose = super.getPose();

        if(autoMode) {

//            Circle detectionCircle = new Circle(junctionLocation, junctionPose.getPoint().getDistanceToOrigin());
//            Point closestPoint = detectionCircle.getClosestTo(odometryPose.getPoint());
//            Line errorLine = new Line(closestPoint, odometryPose.getPoint());
//            if(precision.isInputTrueForTime(errorLine.getLength() < 3.0, 0.6)) {
//                Point adjustedPoint = errorLine.getMidpoint();
//                double currentAngle = errorLine.getVector().getTheta();
//                Pose currentPose = new Pose(adjustedPoint, currentAngle);
//                odometry.setCurrentPose(currentPose);
//            }

        }else{

            double maxDisFromTarget = 15;
            double maxDisVariation = 2;
            double maxAngleVariation = 3;
            int n = 5;
            if(junctionPose.getDistanceTo(junctionTargetPose) < maxDisFromTarget
                    && junctionScanner.distanceProfiler.areLastValuesNearby(n, maxDisVariation)
                    && junctionScanner.angleProfiler.areLastValuesNearby(n, maxAngleVariation) && waiting){
                startOdometryPose = odometryPose.getCopy();
                startJunctionPose = junctionScanner.getAveragePose(n).getCopy();
                waiting = false;
            }else if(!waiting){
                Vector odoDisplacement = new Vector(startOdometryPose.getPoint(), odometryPose.getPoint());
                double headingDisplacement = odometryPose.getAngle() - startOdometryPose.getAngle();
                double currentHeading = startJunctionPose.getAngle() + headingDisplacement;
                Vector currentPosition = new Vector(startJunctionPose.getPoint()).getAdded(odoDisplacement);
                return new Pose(currentPosition.getX(), currentPosition.getY(), currentHeading);
            }else if(timer.seconds() > 5){
                fault.warn("Exiting, took longer than 10s to lock target", Expectation.EXPECTED, Magnitude.MINOR);
                waiting = false;
            }

        }
        return junctionTargetPose;
    }

    @Override
    public boolean isAtTarget() {
        return super.isAtTarget() && !waiting;
    }

    @Override
    public void setTarget(Pose target) { super.setTarget(junctionTargetPose); }

    @Override
    public void nextTarget() { super.nextTarget(); JunctionScanner.pause(); }
}

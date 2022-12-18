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

import static global.General.bot;
import static global.General.fault;
import static global.General.log;

public class MecanumJunctionReactor extends MecanumPIDReactor{

    public static final JunctionScanner junctionScanner = new JunctionScanner();
    private static final Pose junctionTargetPose = new Pose(0, 23, 0);
    private static double cameraToRobotCenter = 19.5;
    private static Point junctionLocation = new Point();
    private Pose startOdometryPose = new Pose();
    private Pose startJunctionPose = junctionTargetPose.getAdded(new Pose(0,5,0));

    private static boolean flipped = false;
    private static boolean auto = false;
    private static boolean waiting = true;
    private static boolean exit = false;
    private static final Timer timer = new Timer();

    public static void setFlipped(boolean flipped){ auto = true; junctionLocation = new Point(flipped ? -30.0 : 30.0, -158); MecanumJunctionReactor.flipped = flipped; }

    @Override
    public void init() {
        super.init();
        startOdometryPose = new Pose(0,0,0);
        startJunctionPose = junctionTargetPose.getAdded(new Pose(0,5,0));
        waiting = true; exit = false;
        junctionScanner.reset();
        timer.reset();
        JunctionScanner.resume();
    }

    @Override
    public Pose getPose() {

        Pose junctionPose = junctionScanner.getPose();
        Pose odometryPose = super.getPose();

//        cameraToRobotCenter = 20.5;
        if(flipped){
            cameraToRobotCenter = 20.5;
        }

        double maxDisFromTarget = 15;
        double maxDisVariation = 2;
        double maxAngleVariation = 3;
        int n = 5;
        if(junctionPose.getDistanceTo(junctionTargetPose) < maxDisFromTarget
                && junctionScanner.distanceProfiler.areLastValuesNearby(n, maxDisVariation)
                && junctionScanner.angleProfiler.areLastValuesNearby(n, maxAngleVariation) && waiting){

            startOdometryPose = odometryPose.getCopy();
            startJunctionPose = junctionScanner.getAveragePose(n).getCopy();

            if(auto) {

                Circle detectionCircle = new Circle(junctionLocation.getCopy(), startJunctionPose.getPoint().getCopy().getDistanceToOrigin() + cameraToRobotCenter);
                Point closestPoint = detectionCircle.getClosestTo(startOdometryPose.getPoint()).getCopy();
                Line error = new Line(closestPoint.getCopy(), startOdometryPose.getPoint().getCopy());
                Line detection = new Line(junctionLocation.getCopy(), closestPoint.getCopy());

                if(error.getLength() < 5){
                    Point position = error.getMidpoint();
//                    log.record("Odometry Start", startOdometryPose);
//                    log.record("Detection" , detection);
//                    log.record("Flipped", flipped);
//                    log.record("StartAngle", startJunctionPose.getAngle());
                    double angle = (detection.getVector().getRotated(-90).getTheta()+startJunctionPose.getAngle());
//                    log.record("angle", angle);
                    Pose robotPose = new Pose(position, (angle + startOdometryPose.getAngle())/2.0);
                    odometry.setCurrentPose(robotPose);
                }
                exit = true;
            }

            waiting = false;
        }else if(!waiting){
            Vector odoDisplacement = new Vector(startOdometryPose.getPoint(), odometryPose.getPoint());
            double headingDisplacement = odometryPose.getAngle() - startOdometryPose.getAngle();
            double currentHeading = startJunctionPose.getAngle() + headingDisplacement;
            Vector currentPosition = new Vector(startJunctionPose.getPoint()).getAdded(odoDisplacement);
            return new Pose(currentPosition.getX(), currentPosition.getY(), currentHeading);
        }else if(timer.seconds() > 3){
            fault.warn("Exiting, took longer than 10s to lock target", Expectation.EXPECTED, Magnitude.MINOR);
            waiting = false;
            exit = true;
        }
        return junctionTargetPose;
    }

    @Override
    public boolean isAtTarget() {
        return exit;
//        return (super.isAtTarget() && !waiting) || exit;
    }

    @Override
    public void setTarget(Pose target) { super.setTarget(junctionTargetPose); }


    @Override
    public void nextTarget() { super.nextTarget(); JunctionScanner.pause(); }

    @Override
    public void moveToTarget(PoseGenerator generator) {
        getPose();
        drive.halt();
    }
}

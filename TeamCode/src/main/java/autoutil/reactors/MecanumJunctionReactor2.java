package autoutil.reactors;

import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.Profiler;
import autoutil.controllers.control1D.PID;
import autoutil.controllers.control2D.Default2D;
import autoutil.generators.PoseGenerator;
import autoutil.vision.JunctionScanner;
import autoutil.vision.JunctionScannerAll;
import geometry.circles.Circle;
import geometry.framework.CoordinatePlane;
import geometry.framework.Point;
import geometry.position.Line;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Modes;
import math.linearalgebra.Vector3D;
import math.polynomial.Linear;
import robotparts.RobotPart;
import util.Timer;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Precision;

import static global.General.bot;
import static global.General.fault;
import static global.General.gph1;
import static global.General.log;
import static global.Modes.AttackMode.STICKY;

public class MecanumJunctionReactor2 extends MecanumNonstopReactor{

    public static final JunctionScannerAll junctionScanner = new JunctionScannerAll();
    private final Precision precision = new Precision();
    public static boolean stop = false;

    private Pose startOdometryPose = new Pose();
    private Pose startJunctionPose = new Pose();
    private Pose startTarget = JunctionScannerAll.getTarget();
    private final Linear yCurve = new Linear(0.025, 0.05);
    private final Linear hCurve = new Linear(0.006, 0.05);
    private Pose error = new Pose();
    private Pose lastError = new Pose();
    private boolean tracking = false;
    private final double initialStartPow = -0.1;
    private double startPow = initialStartPow;

    @Override
    public void init() { super.init(); junctionScanner.reset(); }

    @Override
    public boolean isAtTarget() {
        return stop;
    }

    @Override
    public void firstTarget() { super.firstTarget(); JunctionScannerAll.resume(); precision.reset(); stop = false; }

    @Override
    public void nextTarget() {super.nextTarget(); JunctionScannerAll.pause(); startPow = initialStartPow; }

    @Override
    public void moveToTarget(PoseGenerator generator) {
        move(true, new Pose(0,startPow,0));
    }

    public void move(boolean attack, Pose power){
        Vector3D attackPow = new Vector3D();
        Pose currentPose = JunctionScannerAll.getPose();
        boolean inRange = currentPose.within(startTarget, 15, 30);
        if(attack && inRange){ tracking = true; }
        if(attack && tracking) {
            if (inRange && lastError.withinY(error, 2, 2)) {
                startOdometryPose = odometry.getPose();
                startJunctionPose = currentPose.getCopy();
                startTarget = JunctionScannerAll.getTarget();
                startPow = 0.0;
            }
            if(inRange && !startOdometryPose.equals(new Pose())) {
                currentPose = CoordinatePlane.applyCoordinateTransform(odometry.getPose(), p -> p.setStartInverse(startOdometryPose)).getOnlyPointRotated(startJunctionPose.getAngle()).getAdded(startJunctionPose);
                error = startTarget.getSubtracted(currentPose).getOrientationInverted();
                lastError = error.getCopy();

                if(Math.abs(gph1.ry) > 0.9 || Math.abs(gph1.rx) > 0.9 || Math.abs(gph1.lx) > 0.9){ tracking = false; }

                Vector junctionPow = new Vector(0, yCurve.fodd(error.getY()));
                junctionPow.rotate(-error.getAngle()); junctionPow.limitLength(0.3);
                attackPow = new Vector3D(junctionPow, Precision.clip(hCurve.fodd(error.getAngle()), 0.2));
            }
        }else{
            startOdometryPose = new Pose();
            startJunctionPose = new Pose();
            error = new Pose();
            lastError = new Pose();
            startTarget = JunctionScannerAll.getTarget();
        }

        drive.move(attackPow.getY() + power.getX(), attackPow.getX() + power.getY(), attackPow.getZ() + power.getAngle());

    }
}

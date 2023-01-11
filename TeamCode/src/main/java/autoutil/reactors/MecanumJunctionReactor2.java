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
import static global.General.log;
import static global.Modes.AttackMode.STICKY;

public class MecanumJunctionReactor2 extends MecanumNonstopReactor{

    public static final JunctionScannerAll junctionScanner = new JunctionScannerAll();
    Linear yCurve = new Linear(0.02, 0.06);
    Linear hCurve = new Linear(0.008, 0.04);
    private Pose error = new Pose();
    private final Precision precision = new Precision();
    public static boolean stop = false;


    @Override
    public void init() { super.init(); junctionScanner.reset(); }

    @Override
    public boolean isAtTarget() { return stop || (!(error.getLength() < 0.001) && error.getLength() < 1.0 && Math.abs(error.getAngle()) < 1.0); }

    @Override
    public void firstTarget() { super.firstTarget(); JunctionScannerAll.resume(); precision.reset(); stop = false; }

    @Override
    public void nextTarget() {super.nextTarget(); JunctionScannerAll.pause(); }

    @Override
    public void moveToTarget(PoseGenerator generator) {
//        error = JunctionScannerAll.getError().getCopy();
        Vector junctionPow = new Vector(0, yCurve.fodd(error.getY()));
        junctionPow.rotate(-error.getAngle());
        junctionPow.limitLength(0.5);
        drive.move(junctionPow.getY(), 0, Precision.clip(hCurve.fodd(error.getAngle()), 0.5));
    }
}

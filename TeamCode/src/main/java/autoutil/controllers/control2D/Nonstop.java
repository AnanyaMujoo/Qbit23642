package autoutil.controllers.control2D;

import autoutil.controllers.control1D.RP;
import autoutil.generators.Generator;
import autoutil.generators.LineGenerator;
import geometry.framework.Point;
import geometry.position.Line;
import geometry.position.Pose;
import geometry.position.Vector;
import math.polynomial.Linear;
import util.Timer;
import util.codeseg.ReturnCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Precision;

import static global.General.fault;

public class Nonstop extends Controller2D{

    private Line currentLine = new Line();
    private final RP rpController;

    private final Timer timer = new Timer();
    private double t = 0;
    private static final double tOffset = 0.05;
    private static final double maxVelocity = 100; // cm/s
    private static final double endTp = 0.1;
    private boolean setpoint = false;


    public Nonstop(double kp, double restPower, double accuracy){
        rpController = new RP(kp, restPower); rpController.setProcessVariable(() -> 0.0);
        rpController.setMinimumTime(0.01); rpController.setAccuracy(0.0);
        setAccuracy(accuracy);
    }

    public void setpoint(){ setpoint = true; }

    @Override
    public void scale(double scale) { this.scale = scale; }

    @Override
    public void scaleAccuracy(double scale) { this.accuracyScale = scale; }

    @Override
    public void setProcessVariable(ReturnCodeSeg<Double> processVariableX, ReturnCodeSeg<Double> processVariableY) { super.setProcessVariable(() -> 0.0); }

    @Override
    public void setTarget(Point point) {}

    @Override
    protected void updateController(Pose pose, Generator generator) {
        checkGenerator(generator, LineGenerator.class, g -> currentLine = g.getLine());
        t = Precision.clip(timer.seconds() / currentLine.getLength() * maxVelocity * scale, 1);
        Point target = currentLine.getAt(Precision.clip(t+tOffset, 1));
        setProcessError(() -> currentLine.getEndPoint().getDistanceTo(pose.getPoint()));
        Vector error = target.getSubtracted(pose.getPoint()).getVector();
        rpController.update(pose, generator);
        rpController.setProcessError(error::getLength);
        Vector power = error.getUnitVector().getScaled(rpController.getOutput()).getRotated(-pose.getAngle());

        if(setpoint){
            rpController.scaleKp(scale*(1-t) + endTp*t);
        }else {
            rpController.scale(scale);
        }
        setOutputX(power.getX());
        setOutputY(power.getY());
    }


    @Override
    public void reset() {
        timer.reset(); t = 0; rpController.reset();
    }

    @Override
    protected boolean hasReachedTarget() {
        return t > 0.99 && isWithinAccuracyRange();
    }
}

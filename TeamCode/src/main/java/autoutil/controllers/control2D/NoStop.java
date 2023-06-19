package autoutil.controllers.control2D;

import autoutil.controllers.control1D.RP;
import autoutil.generators.Generator;
import autoutil.generators.LineGenerator;
import geometry.framework.Point;
import geometry.position.Line;
import geometry.position.Pose;
import geometry.position.Vector;
import util.Timer;
import util.codeseg.ReturnCodeSeg;
import util.template.Precision;

public class NoStop extends Controller2D{

    private Line currentLine = new Line();
    private final RP rpController;

    private final Timer timer = new Timer();
    private double t = 0;
    private static final double tOffset = 0.05;
    private static final double maxVelocity = 100; // cm/s
    private static final double endTp = 0.1;
    private boolean setpoint = false;


    public NoStop(double kp, double restPower, double accuracy){
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

        setProcessError(() -> currentLine.getEndPoint().getDistanceTo(pose.getPoint()));

        if(!isTimed()) {
            t = Precision.clip((timer.seconds() / currentLine.getLength()) * maxVelocity * scale, 1);
        }else{
            t = isSetpoint() ? 1 : Precision.clip(timer.seconds() / time, 1);
        }

        Point target = currentLine.getAt(Precision.clip(t+tOffset, 1));
        Vector error = target.getSubtracted(pose.getPoint()).getVector();
        rpController.update(pose, generator);
        rpController.setProcessError(error::getLength);
        Vector power = error.getUnitVector().getScaled(rpController.getOutput()).getRotated(-pose.getAngle());

        rpController.scaleKp(isTimed() ? Math.max(endTp,scale)*(1-t) + endTp*t : scale);

        setOutputX(power.getX());
        setOutputY(power.getY());
    }


    @Override
    public void reset() {
        timer.reset(); t = 0; rpController.reset();
    }


    public boolean isTimed(){ return time < 10; }
    public boolean isSetpoint(){ return setpoint; }

    @Override
    protected boolean hasReachedTarget() {
        return isTimed() ? timer.seconds() > time : (isSetpoint() ? t > 0.99 && isWithinAccuracyRange() : t > 0.99);
    }

    /**
     * Waypoint - quick, inaccurate
     * Timed Waypoint - variable, moderate accuracy
     * Setpoint - slow, accurate
     * Timed Setpoint - variable, accurate
     */
}

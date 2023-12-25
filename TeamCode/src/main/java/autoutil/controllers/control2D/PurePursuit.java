package autoutil.controllers.control2D;

import autoutil.controllers.control1D.PID;
import autoutil.generators.Generator;
import autoutil.generators.LineGenerator;
import geometry.position.Line;
import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import math.misc.Exponential;
import math.polynomial.Quadratic;
import math.trigonmetry.Trig;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.ParameterConstructor;
import util.template.Precision;

import static global.General.fault;

public class PurePursuit extends Controller2D implements ParameterConstructor<Double> {
    public double maxRadius = 15;
    public double currentRadius = 5;
    private double kx;
    private double ky;
    private double radiusK;
    private double t = 0;
    private boolean isStarting = true;
    private Line currentLine = new Line();

    // TOD5 NEW Create option for tracer

    private final Exponential radiusLogistic;

    /**
     * Constructor for Pure Pursuit <br>
     * Input 1: X Proportional coefficient<br>
     * Input 2: Y Proportional coefficient <br>
     * Input 3: Radius Coefficient  <br>
     * Input 4: Max Radius <br>
     * @param parameterType
     * @param input
     */
    public PurePursuit(PurePursuitParameterType parameterType, Double... input){
        addConstructor(PurePursuitParameterType.DEFAULT, 2);
        addConstructor(PurePursuitParameterType.ALL, 4);
        createConstructors(parameterType, input, new Double[]{0.5, 0.5, 0.05, 15.0});
        xController = new PID(PID.PIDParameterType.DEFAULT, kx, 0.0, 0.0);
        yController = new PID(PID.PIDParameterType.DEFAULT, ky, 0.0, 0.0);
        radiusLogistic = new Exponential(1, 1, (radiusK/maxRadius));
    }

    @Override
    public void construct(Double[] in) {
        kx = in[0]; ky = in[1]; radiusK = in[2]; maxRadius = in[3];
    }

    @Override
    public void updateController(Pose pose, Generator generator) {
        checkGenerator(generator, LineGenerator.class, g -> currentLine = g.getLine());
        Point targetPos = getTargetPos(pose.getPoint(), currentLine);
        xController.setTarget(targetPos.getX());
        yController.setTarget(targetPos.getY());
        xController.update(pose, generator);
        yController.update(pose, generator);
        updateRadius(currentLine.getLength());
        Vector powerVector = new Vector(xController.getOutput(), yController.getOutput());
        powerVector.rotate(-pose.getAngle());
        setOutputX(powerVector.getX());
        setOutputY(powerVector.getY());
    }

    public void updateRadius(double dis){
        currentRadius = maxRadius*radiusLogistic.f(dis);
    }

    public Point getTargetPos(Point currentPos, Line currentLine){
        return currentLine.getAt(solve(currentPos, currentLine));
    }

    public double solve(Point currentPos, Line currentLine){
        double dx = currentLine.getStartPoint().getX()-currentPos.getX();
        double dy = currentLine.getStartPoint().getY()-currentPos.getY();
        double a = Trig.pythagC2(currentLine.getSlopeX(), currentLine.getSlopeY());
        double b = 2*((dx*currentLine.getSlopeX())+(dy*currentLine.getSlopeY()));
        double c = Trig.pythagC2(dx, dy)-Math.pow(currentRadius,2);
        Quadratic quadratic = new Quadratic(a, b, c);
        double[] roots = quadratic.roots();
        t = roots[0];
        isStarting = Precision.runOnCondition(Double.isNaN(t), () -> t = 1);
        return t;
    }

    @Override
    protected boolean hasReachedTarget() {
        return !isStarting && t > 0.99;
    }

    public enum PurePursuitParameterType implements ParameterType {
        DEFAULT,
        ALL
    }
}

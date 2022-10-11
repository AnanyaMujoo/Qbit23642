package autoutil.controllers.control2D;

import autoutil.controllers.control1D.PID;
import autoutil.paths.PathLine;
import autoutil.paths.PathSegment;
import geometry.position.Line;
import geometry.position.Point;
import geometry.position.Pose;
import geometry.position.Vector2;
import math.misc.Exponential;
import math.polynomial.Quadratic;
import math.trigonmetry.Trigonometry;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.ParameterConstructor;

import static global.General.fault;
import static global.General.log;

public class PurePursuit extends Controller2D implements ParameterConstructor<Double> {
    public double maxRadius = 15;
    public double currentRadius = 5;
    private double kx;
    private double ky;
    private double radiusK;

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
    public void updateController(Pose pose, PathSegment pathSegment) {
        Line currentLine = new Line(new Point(0,0), new Point(0,0));
        if(pathSegment instanceof PathLine){
            currentLine = ((PathLine) pathSegment).getLine();
        }else{
            fault.check("Use Line Generator for Pure Pursuit", Expectation.UNEXPECTED, Magnitude.CATASTROPHIC);
        }
        Point targetPos = getTargetPos(pose.getPoint(), currentLine);
        xController.setTarget(targetPos.getX());
        yController.setTarget(targetPos.getY());
//        log.show("targetpos", targetPos.toString());
//        log.show("ytarget",  yController.getTarget());
//        log.show("yerr", yController.getError());
        log.show("Current raduis", currentRadius);
        log.show("Current length", currentLine.getlength());
        xController.update(pose, pathSegment);
        yController.update(pose, pathSegment);
        updateRadius(currentLine.getlength());
        Vector2 powerVector = new Vector2(xController.getOutput(), yController.getOutput());
        powerVector.rotate(pose.getAngle());
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
        double dx = currentLine.p1.getX()-currentPos.getX();
        double dy = currentLine.p1.getY()-currentPos.getY();
        double a = Math.pow(Trigonometry.pythag(currentLine.mx, currentLine.my),2);
        double b = 2*((dx*currentLine.mx)+(dy*currentLine.my));
        double c = Math.pow(Trigonometry.pythag(dx, dy),2)-Math.pow(currentRadius,2);
        Quadratic quadratic = new Quadratic(a, b, c);
        double ans = quadratic.roots()[0];
        if(!Double.isNaN(ans)) {
            if(ans > 0.99){
                isAtTarget = true;
                return 1;
            }else {
                return ans;
            }
        }else{
            return 1;
        }
    }

    public enum PurePursuitParameterType implements ParameterType {
        DEFAULT,
        ALL
    }
}

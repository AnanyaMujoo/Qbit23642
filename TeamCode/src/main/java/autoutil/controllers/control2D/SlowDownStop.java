package autoutil.controllers.control2D;

import android.telecom.StatusHints;

import autoutil.controllers.control2D.Controller2DNew;
import autoutil.generators.Generator;
import autoutil.generators.LineGenerator;
import debugging.Fault;
import geometry.framework.Point;
import geometry.framework.Tracer;
import geometry.position.Line;
import geometry.position.Pose;
import geometry.position.Vector;
import util.Timer;

public class SlowDownStop extends Controller2DNew {


    private Line currentLine = new Line();
    public Point lastPos = new Point();
    public double lastTime = 0;
    public Timer timer = new Timer();
    public boolean exit = false;


    public double restPower, dis, minVel;

    public SlowDownStop(double restPower, double dis, double minVel){
        this.restPower = restPower;
        this.dis = dis;
        this.minVel = minVel;
    }




    @Override
    public void updateController(Pose pose, Generator generator) {
        checkGenerator(generator, LineGenerator.class, g -> currentLine = g.getLine());

        scale(scale);

        Point target = currentLine.getEndPoint();
        Vector error = target.getSubtracted(pose.getPoint()).getVector();


        Point currentPos = pose.getPoint().getCopy();
        Point deltaPos = currentPos.getSubtracted(lastPos);
        lastPos = currentPos;

        double currentTime = timer.seconds();
        double deltaTime = currentTime - lastTime;
        lastTime = currentTime;

        Vector velocity = new Vector(deltaPos.getX()/deltaTime, deltaPos.getY()/deltaTime);



        if(velocity.getLength() < minVel){
            exit = true;
        }


        double powerVal = scale*Math.exp(-3*Math.abs(currentLine.getLength() - error.getLength() + 1)/Math.abs(dis));

        Vector power = error.getUnitVector().getScaled(powerVal + restPower).getRotated(-pose.getAngle());

        setOutputX(power.getX());
        setOutputY(power.getY());

    }


    @Override
    public void reset() {
        lastTime = 0;
        timer.reset();
        lastPos = new Point();
        exit = false;
    }

    @Override
    public boolean hasReachedTarget() {
        return exit;
    }
}

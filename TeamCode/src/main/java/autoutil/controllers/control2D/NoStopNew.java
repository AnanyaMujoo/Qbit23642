package autoutil.controllers.control2D;

import autoutil.controllers.control1D.RP;
import autoutil.controllers.control1D.RV;
import autoutil.generators.Generator;
import autoutil.generators.LineGenerator;
import geometry.framework.Point;
import geometry.position.Line;
import geometry.position.Pose;
import geometry.position.Vector;
import util.Timer;
import util.codeseg.ReturnCodeSeg;
import util.template.Precision;

public class NoStopNew extends Controller2DNew {

    private final Timer timer = new Timer();
    private Line currentLine = new Line();
    public final RV rvController;

    public Point lastPos = new Point();
    public double lastTime = 0;


    public NoStopNew(double kp, double restPower, double minVel, double ratio, double accuracy){
        rvController =  new RV(kp, restPower, minVel, ratio){
            @Override
            public void setVelocity() {

            }
        };
        rvController.setProcessVariable(() -> 0.0);
        rvController.setMinimumTime(0.05);
        rvController.setAccuracy(accuracy);
    }




    @Override
    public void updateController(Pose pose, Generator generator) {
        checkGenerator(generator, LineGenerator.class, g -> currentLine = g.getLine());

        rvController.scale(scale);

        Point target = currentLine.getEndPoint();
        Vector error = target.getSubtracted(pose.getPoint()).getVector();


        Point currentPos = pose.getPoint().getCopy();
        Point deltaPos = currentPos.getSubtracted(lastPos);
        lastPos = currentPos;

        double currentTime = timer.seconds();
        double deltaTime = currentTime - lastTime;
        lastTime = currentTime;

        Vector velocity = new Vector(deltaPos.getX()/deltaTime, deltaPos.getY()/deltaTime);


        rvController.setVelocity(velocity);


        rvController.setProcessError(error::getLength);
        rvController.update(pose, generator);

        Vector power;

        if(rvController.stopMode && !rvController.endMode){
            power = velocity.getUnitVector().getScaled(rvController.getOutput()).getRotated(-pose.getAngle());
        }else{
            power = error.getUnitVector().getScaled(rvController.getOutput()).getRotated(-pose.getAngle());
        }

        setOutputX(power.getX());
        setOutputY(power.getY());



    }


    @Override
    public void reset() {
        lastTime = 0;
        timer.reset();
        lastPos = new Point();
        rvController.reset();
    }

    @Override
    public boolean hasReachedTarget() {
        return rvController.isAtTarget();
    }

}

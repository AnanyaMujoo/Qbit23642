package autoutil.reactors;

import autoutil.controllers.control1D.RP;
import autoutil.controllers.control2D.SlowDownStop;
import autoutil.generators.PoseGenerator;
import geometry.position.Pose;

public class SlowDownStopReactor extends Reactor{

    public SlowDownStop stop = new SlowDownStop(0.07, 15, 5);
    public RP hRP = new RP(0.01, 0.07);

    public SlowDownStopReactor(){
        hRP.setMinimumTime(0.0);
        hRP.setAccuracy(2.0);
        hRP.setProcessVariable(odometry::getHeading);
        hRP.reset();
    }


    @Override
    public void scale(double scale) {
        stop.scale(scale);
    }

    @Override
    public void firstTarget() {
        stop.timer.reset();
    }

    @Override
    public void init() { hRP.setProcessVariable(odometry::getHeading); }

    @Override
    public Pose getPose() { return odometry.getPose(); }

    @Override
    public void setTarget(Pose target) { hRP.setTarget(target.getAngle()); }

    @Override
    public void nextTarget() {  firstTarget(); hRP.reset();  }

    @Override
    public boolean isAtTarget() {
        return stop.hasReachedTarget();
    }

    @Override
    public void moveToTarget(PoseGenerator generator) {
        stop.updateController(getPose(), generator); hRP.update(getPose(), generator);
        drive.move(stop.getOutputY(), 1.2*stop.getOutputX(), -hRP.getOutput());
    }

    @Override
    public void setTime(double time) {

    }

    @Override
    public void scaleAccuracy(double scale) {
        stop.scaleAccuracy(scale);
    }

}

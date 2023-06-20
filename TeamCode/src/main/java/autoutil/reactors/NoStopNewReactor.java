package autoutil.reactors;

import autoutil.controllers.control1D.RP;
import autoutil.controllers.control1D.RV;
import autoutil.controllers.control2D.NoStop;
import autoutil.controllers.control2D.NoStopNew;
import autoutil.generators.PoseGenerator;
import geometry.position.Pose;

public class NoStopNewReactor extends Reactor{

    public NoStopNew noStopNew = new NoStopNew(0.01, 0.07,20, 1.0,  1);
    public RV rvHeading = new RV(0.01, 0.1, 40, 0);

    public NoStopNewReactor(){
        rvHeading.setProcessVariable(odometry::getHeading);
        rvHeading.setMinimumTime(0.05);
        rvHeading.setAccuracy(1);
        rvHeading.set1D();
        rvHeading.setStopConstant(45);
        rvHeading.scale(1);
        noStopNew.reset();
        rvHeading.reset();
    }


    @Override
    public void scale(double scale) {
        noStopNew.scale(scale);
    }

    @Override
    public void firstTarget() {
//        noStopNew.reset(); rvHeading.reset();
    }

    @Override
    public void init() { rvHeading.setProcessVariable(odometry::getHeading); }

    @Override
    public Pose getPose() { return odometry.getPose(); }

    @Override
    public void setTarget(Pose target) { rvHeading.setTarget(target.getAngle()); }

    @Override
    public void nextTarget() {  firstTarget();  }

    @Override
    public boolean isAtTarget() { return noStopNew.hasReachedTarget() && rvHeading.isAtTarget(); }

    @Override
    public void moveToTarget(PoseGenerator generator) {
        noStopNew.updateController(getPose(), generator); rvHeading.update(getPose(), generator);
        drive.move(noStopNew.getOutputY(), 1.2*noStopNew.getOutputX(), -rvHeading.getOutput());
    }

    @Override
    public void setTime(double time) {

    }

    @Override
    public void scaleAccuracy(double scale) {
        noStopNew.scaleAccuracy(scale);
    }
}

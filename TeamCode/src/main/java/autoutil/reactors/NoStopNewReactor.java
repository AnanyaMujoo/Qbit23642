package autoutil.reactors;

import autoutil.controllers.control1D.RP;
import autoutil.controllers.control1D.RV;
import autoutil.controllers.control2D.NoStop;
import autoutil.controllers.control2D.NoStopNew;
import autoutil.generators.PoseGenerator;
import geometry.position.Pose;

public class NoStopNewReactor extends Reactor{

    public NoStopNew noStopNew = new NoStopNew(0.02, 0.09,30, 1.0,2);
    public RP hRP = new RP(0.01, 0.07);
//    public RV rvHeading = new RV(0.008, 0.05, 40, 0);

    public NoStopNewReactor(){
        hRP.setMinimumTime(0.1);
        hRP.setAccuracy(2.0);

        hRP.setProcessVariable(odometry::getHeading);
//        rvHeading.setMinimumTime(0.05);
//        rvHeading.setAccuracy(2);
//        rvHeading.set1D();
//        rvHeading.setStopConstant(45);
//        rvHeading.scale(1);
        noStopNew.reset();
        hRP.reset();
//        rvHeading.reset();
    }


    @Override
    public void scale(double scale) {
        noStopNew.scale(scale);
    }

    @Override
    public void firstTarget() { }

    @Override
    public void init() { hRP.setProcessVariable(odometry::getHeading); }

    @Override
    public Pose getPose() { return odometry.getPose(); }

    @Override
    public void setTarget(Pose target) { hRP.setTarget(target.getAngle()); }

    @Override
    public void nextTarget() {  firstTarget();  }

    @Override
    public boolean isAtTarget() { return noStopNew.hasReachedTarget() && hRP.isAtTarget(); }

    @Override
    public void moveToTarget(PoseGenerator generator) {
        noStopNew.updateController(getPose(), generator); hRP.update(getPose(), generator);
        drive.move(noStopNew.getOutputY(), 1.2*noStopNew.getOutputX(), -hRP.getOutput());
    }

    @Override
    public void setTime(double time) {

    }

    @Override
    public void scaleAccuracy(double scale) {
        noStopNew.scaleAccuracy(scale);
    }

    public static class NoStopNewReactorHalt extends NoStopNewReactor{
        public NoStopNewReactorHalt(){
            super();
            noStopNew.rvController.setStopConstant(1);
            noStopNew.rvController.setAccuracy(4);
            noStopNew.rvController.setToEndModeExit();
        }

        @Override
        public boolean isAtTarget() {
            return noStopNew.hasReachedTarget();
        }
    }

    public static class NoStopNewReactorNoHeading extends NoStopNewReactor{
        public NoStopNewReactorNoHeading(){
            super();
            noStopNew.rvController.setStopConstant(30);
            noStopNew.rvController.setAccuracy(4);
            noStopNew.rvController.setMinimumTime(0.01);
            noStopNew.rvController.setToEndModeExit();
            noStopNew.rvController.setMinVelocity(80);
        }

        @Override
        public boolean isAtTarget() {
            return noStopNew.hasReachedTarget();
        }
    }
}

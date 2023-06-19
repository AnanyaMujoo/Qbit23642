package autoutil.reactors;

import autoutil.controllers.control1D.RP;
import autoutil.controllers.control2D.NoStop;
import autoutil.controllers.control2D.NoStopNew;
import autoutil.generators.PoseGenerator;

public class NoStopNewReactor extends MecanumReactor{

    public NoStopNew noStopNew = new NoStopNew(0.014, 0.07,50,0.6,   100.0);
    public RP hRP = new RP(0.012, 0.08);

    public NoStopNewReactor(){
        hRP.setMinimumTime(0.05); hRP.setAccuracy(2.0);
//        setControllers(noStopNew, hRP);
    }


    @Override
    public void scale(double scale) {
        movementController.scale(scale);
    }

    @Override
    public void firstTarget() { movementController.reset(); }

    @Override
    public boolean isAtTarget() { return movementController.isAtTarget(); }

    @Override
    public void moveToTarget(PoseGenerator generator) {
        movementController.update(getPose(), generator); headingController.update(getPose(), generator);
        drive.move(movementController.getOutputY(), 1.2*movementController.getOutputX(), -headingController.getOutput());
    }

}

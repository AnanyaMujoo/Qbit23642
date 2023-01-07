package autoutil.reactors;

import autoutil.controllers.control1D.PID;
import autoutil.controllers.control1D.RP;
import autoutil.controllers.control2D.Nonstop;
import autoutil.controllers.control2D.PurePursuit;
import autoutil.generators.PoseGenerator;
import util.template.Precision;

public class MecanumNonstopReactor extends MecanumReactor {

    public Nonstop nonstop = new Nonstop(0.15, 0.1, 100.0);
    public RP hRP = new RP(0.012, 0.08);
//    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.01, 6.0, 0.2, 50.0, 20.0);

    public MecanumNonstopReactor(){
        hRP.setMinimumTime(0.1); hRP.setAccuracy(2.0); setControllers(nonstop, hRP);
//        hPID.setMinimumTime(0.1); hPID.setAccuracy(2.0); hPID.setRestOutput(0.06); setControllers(nonstop, hPID);
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
        drive.move(movementController.getOutputY(), 1.1*movementController.getOutputX(), -headingController.getOutput());
    }


    public static class MecanumNonstopReactorSetpoint extends MecanumNonstopReactor {
        public MecanumNonstopReactorSetpoint(){ super(); nonstop.setpoint(); movementController.setAccuracy(2.0);  }
    }

}

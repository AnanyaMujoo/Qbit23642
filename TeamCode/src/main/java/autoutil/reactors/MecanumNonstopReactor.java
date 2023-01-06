package autoutil.reactors;

import autoutil.controllers.control1D.PID;
import autoutil.controllers.control2D.Nonstop;
import autoutil.controllers.control2D.PurePursuit;
import autoutil.generators.PoseGenerator;
import util.template.Precision;

public class MecanumNonstopReactor extends MecanumReactor {

    public Nonstop nonstop = new Nonstop(0.15, 0.1, 100.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.02, 6.0, 0.2, 50.0, 20.0);

    public MecanumNonstopReactor(){
        hPID.setMinimumTime(0.1); hPID.setAccuracy(1.0); hPID.setRestOutput(0.06); setControllers(nonstop, hPID);
    }

    @Override
    public void firstTarget() { movementController.reset(); }

    @Override
    public boolean isAtTarget() { return movementController.isAtTarget(); }

    @Override
    public void moveToTarget(PoseGenerator generator) {
        movementController.update(getPose(), generator); headingController.update(getPose(), generator);
        drive.move(movementController.getOutputY(), movementController.getOutputX(), -headingController.getOutput());
    }


    public static class MecanumNonstopReactorSetpoint extends MecanumNonstopReactor {
        private final Precision precision = new Precision();
        public MecanumNonstopReactorSetpoint(){ super(); nonstop.setpoint(); movementController.setAccuracy(1.0);  }

        @Override
        public void firstTarget() {
            super.firstTarget(); precision.reset();
        }

        @Override
        public boolean isAtTarget() {
            return precision.isInputTrueForTime(movementController.isAtTarget(), 0.1) && headingController.isAtTarget();
        }
    }

}

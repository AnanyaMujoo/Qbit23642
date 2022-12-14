package autoutil.reactors;

import autoutil.controllers.control1D.PID;
import autoutil.controllers.control2D.PurePursuit;
import autoutil.generators.PoseGenerator;

public class MecanumPurePursuitReactor extends MecanumReactor {

    public PurePursuit movementPurePursuit = new PurePursuit(PurePursuit.PurePursuitParameterType.ALL, 0.06, 0.06, 1.0, 15.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.007, 6.0, 0.2, 50.0, 20.0);

    public MecanumPurePursuitReactor(){
        hPID.setAccuracy(4);
        hPID.setRestOutput(0.08);
        setControllers(movementPurePursuit, hPID);
    }

    @Override
    public boolean isAtTarget() { return movementController.isAtTarget(); }

    @Override
    public void moveToTarget(PoseGenerator generator) {
        movementController.update(getPose(), generator);
        headingController.update(getPose(), generator);
        drive.move(movementController.getOutputY() + drive.getAntiTippingPower(), movementController.getOutputX(), -headingController.getOutput());
    }
}

package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.controllers.control1D.PID;
import autoutil.generators.PoseGenerator;

public class MecanumPIDReactor extends MecanumReactor{


//    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.023, 1000.0, 0.2, 60.0, 5.0);
//    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.025, 1000.0, 0.3, 60.0, 5.0);
//    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.009, 1000.0, 0.1, 60.0, 5.0);


    public MecanumPIDReactor(){

        PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.015, 10000.0, 0.05, 60.0, 5.0);
        PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.02, 10000.0, 0.05, 60.0, 5.0);
        PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.009, 10000.0, 0.025, 60.0, 5.0);

        yPID.setAccuracy(1.0);
        xPID.setAccuracy(1.0);
        hPID.setAccuracy(1.0);

        yPID.setRestOutput(0.06);
        xPID.setRestOutput(0.09);
        hPID.setRestOutput(0.06);
        setControllers(new Default2D(xPID, yPID), hPID);
    }

    @Override
    public void moveToTarget(PoseGenerator generator) {
        movementController.update(getPose(), generator);
        headingController.update(getPose(), generator);
        drive.move(movementController.getOutputY() + drive.getAntiTippingPower(), movementController.getOutputX(), -headingController.getOutput());
    }
}
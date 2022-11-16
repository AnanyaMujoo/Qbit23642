package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.controllers.control1D.PID;

public class MecanumPIDReactor extends MecanumReactor{

    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.02, 10.0, 0.1, 50.0, 5.0);
    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.02, 10.0, 0.1, 50.0, 5.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.007, 6.0, 0.2, 50.0, 20.0);

    public MecanumPIDReactor(){
        hPID.setAccuracy(3);
        hPID.setRestOutput(0.08);
        xPID.setAccuracy(1.5);
        yPID.setAccuracy(1.5);
        setControllers(new Default2D(xPID, yPID), hPID);
    }
}
package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.controllers.control1D.PID;

public class MecanumPIDReactor extends MecanumReactor{

    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.04, 5.0, 0.05, 50.0, 5.0);
    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.04, 5.0, 0.05, 50.0, 5.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.007, 6.0, 0.2, 50.0, 20.0);

    public MecanumPIDReactor(){
        hPID.setAccuracy(2);
        hPID.setRestOutput(0.08);
        setControllers(new Default2D(xPID, yPID), hPID);
    }
}
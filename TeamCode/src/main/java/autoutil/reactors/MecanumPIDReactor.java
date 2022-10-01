package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.controllers.control1D.PID;

public class MecanumPIDReactor extends MecanumReactor{

    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_EXTRA, 0.04, 5.0, 0.2, 0.2, 0.05, 50.0, 5.0, 0.05, 1.0);
    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_EXTRA, 0.04, 5.0, 0.2, 0.2, 0.05, 50.0, 5.0, 0.05, 1.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_EXTRA, 0.4, 6.0, 0.2, 0.2,  0.08, 50.0, Math.toRadians(20), 0.08, 2.0);

    public MecanumPIDReactor(){ setControllers(new Default2D(xPID, yPID), hPID); }
}
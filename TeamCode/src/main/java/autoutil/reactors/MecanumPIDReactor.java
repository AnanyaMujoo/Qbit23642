package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.controllers.control1D.PID;

public class MecanumPIDReactor extends MecanumReactor{

    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.023, 0.5, 0.03, 5.0, 5.0);
    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.023, 0.5, 0.03, 5.0, 5.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.008, 1.0, 0.015, 5.0, 5.0);

    public MecanumPIDReactor(){
        hPID.setAccuracy(0.25);
        xPID.setAccuracy(0.25);
        yPID.setAccuracy(0.25);

        hPID.setRestOutput(0.06);
        xPID.setRestOutput(0.07);
        yPID.setRestOutput(0.06);
        setControllers(new Default2D(xPID, yPID), hPID);
    }
}
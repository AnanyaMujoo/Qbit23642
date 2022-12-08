package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.controllers.control1D.PID;

public class MecanumPIDReactor extends MecanumReactor{
//
//    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.023, 0.5, 0.08, 20.0, 5.0);
//    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.025, 0.5, 0.08, 20.0, 5.0);
//    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.008, 1.0, 0.06, 20.0, 5.0);

    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.023, 1000.0, 0.14, 30.0, 5.0);
    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.025, 1000.0, 0.16, 30.0, 5.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.008, 1000.0, 0.12, 30.0, 5.0);

    public MecanumPIDReactor(){
        hPID.setAccuracy(0.25*4);
        xPID.setAccuracy(0.25*4);
        yPID.setAccuracy(0.25*4);

        hPID.setRestOutput(0.065);
        xPID.setRestOutput(0.095);
        yPID.setRestOutput(0.065);
        setControllers(new Default2D(xPID, yPID), hPID);
    }
}
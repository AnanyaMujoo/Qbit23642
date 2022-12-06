package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.controllers.control1D.PID;

public class MecanumPIDReactor extends MecanumReactor{

//    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.023, 5.0, 0.08, 30.0, 5.0);
//    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.028, 5.0, 0.08, 30.0, 5.0);
//    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.008, 3.0, 0.02, 30.0, 15.0);

    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.00, 0.0, 0.0, 30.0, 5.0);
    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.00, 0.0, 0.0, 30.0, 5.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.00, 0.0, 0.0, 30.0, 15.0);

    public MecanumPIDReactor(){
        hPID.setAccuracy(0.25);
        xPID.setAccuracy(0.25);
        yPID.setAccuracy(0.25);

        hPID.setRestOutput(0.08);
        xPID.setRestOutput(0.1);
        yPID.setRestOutput(0.08);
        setControllers(new Default2D(xPID, yPID), hPID);
    }
}
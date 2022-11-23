package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.controllers.control1D.PID;

public class MecanumPIDReactor extends MecanumReactor{

    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.02, 15.0, 0.13, 30.0, 5.0);
    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.02, 15.0, 0.13, 30.0, 5.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.008, 8.0, 0.3, 30.0, 15.0);

    public MecanumPIDReactor(){
        hPID.setAccuracy(0.5);
//        hPID.setAccuracy(1.0);

        xPID.setAccuracy(0.4);
        yPID.setAccuracy(0.4);
//        xPID.setAccuracy(1.0);
//        yPID.setAccuracy(1.0);

        hPID.setRestOutput(0.1);
        xPID.setRestOutput(0.08);
        setControllers(new Default2D(xPID, yPID), hPID);
    }
}
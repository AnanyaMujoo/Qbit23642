package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.controllers.control1D.PID;

public class MecanumPIDReactor extends MecanumReactor{

    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.025, 10.0, 0.1, 50.0, 5.0);
    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.025, 10.0, 0.1, 50.0, 5.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.01, 8.0, 0.2, 50.0, 20.0);

    public MecanumPIDReactor(){
        hPID.setAccuracy(2.0);
//        hPID.setAccuracy(3);

        xPID.setAccuracy(1.0);
        yPID.setAccuracy(1.0);
//        xPID.setAccuracy(1.5);
//        yPID.setAccuracy(1.5);
        hPID.setRestOutput(0.1);
        xPID.setRestOutput(0.08);
        setControllers(new Default2D(xPID, yPID), hPID);
    }
}
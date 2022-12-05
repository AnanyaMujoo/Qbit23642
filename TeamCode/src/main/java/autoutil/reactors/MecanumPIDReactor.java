package autoutil.reactors;

import autoutil.controllers.control2D.Default2D;
import autoutil.controllers.control1D.PID;

public class MecanumPIDReactor extends MecanumReactor{

    public PID xPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.023, 10.0, 0.1, 30.0, 5.0);
    public PID yPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.028, 10.0, 0.1, 30.0, 5.0);
    public PID hPID = new PID(PID.PIDParameterType.STANDARD_FORM_ALL, 0.008, 6.0, 0.1, 30.0, 15.0);

    public MecanumPIDReactor(){
        hPID.setAccuracy(0.4);
//        hPID.setAccuracy(1.0);

        xPID.setAccuracy(0.4);
        yPID.setAccuracy(0.4);
//        xPID.setAccuracy(1.0);
//        yPID.setAccuracy(1.0);

        hPID.setRestOutput(0.08);
        xPID.setRestOutput(0.1);
        setControllers(new Default2D(xPID, yPID), hPID);
    }
}
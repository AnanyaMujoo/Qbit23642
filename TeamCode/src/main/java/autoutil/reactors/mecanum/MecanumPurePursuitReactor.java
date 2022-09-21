package autoutil.reactors.mecanum;

import autoutil.controllers.PID;
import autoutil.controllers.PurePursuit;

public class MecanumPurePursuitReactor extends MecanumReactor{

    public PurePursuit movementPurePursuit = new PurePursuit(PurePursuit.PurePursuitParameterType.ALL, 0.06, 0.06, 1.0, 15.0);
    public PID hPID =  new PID(PID.PIDParameterType.STANDARD_FORM_EXTRA, 0.3, 6.0, 0.2,
            0.2 , 0.05, 50.0, Math.toRadians(20), 0.08, 4.0);

    public MecanumPurePursuitReactor(){
        setControllers(movementPurePursuit, hPID);
    }
}

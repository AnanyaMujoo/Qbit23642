package robotparts.electronics;

import robotparts.Electronic;
import robotparts.electronics.continuous.CMotor;

public enum ElectronicType {
    CMOTOR_FORWARD,
    CMOTOR_REVERSE,
    CMOTOR_FORWARD_FLOAT,
    CMOTOR_REVERSE_FLOAT,
    CSERVO_FORWARD,
    CSERVO_REVERSE,
    PMOTOR_FORWARD,
    PMOTOR_REVERSE,
    PMOTOR_FORWARD_FLOAT,
    PMOTOR_REVERSE_FLOAT,
    PSERVO_FORWARD,
    PSERVO_REVERSE,
    ICAMERA_EXTERNAL,
    ICAMERA_INTERNAL,
    ICOLOR,
    IDISTANCE,
    IENCODER_NORMAL,
    IENCODER_PMOTOR,
    IENCODER_CMOTOR,
    IGYRO,
    ITOUCH,
    OLED
}

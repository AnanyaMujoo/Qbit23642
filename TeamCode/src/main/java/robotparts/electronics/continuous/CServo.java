package robotparts.electronics.continuous;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import robotparts.Electronic;

public class CServo extends Electronic {
    /**
     * Continuous rotation servo object
     */
    private final CRServo crservo;
    /**
     * Direction of the continuous servo
     */
    private final DcMotorSimple.Direction direction;

    /**
     * Constructor to create the cservo and init parameters
     * @param crs
     * @param dir
     */
    public CServo(CRServo crs, DcMotorSimple.Direction dir){
        crservo = crs;
        direction = dir;
        crservo.setDirection(direction);
        crservo.setPower(0);
    }

    /**
     * Set the power of the cservo if access is allowed
     * @param power
     */
    public void setPower(double power){
        if(access.isAllowed()){ crservo.setPower(power); }
    }

    /**
     * Get the logical direction of the cservo
     * @return direction
     */
    public DcMotorSimple.Direction getDirection(){
        return direction;
    }

    /**
     * Sets the power of the servo to 0
     * NOTE: This should only be called in a thread that has access to use the robot
     */
    public void halt(){ setPower(0); }
}

package robotparts.electronics.continuous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import robotparts.Electronic;

public class CMotor extends Electronic {
    /**
     * DcMotor object since its continuous
     */
    private final DcMotor motor;
    /**
     * Logical direction of the motor (what is positive)
     */
    private final DcMotorSimple.Direction direction;
    /**
     * What is the zero power behavior (i.e. should the robot actively try to stop BRAKE, or coast FLOAT)
     */
    private final DcMotor.ZeroPowerBehavior zeroPowerBehavior;

    /**
     * Constructor with parameters
     * @param m
     * @param dir
     * @param zpb
     * @param mode
     */
    public CMotor(DcMotor m, DcMotor.Direction dir, DcMotor.ZeroPowerBehavior zpb, DcMotor.RunMode mode){
        motor = m;
        direction = dir;
        zeroPowerBehavior = zpb;

        motor.setDirection(direction);
        motor.setZeroPowerBehavior(zeroPowerBehavior);
        motor.setMode(mode);

        motor.setPower(0);
    }

    /**
     * Sets the power of the motor if access is allowed
     * @param p
     */
    public void setPower(double p){ if(access.isAllowed()){ motor.setPower(p); } }

    /**
     * Gets the direction of the motor
     * @return direction
     */
    public DcMotorSimple.Direction getDirection(){
        return direction;
    }

    /**
     * Sets the power of the motor to 0
     * NOTE: This should only be called in a thread that has access to use the robot
     */
    public void halt(){ setPower(0); }
}

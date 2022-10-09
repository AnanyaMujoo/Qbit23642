package robotparts.electronics.continuous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import debugging.StallDetector;
import robotparts.Electronic;
import robotparts.electronics.input.IEncoder;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.bot;
import static global.General.fault;
import static global.General.log;

public class CMotor extends Electronic {
    /**
     * DcMotor object since its continuous
     */
    private final DcMotor motor;

    private final IEncoder motorEncoder;
    /**
     * Logical direction of the motor (what is positive)
     */
    private final DcMotorSimple.Direction direction;
    /**
     * What is the zero power behavior (i.e. should the robot actively try to stop BRAKE, or coast FLOAT)
     */
    private final DcMotor.ZeroPowerBehavior zeroPowerBehavior;

    public final StallDetector detector;

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
        motorEncoder = new IEncoder(motor, IEncoder.EncoderType.CMOTOR);
        detector = new StallDetector(motorEncoder, 10, 10);

        motor.setDirection(direction);
        motor.setZeroPowerBehavior(zeroPowerBehavior);
        motor.setMode(mode);

        motor.setPower(0);
    }


    public void useStallDetector(){
        motorEncoder.setUpdateCMotor();
    }

    /**
     * Sets the power of the motor if access is allowed
     * @param p
     */
    public void setPower(double p){
        if(access.isAllowed()){
            if(!detector.isStalling()){
                motor.setPower(p);
            }else{
                motor.setPower(0);
                fault.warn("Motor is stalling, stopped all AutoModules", Expectation.EXPECTED, Magnitude.CRITICAL);
                bot.cancelAutoModules();
            }
        }
    }

    /**
     * Gets the direction of the motor
     * @return direction
     */
    public DcMotorSimple.Direction getDirection(){
        return direction;
    }

    public IEncoder getMotorEncoder(){ return motorEncoder; }

    /**
     * Sets the power of the motor to 0
     * NOTE: This should only be called in a thread that has access to use the robot
     */
    public void halt(){ setPower(0); }

    public StallDetector getStallDetector() {
        return detector;
    }
}

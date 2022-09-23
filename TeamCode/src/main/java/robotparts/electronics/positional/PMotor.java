package robotparts.electronics.positional;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import debugging.StallDetector;
import robotparts.Electronic;
import robotparts.electronics.input.IEncoder;
import util.Timer;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.fault;

public class PMotor extends Electronic {
    /**
     * DcMotor object and related parameters
     */
    private final DcMotor motor;
    private final DcMotorSimple.Direction direction;
    private final DcMotor.ZeroPowerBehavior zeroPowerBehavior;
    private final IEncoder motorEncoder;
    private final StallDetector detector;

    // TODO 4 NEW Make this have functionality to use custom PID

    /**
     * Constructor to create a pmotor
     * @param m
     * @param dir
     * @param zpb
     * @param mode
     */
    public PMotor(DcMotor m, DcMotor.Direction dir, DcMotor.ZeroPowerBehavior zpb, DcMotor.RunMode mode){
        motor = m;
        detector = new StallDetector(motor);
        direction = dir;
        zeroPowerBehavior = zpb;

        motor.setDirection(direction);
        motor.setZeroPowerBehavior(zeroPowerBehavior);
        motor.setMode(mode);

        motor.setPower(0);

        motorEncoder = new IEncoder(motor, IEncoder.EncoderType.MOTOR);

        motorEncoder.reset();
    }

    /**
     * Set the power of the pmotor
     * @param p
     */
    public void setPower(double p){
        if(access.isAllowed()){
            if(!detector.isStalling()){
                motor.setPower(p);
            }else{
                motor.setPower(0);
                fault.warn("Motor is stalling", Expectation.EXPECTED, Magnitude.CRITICAL);
            }
        }
    }

    /**
     * Set the position in ticks to move to
     * @param pos
     */
    public void setPosition(double pos){
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setTargetPosition((int)(pos));
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Get the target position in ticks
     * @return target
     */
    public double getTarget() {
        return motor.getTargetPosition();
    }

    /**
     * Get the power of the motor
     * @return power
     */
    public double getPower() { return motor.getPower(); }

    /**
     * Has the motor reached the target
     * @return if the motor is busy
     */
    public boolean hasReachedPosition(){
        return !motor.isBusy();
    }

    /**
     * Get the position of the motor
     * @return ticks
     */
    public double getPosition(){
        return motorEncoder.getPos();
    }

    /**
     * Gets the logical direction of the pmotor
     * @return direction
     */
    public DcMotorSimple.Direction getDirection(){return direction;}

    /**
     * Stop and reset the mode of the pmotor
     */
    public void stopAndReset(){
        setPower(0);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Reset the position of the pmotor
     */
    public void resetPosition(){
        motorEncoder.reset();
    }

    public void useStallDetector(double maxPower, double powerOffset, double minSpeed, double minTime, double disableTime){
        detector.init(maxPower, powerOffset, minSpeed, minTime, disableTime);
    }

    public double getStallDerivative(){
        return detector.getCurrentDerivative();
    }

    /**
     * Sets the power of the motor to 0
     * NOTE: This should only be called in a thread that has access to use the robot
     */
    @Override
    public void halt(){ setPower(0); }
}

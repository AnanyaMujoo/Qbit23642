package robotparts.electronics.positional;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import autoutil.controllers.PositionHolder;
import debugging.StallDetector;
import robotparts.Electronic;
import robotparts.electronics.input.IEncoder;
import util.codeseg.ReturnParameterCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Precision;

import static global.General.fault;
import static java.lang.Math.*;

public class PMotor extends Electronic {
    /**
     * DcMotor object and related parameters
     */
    private final DcMotor motor;
    private final DcMotorSimple.Direction direction;
    private final DcMotor.ZeroPowerBehavior zeroPowerBehavior;
    private final IEncoder motorEncoder;
    private final StallDetector detector;
    private MovementType movementType = MovementType.ROTATIONAL;
    private ReturnParameterCodeSeg<Double, Double> outputToTicks = input -> input;
    private ReturnParameterCodeSeg<Double, Double> ticksToOutput = input -> input;
    private PositionHolder positionHolder = new PositionHolder(0, 0, 0,0);

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

    public void setToLinear(double ticksPerRev, double radius, double ratio, double angle){
        movementType = MovementType.LINEAR;
        outputToTicks = distance -> (distance/(2*Math.PI*radius))*ticksPerRev*(ratio/cos(toRadians(angle)));
        ticksToOutput = Precision.invert(outputToTicks);
    }

    public void setToRotational(double ticksPerRev, double ratio){
        movementType = MovementType.ROTATIONAL;
        outputToTicks = distance -> (distance/360)*ticksPerRev*ratio;
        ticksToOutput = Precision.invert(outputToTicks);
    }

    public void setPositionHolder(PositionHolder holder){
        positionHolder = holder;
        positionHolder.setProcessVariable(this::getPosition);
    }

    /**
     * Set the power of the pmotor
     * @param p
     */
    @Override
    public final void move(double p){
        if(access.isAllowed()){
            if(!detector.isStalling()){
                motor.setPower(p);
            }else{
                motor.setPower(0);
                fault.warn("Motor is stalling", Expectation.EXPECTED, Magnitude.CRITICAL);
            }
        }
    }

    public void setPowerAdjusted(double p){
        positionHolder.update();
        motor.setPower(positionHolder.getOutput() + p);
    }

    /**
     * Set the position to move to
     * @param distance
     */
    @Override
    public final void setTarget(double distance){
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setTargetPosition(outputToTicks.run(distance).intValue());
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Get the target position
     * @return target
     */
    public double getTarget() { return ticksToOutput.run((double) motor.getTargetPosition()); }

    /**
     * Get the power of the motor
     * @return power
     */
    public double getPower() { return motor.getPower(); }

    /**
     * Has the motor reached the target
     * @return if the motor is busy
     */
    @Override
    public boolean exitTarget(){
        return !motor.isBusy();
    }

    /**
     * Get the position of the motor
     * @return ticks
     */
    public double getPosition(){ return ticksToOutput.run(motorEncoder.getPos()); }

    /**
     * Gets the logical direction of the pmotor
     * @return direction
     */
    public DcMotorSimple.Direction getDirection(){return direction;}

    /**
     * Stop and reset the mode of the pmotor
     */
    @Override
    public void stopTarget(){
        move(0);
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
    public void halt(){ move(0); }


    /**
     * Type of movement preformed
     */
    public enum MovementType{
        ROTATIONAL,
        LINEAR
    }
}

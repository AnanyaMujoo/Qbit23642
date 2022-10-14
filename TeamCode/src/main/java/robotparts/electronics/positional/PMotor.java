package robotparts.electronics.positional;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import autoutil.controllers.control1D.PositionHolder;
import debugging.StallDetector;
import robotparts.Electronic;
import robotparts.electronics.input.IEncoder;
import util.codeseg.ReturnParameterCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Precision;

import static global.General.bot;
import static global.General.fault;
import static global.General.log;
import static java.lang.Math.*;

public class PMotor extends Electronic {
    /**
     * DcMotor object and related parameters
     */
    private final DcMotorEx motor;
    private final DcMotorSimple.Direction direction;
    private final DcMotor.ZeroPowerBehavior zeroPowerBehavior;
    private final IEncoder motorEncoder;
    private final StallDetector detector;
    private final PIDFCoefficients defaultCoeffs;
    private final PositionHolder positionHolder = new PositionHolder(0);
    private MovementType movementType = MovementType.ROTATIONAL;
    private ReturnParameterCodeSeg<Double, Double> outputToTicks = input -> input;
    private ReturnParameterCodeSeg<Double, Double> ticksToOutput = input -> input;
    private PIDFCoefficients currentCoeffs;

    /**
     * Constructor to create a pmotor
     * @param m
     * @param dir
     * @param zpb
     * @param mode
     */
    public PMotor(DcMotor m, DcMotor.Direction dir, DcMotor.ZeroPowerBehavior zpb, DcMotor.RunMode mode){
        motor = (DcMotorEx) m;
        motorEncoder = new IEncoder(motor, IEncoder.EncoderType.PMOTOR);
        detector = new StallDetector(motorEncoder, 10, 10);
        positionHolder.setProcessVariable(motorEncoder::getAngularVelocity);
        defaultCoeffs = motor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
        currentCoeffs = defaultCoeffs;
        direction = dir;
        zeroPowerBehavior = zpb;

        motor.setDirection(direction);
        motor.setZeroPowerBehavior(zeroPowerBehavior);
        motor.setMode(mode);

        motor.setPower(0);

        motorEncoder.reset();
    }


    public void setRestPower(double restPower){ positionHolder.setRestOutput(restPower); }

    public void holdPosition(){ positionHolder.activate(); move(0); }

    public void releasePosition(){ positionHolder.deactivate(); }

    public PositionHolder getPositionHolder(){ return positionHolder; }



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

    public void scalePIDFCoefficients(double ps, double is, double ds, double fs){ currentCoeffs = new PIDFCoefficients(defaultCoeffs.p*ps,defaultCoeffs.i*is,defaultCoeffs.d*ds,defaultCoeffs.f*fs); motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, currentCoeffs); }
    public void scalePIDCoefficients(double ps, double is, double ds){ scalePIDFCoefficients(ps, is, ds, 1);}
    public void setPIDFCoefficients(double p, double i, double d, double f){ currentCoeffs = new PIDFCoefficients(p, i, d, f); motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, currentCoeffs); }
    public void setPIDCoefficients(double p, double i, double d){ setPIDFCoefficients(p, i, d, defaultCoeffs.f); }

    /**
     * default: 10 3 0 0
     * @return coeffs
     */
    public PIDFCoefficients getDefaultPIDFCoefficients(){ return defaultCoeffs; }
    public PIDFCoefficients getCurrentPIDFCoefficients(){ return currentCoeffs; }


    /**
     * Set the power of the pmotor
     * @param p
     */
    @Override
    public final void move(double p){
        if(access.isAllowed()){
            if(!detector.isStalling()){
                positionHolder.update();
                motor.setPower(positionHolder.getOutput() + p);
            }else{
                motor.setPower(0);
                fault.warn("Motor is stalling, stopped all AutoModules", Expectation.EXPECTED, Magnitude.CRITICAL);
                bot.cancelAutoModules();
            }
        }
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
     * @return if the motor is not busy (done) or is stalling (prevent damage)
     */
    @Override
    public boolean exitTarget(){ return !motor.isBusy() || detector.isStalling(); }

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

    public StallDetector getStallDetector(){
        return detector;
    }

    public IEncoder getMotorEncoder(){ return motorEncoder; }

    public MovementType getMovementType(){
        return movementType;
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

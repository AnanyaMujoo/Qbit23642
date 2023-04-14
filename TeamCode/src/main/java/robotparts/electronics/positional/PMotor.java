package robotparts.electronics.positional;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import autoutil.controllers.control1D.PositionHolder;
import debugging.StallDetector;
import robotparts.Electronic;
import robotparts.electronics.input.IEncoder;
import util.codeseg.ReturnCodeSeg;
import util.codeseg.ReturnParameterCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Precision;

import static global.General.bot;
import static global.General.fault;
import static global.General.log;
import static global.General.voltageScale;
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
    private final PositionHolder positionHolder = new PositionHolder();
    private MovementType movementType = MovementType.ROTATIONAL;
    private ReturnParameterCodeSeg<Double, Double> outputToTicks = input -> input;
    private ReturnParameterCodeSeg<Double, Double> ticksToOutput = input -> input;
    private PIDFCoefficients currentCoeffs;
    private static final double exitTimeDelay = 0.1;
    private boolean holdingExact = false;

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
        detector = new StallDetector(motorEncoder, 10, 13);
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
        holdingExact = false;
    }

    // TOD 5 Make way for custom PID and custom rest pow function (feedforward)


    /**
     * Use the position holder with parameters
     * @param restPower
     */
    public void usePositionHolder(double restPower){ positionHolder.setRestOutput(restPower); resetPosition(); }
    public void usePositionHolder(double restPower, double pCoefficient){ positionHolder.setPCoefficient(pCoefficient); usePositionHolder(restPower); }
    public void usePositionHolder(ReturnCodeSeg<Double> restPowerFunction, double pCoefficient){ positionHolder.setRestPowerFunction(restPowerFunction); positionHolder.setPCoefficient(pCoefficient); resetPosition();}

    /**
     * Hold position by activating the position holder
     */
    public void holdPosition(){ positionHolder.activate(); move(0); }

    /**
     * Hold the position exactly
     */
    public void holdPositionExact(){ positionHolder.activate(this::getPosition); move(0); }

    /**
     * Release the position
     */
    public void releasePosition(){ positionHolder.deactivate(); }

    /**
     * Get the position holder object
     * @return position holder
     */
    public PositionHolder getPositionHolder(){ return positionHolder; }

    /**
     * Set the motor to linear mode
     * @param ticksPerRev
     * @param radius
     * @param ratio
     * @param angle
     */
    public void setToLinear(double ticksPerRev, double radius, double ratio, double angle){
        movementType = MovementType.LINEAR;
        outputToTicks = distance -> (distance/(2*Math.PI*radius))*ticksPerRev*(ratio/cos(toRadians(angle)));
        ticksToOutput = Precision.invert(outputToTicks);
    }

    /**
     * Set the motor to rotational mode
     * @param ticksPerRev
     * @param ratio
     */
    public void setToRotational(double ticksPerRev, double ratio){
        movementType = MovementType.ROTATIONAL;
        outputToTicks = distance -> (distance/360)*ticksPerRev*ratio;
        ticksToOutput = Precision.invert(outputToTicks);
    }

    // TOD5 FIX THIS
    @Deprecated
    public void scalePIDFCoefficients(double ps, double is, double ds, double fs){ currentCoeffs = new PIDFCoefficients(defaultCoeffs.p*ps,defaultCoeffs.i*is,defaultCoeffs.d*ds,defaultCoeffs.f*fs); motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, currentCoeffs); }
    @Deprecated
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
     * NOTE: Use halt to stop the motors (otherwise will be set with restPower)
     * @param p
     */
    @Override
    public final void move(double p){
        if(access.isAllowed()){
            if(!detector.isStalling()){
                positionHolder.update();
                motor.setPower(Precision.clip(positionHolder.getOutput() + p, 1)*voltageScale);
            }else{
                motor.setPower(0);
                fault.warn("Motor is stalling, stopped all AutoModules", Expectation.EXPECTED, Magnitude.CRITICAL);
                bot.cancelAutoModules();
            }
        }
    }

    /**
     * Sets the power of motor without access checking, stall detection, or restPower
     * @param power
     */
    public void setPowerRaw(double power){ motor.setPower(Precision.clip(power, 1)); }

    /**
     * Set the position to move to
     * @param distance
     */
    @Override
    public final void setTarget(double distance){
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setTargetPosition(outputToTicks.run(distance).intValue());
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPositionHolderTarget(distance);
    }

    public final void setPositionHolderTarget(double target){ positionHolder.setTarget(target); }

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
    public boolean exitTarget(){
        return (!motor.isBusy() || detector.isStalling()) && bot.rfsHandler.getTimer().seconds() > exitTimeDelay;
    }

    /**
     * Get the position of the motor
     * @return output
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
        halt();
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        holdingExact = true;
    }

    /**
     * Reset the position of the pmotor
     */
    public void resetPosition(){ motorEncoder.reset(); }

    /**
     * Get the stall detector
     * @return stall detector
     */
    public StallDetector getStallDetector(){ return detector; }

    /**
     * Get the motor encoder
     * @return motor encoder
     */
    public IEncoder getMotorEncoder(){ return motorEncoder; }

    /**
     * Get the type of movement
     * @return movementType
     */
    public MovementType getMovementType(){ return movementType; }

    /**
     * Default move with position holder
     * @param p
     * @param cutOffPosition
     * @param backPower
     */
    public void moveWithPositionHolder(double p, double cutOffPosition, double backPower) {
        if (p != 0) {
            releasePosition(); move(p); holdingExact = false;
        } else if (getPosition() > cutOffPosition) {
            if (holdingExact) { holdPositionExact(); } else { holdPosition(); }
        } else {
            releasePosition();
            move(-Math.abs(backPower));
        }
    }

    /**
     * Sets the power of the motor to 0
     * NOTE: This should only be called in a thread that has access to use the robot
     */
    @Override
    public void halt(){ setPowerRaw(0); }

    /**
     * Type of movement preformed
     */
    public enum MovementType{
        ROTATIONAL,
        LINEAR
    }


    public void softReset(){ motorEncoder.softReset(); }
}

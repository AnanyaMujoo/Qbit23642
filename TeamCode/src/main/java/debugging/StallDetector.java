package debugging;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import util.template.Precision;
import autoutil.profilers.Profiler;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class StallDetector implements Precision {
    // TODO 4 FIX Stall Detector
    private Profiler profiler;
    private final DcMotorEx motor;
    private double maxPower;
    private double minSpeed;
    private double minTime;
    private double offset;
    private double disableTime;
    private boolean hasBeenInitialized = false;

    private double maxCurrent = 0;

    public StallDetector(DcMotor m){
        motor = (DcMotorEx) m;
//        motor.getVelocity(AngleUnit.DEGREES);
//        motor.getCurrent(CurrentUnit.MILLIAMPS);

//        profiler = new Profiler(() -> (double) motor.getCurrentPosition());
        resetPrecisionTimers();
    }


    public void init(double maxPower, double powerOffset, double minSpeed, double minTime, double disableTime){
        this.maxPower = maxPower;
        this.minSpeed = minSpeed;
        this.minTime = minTime;
        this.offset = powerOffset;
        this.disableTime = disableTime;
        hasBeenInitialized = true;
    }

    /**
     * @param minSpeedThresh (deg/s) ~5
     * @param maxCurrentThresh (amps) ~8
     */
    public void init(double minSpeedThresh, double maxCurrentThresh){
        minSpeed = minSpeedThresh;
        maxCurrent = maxCurrentThresh;
    }

    public double getMotorSpeed(){
        return abs(motor.getVelocity(AngleUnit.DEGREES));
    }

    public double getMotorCurrent(){
        return abs(motor.getCurrent(CurrentUnit.AMPS));
    }

    public boolean isMotorVelocityLow(){
        return getMotorSpeed() < minSpeed;
    }

    public boolean isMotorCurrentHigh(){
        return getMotorCurrent() > maxCurrent;
    }

    public double getMotorPower(){
        return Math.pow(getMotorCurrent(),2);
    }


    public boolean isStalling() {
        return inputOutputTrueForTime(isMotorVelocityLow()&&isMotorCurrentHigh(), 1,1);

//        if(hasBeenInitialized) {
//            profiler.update();
//            boolean isStalling = isInputTrueForTime(Math.abs(motor.getPower() - offset) > maxPower && Math.abs(profiler.getDerivative()) < minSpeed, minTime);
//            return outputTrueForTime(isStalling, disableTime);
//        }
//        return false;
    }

    public double getCurrentDerivative(){
        return profiler.getDerivative();
    }
}

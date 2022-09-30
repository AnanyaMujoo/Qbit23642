package debugging;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import util.template.Precision;
import autoutil.profilers.Profiler;

public class StallDetector implements Precision {
    // TODO 4 FIX Stall Detector
    private final Profiler profiler;
    private final DcMotorEx motor;
    private double maxPower;
    private double minSpeed;
    private double minTime;
    private double offset;
    private double disableTime;
    private boolean hasBeenInitialized = false;

    public StallDetector(DcMotor m){
        motor = (DcMotorEx) m;
//        motor.getVelocity(AngleUnit.DEGREES);
//        motor.getCurrent(CurrentUnit.MILLIAMPS);

        profiler = new Profiler(() -> (double) motor.getCurrentPosition());
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


    public boolean isStalling() {
        if(hasBeenInitialized) {
            profiler.update();
            boolean isStalling = isInputTrueForTime(Math.abs(motor.getPower() - offset) > maxPower && Math.abs(profiler.getDerivative()) < minSpeed, minTime);
            return outputTrueForTime(isStalling, disableTime);
        }
        return false;
    }

    public double getCurrentDerivative(){
        return profiler.getDerivative();
    }
}

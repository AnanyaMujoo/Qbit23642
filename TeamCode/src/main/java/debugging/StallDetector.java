package debugging;

import com.qualcomm.robotcore.hardware.DcMotor;

import util.template.Precision;
import autoutil.profilers.Profiler;

public class StallDetector implements Precision {
    private final Profiler profiler;
    private final DcMotor motor;
    private double maxPower;
    private double minSpeed;
    private double minTime;
    private double offset;
    private double disableTime;
    private boolean hasBeenInitialized = false;

    public StallDetector(DcMotor m){
        motor = m;
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

package debugging;

import robotparts.electronics.input.IEncoder;
import util.template.Precision;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class StallDetector {

    private final Precision precision = new Precision();

    private final IEncoder encoder;
    private double minSpeed;
    private double maxCurrent;
    public final double stallTime = 0.1;

    /**
     * @param minSpeedThresh (deg/s) ~10
     * @param maxCurrentThresh (amps) ~8
     */
    public StallDetector(IEncoder e, double minSpeedThresh, double maxCurrentThresh){
        encoder = e;
        minSpeed = minSpeedThresh;
        maxCurrent = maxCurrentThresh;
    }

    public void setCustomThresholds(double minSpeedThresh, double maxCurrentThresh){
        minSpeed = minSpeedThresh;
        maxCurrent = maxCurrentThresh;
    }

    public double getMotorSpeed(){ return abs(Math.toDegrees(encoder.getAngularVelocity())); }

    public double getMotorCurrent(){
        return abs(encoder.getCurrent());
    }

    public boolean isMotorVelocityLow(){
        return getMotorSpeed() < minSpeed;
    }

    public boolean isMotorCurrentHigh(){
        return getMotorCurrent() > maxCurrent;
    }

    public boolean isStalling() {
        return precision.outputTrueForTime(isMotorVelocityLow() && precision.isInputTrueForTime(isMotorCurrentHigh(), stallTime), 1);
    }
}

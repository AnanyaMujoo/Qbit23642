package robotparts.electronics.input;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import robotparts.Electronic;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Precision;

import static global.General.fault;

/**
 * NOTE: Uncommented
 */

public class IDistance extends Electronic {
    private final DistanceSensor distanceSensor;
    private double oldDistance = 0;
    private boolean ready = false;
    private static final double maxDeltaDistance = 20;

    public IDistance(DistanceSensor distanceSensor) {
        this.distanceSensor = distanceSensor;
    }

    public void ready(){ ready = true; }

    public double getDistance(){
        double currentDistance = distanceSensor.getDistance(DistanceUnit.CM);
        if(Double.isFinite(currentDistance) && currentDistance != 0) {
            if (ready || Precision.difference(currentDistance, oldDistance, maxDeltaDistance)) {
                oldDistance = currentDistance;
                ready = false;
                return currentDistance;
            } else {
                return oldDistance;
            }
        }else{
            fault.warn("Distance Sensor Reading Infinity Or Incorrect Value", Expectation.EXPECTED, Magnitude.MODERATE);
            return oldDistance;
        }
    }
}

package robotparts.electronics.input;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import robotparts.Electronic;
import util.template.Precision;

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
        if(ready || Precision.difference(currentDistance, oldDistance, maxDeltaDistance)){
            oldDistance = currentDistance;
            ready = false;
            return currentDistance;
        }else{
            return oldDistance;
        }
    }
}

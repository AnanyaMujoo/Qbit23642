package robotparts.electronics.input;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import robotparts.Electronic;

/**
 * NOTE: Uncommented
 */

public class IDistance extends Electronic {
    private final DistanceSensor distanceSensor;

    public IDistance(DistanceSensor distanceSensor) {
        this.distanceSensor = distanceSensor;
    }

    public double getDistance(){
        return distanceSensor.getDistance(DistanceUnit.CM);
    }
}

package robotparts.electronics.input;

import static global.General.hardwareMap;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import robotparts.Electronic;

/**
 * NOTE: Uncommented
 */

public class IColor extends Electronic {

    private final ColorRangeSensor colorSensor;
    public IColor(ColorRangeSensor colorSensor){
        this.colorSensor = colorSensor;
    }

    public int getRed(){
        return colorSensor.red();
    }
    public int getGreen(){
        return colorSensor.green();
    }
    public int getBlue(){
        return colorSensor.blue();
    }
    public double getDistance() { return colorSensor.getDistance(DistanceUnit.CM); }

}

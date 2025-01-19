package robotparts.electronics.input;

import static global.General.hardwareMap;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

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


    public void setGain(float gain){
        colorSensor.setGain(gain);
    }


    public double[] getColorRatios(){
//        double light = colorSensor.getLightDetected();
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        float r = colors.red;
        float g = colors.green;
        float b = colors.blue;
        return new double[]{r/g, b/r};
    }

//    public double[] redtolightratio(){
//        double light = colorSensor.getLightDetected();
//        NormalizedRGBA colors = colorSensor.getNormalizedColors();
//        float r = colors.red;
//        float g = colors.green;
//        return new double[]{r/light, g/light};
//
//
//    }


//    public float[] getNormRGB(){
//        NormalizedRGBA out = colorSensor.getNormalizedColors();
//        return new float[]{out.red, out.green, out.blue};
//    }
//
//    public float[] getNormRGB2(){
//        float r = colorSensor.red();
//        float g = colorSensor.green();
//        float b = colorSensor.blue();
//        float norm = (r+g+b)/100;
//        return new float[]{r/norm, g/norm, b/norm};
//    }

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
    public void enableLED(boolean enable){
        colorSensor.enableLed(enable);
    }

}

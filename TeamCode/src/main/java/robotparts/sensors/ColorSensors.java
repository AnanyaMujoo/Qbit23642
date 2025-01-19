package robotparts.sensors;

import static global.General.fieldSide;

import elements.FieldSide;
import elements.SampleColor;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IColor;


public class ColorSensors extends RobotPart {
    /**
     * Color sensor input for intake
     */
    private IColor colorSensor;
    private float GAIN = 2;


    @Override
    public void init() {
        colorSensor = create("colsense", ElectronicType.ICOLOR);
        colorSensor.setGain(GAIN);
    }
    public double getDistance(){
        return colorSensor.getDistance();
    }

    public void enableLED(boolean enable){
        colorSensor.enableLED(enable);
    }

    public SampleColor getSampleColor(){
        double dist = getDistance();
        double[] ratios = colorSensor.getColorRatios();
        double rg = ratios[0];
        double br = ratios[1];
//        double rl = ratios[2];


        if(br > 1.5){
            return SampleColor.BLUE;
        }else{
            if(rg > 0.74 && dist < 6){
                return SampleColor.RED;
            }else if(rg > 0.63){
//                if(rl > 0.037)
                if(dist > 6 && br > 0.98) {
                    return SampleColor.RED;
                }else{
                    return SampleColor.YELLOW;
                }
            }else{
                return SampleColor.NONE;
            }
        }
//
//
//
//        if(rg > 0.6){
//            return SampleColor.YELLOW;
//        }

//        if(getHue() > 165){
//            return SampleColor.BLUE;
//        }else if(getHue() < 135){
//            return SampleColor.YELLOW;
//        }else if (getHue() < 40) {
//            return SampleColor.RED;
//        }else {
//            return SampleColor.NONE;
//        }
    }

    public boolean isSampleLoaded() {
        return getDistance() < 6 || !getSampleColor().equals(SampleColor.NONE);
    }

    public float[] getOuttakeColorHSV(){
        float[] color = new float[3];
        android.graphics.Color.RGBToHSV(colorSensor.getRed(), colorSensor.getGreen(), colorSensor.getBlue(), color);
        return color;
    }

    public double[] getOuttakeColorRGB(){
        return colorSensor.getColorRatios();
//        return colorSensor.getNormRGB2();
//        return new float[]{colorSensor.getRed(), colorSensor.getGreen(), colorSensor.getBlue()};
    }

//    public double[] redtolight(){
//        return colorSensor.redtolightratio();
//    }



    public double getHue(){
        float[] color = getOuttakeColorHSV();
        return color[0];
    }


    public int determineCodeFromColor(SampleColor color){
        if(color.equals(SampleColor.NONE)){
            return 0;
        }
        if(fieldSide.equals(FieldSide.RED)){
            if(color.equals(SampleColor.YELLOW) || color.equals(SampleColor.RED)){
                return 2;
            }else{
                return 1;
            }
        }else if(fieldSide.equals(FieldSide.BLUE)){
            if(color.equals(SampleColor.YELLOW) || color.equals(SampleColor.BLUE)){
                return 2;
            }else{
                return 1;
            }
//            if(color.equals(SampleColor.BLUE)){
//                return 2;
//            }else if(color.equals(SampleColor.RED)){
//                return 1;
//            }else{
//                return 0;
//            }
        }else{
            return 0;
        }
    }

}

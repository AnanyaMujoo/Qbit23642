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


    @Override
    public void init() {
        colorSensor = create("colsense", ElectronicType.ICOLOR);
    }
    public double getDistance(){
        return colorSensor.getDistance();
    }

    public SampleColor getSampleColor(){
        if(getHue() > 165){
            return SampleColor.BLUE;
        }else if(getHue() < 135){
            return SampleColor.YELLOW;
        }else if (getHue() < 40) {
            return SampleColor.RED;
        }else {
            return SampleColor.NONE;
        }
    }

    public boolean isSampleLoaded() {
        return getDistance() < 6 || !getSampleColor().equals(SampleColor.NONE);
    }

    public float[] getOuttakeColorHSV(){
        float[] color = new float[3];
        android.graphics.Color.RGBToHSV(colorSensor.getRed(), colorSensor.getGreen(), colorSensor.getBlue(), color);
        return color;
    }
    public double getHue(){
        float[] color = getOuttakeColorHSV();
        return color[0];
    }


    public int correctColor(SampleColor color){
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
            if(color.equals(SampleColor.BLUE)){
                return 2;
            }else if(color.equals(SampleColor.RED)){
                return 1;
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }

}

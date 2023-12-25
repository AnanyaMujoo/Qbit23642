package robotparts.sensors;

import java.util.ArrayList;

import automodules.stage.Exit;
import elements.GameElement;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IColor;


public class ColorSensors extends RobotPart {
    /**
     * Color sensor input for intake
     */
    private IColor cso;

    // TODO4 NEW
    // Make more methods
    private final ArrayList<Double> runningAvg = new ArrayList<>();
    private final int runningAvgSize = 3;
    private final ArrayList<Double> runningAvg2 = new ArrayList<>();

    @Override
    public void init() {
        cso = create("cso", ElectronicType.ICOLOR);
    }

    /**
     * Get the color that is sensed in HSV format
     * @return hsv color
     */
    public float[] getOuttakeColorHSV(){
        float[] color = new float[3];
        android.graphics.Color.RGBToHSV(cso.getRed(), cso.getGreen(), cso.getBlue(), color);
        return color;
    }

    /**
     * Get the type of freight being sensed
     * @return freight type
     */
    public GameElement getFreightType(){
        double h = getOuttakeColorHSV()[0];
        double v = getOuttakeColorHSV()[2];
        runningAvg.add(h);
        runningAvg2.add(v);
        if(runningAvg.size() == runningAvgSize){
            h = (runningAvg.get(0) + runningAvg.get(1) + runningAvg.get(2))/3.0;
            runningAvg.remove(0);
        }
        if(runningAvg2.size() == runningAvgSize){
            v = (runningAvg2.get(0) + runningAvg2.get(1) + runningAvg2.get(2))/3.0;
            runningAvg2.remove(0);
        }
        if(130 < h && h < 200 && (v > 1.2)){
            return GameElement.BALL;
        }else if(40 < h && h < 100 && (v > 1.2)){
            return GameElement.CUBE;
        }else{
            return GameElement.NONE;
        }
    }

    /**
     * Is a ball being sensed?
     * @return is ball
     */
    public boolean isBall(){
        return getFreightType().equals(GameElement.BALL);
    }

    /**
     * Is a cube being sensed?
     * @return is cube
     */
    public boolean isCube(){
        return getFreightType().equals(GameElement.CUBE);
    }

    /**
     * Is there a freight at all?
     * @return is freight
     */
    public boolean isFreight(){
        GameElement element = getFreightType();
        boolean hasFreightNear = element.equals(GameElement.BALL) || element.equals(GameElement.CUBE);
        return hasFreightNear && cso.getDistance() < 4;
    }


    public Exit exitFreight(){return new Exit(this::isFreight);}

}

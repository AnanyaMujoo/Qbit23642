package robotparts.sensors;

import automodules.stage.Exit;
import elements.GameElement;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IColor;


public class ColorSensors extends RobotPart {
    /**
     * Color sensor input for intake
     */
    private IColor leftColorSensor;
    private IColor rightColorSensor;


    // TODO4 NEW
    // Make more methods
//    private final ArrayList<Double> runningAvg = new ArrayList<>();
//    private final int runningAvgSize = 3;
//    private final ArrayList<Double> runningAvg2 = new ArrayList<>();

    @Override
    public void init() {
        leftColorSensor = create("leftcolsense", ElectronicType.ICOLOR);
        rightColorSensor = create("rightcolsense", ElectronicType.ICOLOR);
    }
    public double leftPixelDistance(){return leftColorSensor.getDistance(); }
    public double rightPixelDistance(){return rightColorSensor.getDistance(); }

public boolean arePixelsLoaded() {
    double leftPixDistance = leftPixelDistance();
    double rightPixDistance = rightPixelDistance();
//    if ((leftPixDistance < 1) && (rightPixDistance < 1)) {
//        return true;
//    }
//    return false;
    return ((leftPixDistance < 1) && (rightPixDistance < 1));
}

//    /**
//     * Get the color that is sensed in HSV format
//     * @return hsv color
//     */
//    public float[] getOuttakeColorHSV(){
//        float[] color = new float[3];
//        android.graphics.Color.RGBToHSV(cso.getRed(), cso.getGreen(), cso.getBlue(), color);
//        return color;
//    }
//
//    /**
//     * Get the type of freight being sensed
//     * @return freight type
//     */
//    public GameElement getFreightType(){
//        double h = getOuttakeColorHSV()[0];
//        double v = getOuttakeColorHSV()[2];
//        runningAvg.add(h);
//        runningAvg2.add(v);
//        if(runningAvg.size() == runningAvgSize){
//            h = (runningAvg.get(0) + runningAvg.get(1) + runningAvg.get(2))/3.0;
//            runningAvg.remove(0);
//        }
//        if(runningAvg2.size() == runningAvgSize){
//            v = (runningAvg2.get(0) + runningAvg2.get(1) + runningAvg2.get(2))/3.0;
//            runningAvg2.remove(0);
//        }
//        if(130 < h && h < 200 && (v > 1.2)){
//            return GameElement.BALL;
//        }else if(40 < h && h < 100 && (v > 1.2)){
//            return GameElement.CUBE;
//        }else{
//            return GameElement.NONE;
//        }
//    }
//
//    /**
//     * Is a ball being sensed?
//     * @return is ball
//     */
//    public boolean isBall(){
//        return getFreightType().equals(GameElement.BALL);
//    }

//    /**
//     * Is a cube being sensed?
//     * @return is cube
//     */
//    public boolean isCube(){
//        return getFreightType().equals(GameElement.CUBE);
//    }
//
//    /**
//     * Is there a freight at all?
//     * @return is freight
//     */
//    public boolean isFreight(){
//        GameElement element = getFreightType();
//        boolean hasFreightNear = element.equals(GameElement.BALL) || element.equals(GameElement.CUBE);
//        return hasFreightNear && cso.getDistance() < 4;
//    }
//
//
//    public Exit exitFreight(){return new Exit(this::isFreight);}

}

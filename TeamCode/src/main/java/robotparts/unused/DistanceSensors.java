package robotparts.unused;

import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IDistance;


public class DistanceSensors extends RobotPart {
    /**
     * Distance sensors,
     * left, right, front left, front right, and outtake
     */
    private IDistance dr, df;

    // TOD4 DISTANCE SENSORS (KALMAN FILTER)
    // TOD4 VUFORIA
    // TOD4 LEDS

    @Override
    public void init() {
        df = create("df", ElectronicType.IDISTANCE);
        dr = create("dr", ElectronicType.IDISTANCE);
    }

    /**
     * Get distances
     * @return distances
     */
    public double getFrontDistance(){ return df.getDistance()*1.061224; }
    public double getRightDistance(){ return dr.getDistance()*1.061224; }

    public void ready(){
        df.ready(); dr.ready();
    }

//    public double getLeftDistance(){
//        return dsl.getDistance();
//    }
//    public double getRightDistance(){
//        return dsr.getDistance();
//    }
//    public double getFrontLeftDistance(){
//        return dsfl.getDistance();
//    }
//    public double getFrontRightDistance(){
//        return dsfr.getDistance();
//    }
//    public double getOuttakeDistance(){
//        return dso.getDistance();
//    }
}

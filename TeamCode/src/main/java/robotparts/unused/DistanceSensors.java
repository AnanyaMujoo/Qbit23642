package robotparts.unused;

import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.IDistance;


public class DistanceSensors extends RobotPart {
    /**
     * Distance sensors,
     * left, right, front left, front right, and outtake
     */
//    private IDistance dsl,dsr,dsfl,dsfr,dso;
    private IDistance dr;
//            , df;

    // TOD4 DISTANCE SENSORS (KALMAN FILTER)
    // TOD4 VUFORIA
    // TOD4 LEDS

    @Override
    public void init() {
//        df = create("df", ElectronicType.IDISTANCE);
        dr = create("dr", ElectronicType.IDISTANCE);
//        dsl = createDistanceSensor("dsl");
//        dsr = createDistanceSensor("dsr");
//        dsfl = createDistanceSensor("dsfl");
//        dsfr = createDistanceSensor("dsfr");
//        dso = createDistanceSensor("cso");
    }

    /**
     * Get distances
     * @return distances
     */
//    public double getFrontDistance(){ return df.getDistance(); }
    public double getRightDistance(){ return dr.getDistance(); }

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

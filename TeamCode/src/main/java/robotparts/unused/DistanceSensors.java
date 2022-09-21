package robotparts.unused;

import robotparts.RobotPart;
import robotparts.electronics.input.IDistance;


public class DistanceSensors extends RobotPart {
    /**
     * Distance sensors,
     * left, right, front left, front right, and outtake
     */
    private IDistance dsl,dsr,dsfl,dsfr,dso;

    @Override
    public void init() {
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
    public double getLeftDistance(){
        return dsl.getDistance();
    }
    public double getRightDistance(){
        return dsr.getDistance();
    }
    public double getFrontLeftDistance(){
        return dsfl.getDistance();
    }
    public double getFrontRightDistance(){
        return dsfr.getDistance();
    }
    public double getOuttakeDistance(){
        return dso.getDistance();
    }
}

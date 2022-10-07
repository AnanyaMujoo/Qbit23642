package autoutil.controllers.control1D;

import autoutil.paths.PathSegment;
import geometry.position.Pose;

public class BangBang extends Controller1D {

    private final double s;
    private double maximumTime;

    public BangBang(double s, double maximumTime, double accuracy){
        this.s = s;
        this.maximumTime = maximumTime;
        this.accuracy = accuracy;
    }

    public void setMaximumTime(double maximumTime){ this.maximumTime = maximumTime; }

    @Override
    protected void updateController(Pose pose, PathSegment pathSegment) {
        if(!isWithinAccuracyRange()) {
            if (getError() > 0) {
                setOutput(s);
            } else {
                setOutput(-s);
            }
        }else{
            if((errorProfiler.getCurrentTime() - currentTime) > maximumTime){
                isAtTarget = true;
            }else{
                setOutput(0);
                currentTime = errorProfiler.getCurrentTime();
            }
        }
    }
}

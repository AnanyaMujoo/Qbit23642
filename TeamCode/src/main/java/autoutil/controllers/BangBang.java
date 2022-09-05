package autoutil.controllers;

import autoutil.paths.PathSegment2;
import geometry.position.Pose;

public class BangBang extends Controller1D {

    private double s;
    private double maximumTime;

    public BangBang(double s, double maximumTime, double accuracy){
        this.s = s;
        this.maximumTime = maximumTime;
        this.accuracy = accuracy;
    }

    public void setMaximumTime(double maximumTime){ this.maximumTime = maximumTime; }

    @Override
    public void update(Pose pose, PathSegment2 pathSegment) {
        updateProfilers();
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

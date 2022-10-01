package autoutil.paths;

import util.Timer;

public class PathPause extends PathSegment {
    private final double time;
    private final Timer timer = new Timer();

    public PathPause(double time){
        this.time = time;
    }

    public void startPausing(){
        timer.reset();
    }

    public boolean isDonePausing(){
        return timer.seconds() > time;
    }
}

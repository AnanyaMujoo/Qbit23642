package autoutil.generators;

import geometry.position.Pose;
import util.Timer;

public class PauseGenerator extends Generator{

    private final double time;
    private final Timer timer = new Timer();

    public PauseGenerator(double time){
        this.time = time;
    }

    public void startPausing(){
        timer.reset();
    }

    public boolean isDonePausing(){
        return timer.seconds() > time;
    }

    @Override
    public void add(Pose start, Pose target) {

    }
}

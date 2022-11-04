package autoutil.generators;

import java.util.ArrayList;
import java.util.Arrays;

import automodules.AutoModule;
import geometry.position.Pose;

public abstract class Generator {

    private Pose start, target;

    public final void addSegment(Pose start, Pose target){ this.start = start; this.target = target; add(start, target); }
    public abstract void add(Pose start, Pose target);


    public Pose getStart(){ return start; }
    public Pose getTarget(){ return target; }

    public void addAutoModule(AutoModule automodule){ path.addSegment(new PathAutoModule(automodule, false)); }
    public void addConcurrentAutoModule(AutoModule automodule){ path.addSegment(new PathAutoModule(automodule, true)); }
    public void addPause(double time){
        path.addSegment(new PathPause(time));
    }
    public void addCancelAutoModule(){
        path.addSegment(new PathAutoModule(true));
    }

    protected ArrayList<Pose> poses = new ArrayList<>();

    public ArrayList<Pose> getPoses(){
        return poses;
    }
}
package autoutil.generators;

import java.util.ArrayList;
import java.util.Arrays;

import automodules.AutoModule;
import automodules.stage.Stage;
import autoutil.reactors.Reactor;
import geometry.position.Pose;

public abstract class Generator {

    private Pose start, target;

    public final void addSegment(Pose start, Pose target){ this.start = start; this.target = target; add(start, target); }
    protected abstract void add(Pose start, Pose target);
    public abstract Stage getStage(Reactor reactor);

    public Pose getStart(){ return start; }
    public Pose getTarget(){ return target; }

}
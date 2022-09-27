package autoutil.generators;

import automodules.AutoModule;
import autoutil.paths.Path;
import autoutil.paths.PathAutoModule;
import autoutil.paths.PathPause;

public abstract class Generator {

    protected Path path = new Path();

    public Path getPath() {
        return path;
    }

    public abstract void add(double x, double y, double heading);

    public void addAutoModule(AutoModule automodule){
        path.addSegment(new PathAutoModule(automodule, false));
    }

    public void addConcurrentAutoModule(AutoModule automodule){
        path.addSegment(new PathAutoModule(automodule, true));
    }

    public void addPause(double time){
        path.addSegment(new PathPause(time));
    }

    public void addCancelAutoModule(){
        path.addSegment(new PathAutoModule(true));
    }
}
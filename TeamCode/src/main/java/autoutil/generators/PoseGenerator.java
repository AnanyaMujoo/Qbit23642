package autoutil.generators;

import automodules.StageList;
import autoutil.paths.PathAutoModule;
import autoutil.paths.PathPose;

public class PoseGenerator extends Generator{
    @Override
    public void add(double x, double y, double heading) {
        path.addSegment(new PathPose(x, y, Math.toRadians(heading)));
    }
}
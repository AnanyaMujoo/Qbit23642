package autoutil.generators;

import automodules.StageList;
import autoutil.paths.PathAutoModule;
import autoutil.paths.PathLine;
import autoutil.paths.PathLine2;
import autoutil.paths.PathPose;
import geometry.position.Point;
import geometry.position.Pose;

public class LineGenerator extends Generator{
    private Pose oldPose;
    public LineGenerator(Pose start){
        oldPose = start;
    }
    public LineGenerator(){
        oldPose = new Pose(new Point(0,0), 0);
    }

    @Override
    public void add(double x, double y, double heading) {
        Pose newPose = new Pose(new Point(x, y), Math.toRadians(heading));
        path.addSegment(new PathLine2(oldPose, newPose));
        oldPose = newPose;
    }
}

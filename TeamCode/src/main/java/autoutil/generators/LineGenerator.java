package autoutil.generators;

import autoutil.paths.PathLine;
import geometry.framework.Point;
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
        path.addSegment(new PathLine(oldPose, newPose));
        oldPose = newPose;
    }
}

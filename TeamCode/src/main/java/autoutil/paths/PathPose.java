package autoutil.paths;

import geometry.framework.Point;
import geometry.position.Pose;

public class PathPose extends PathSegment {
    public PathPose(){}
    public PathPose(double x, double y, double h){
        poses.add(new Pose(new Point(x,y),h));
    }
}
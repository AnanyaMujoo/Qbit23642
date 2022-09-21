package autoutil.paths;

import java.util.ArrayList;

import geometry.position.Point;
import geometry.position.Pose;

public class PathPose extends PathSegment2{
    public PathPose(){}
    public PathPose(double x, double y, double h){
        poses.add(new Pose(new Point(x,y),h));
    }
}
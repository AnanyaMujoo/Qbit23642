package autoutil.paths;

import geometry.position.Line;
import geometry.position.Point;
import geometry.position.Pose;

public class PathLine extends PathPose{

    private final Line line;

    public PathLine(Pose start, Pose end){
        super(end.getX(), end.getY(), end.getAngle());
        line = new Line(start.getPoint(),end.getPoint());
    }

    public Line getLine(){
        return line;
    }

}

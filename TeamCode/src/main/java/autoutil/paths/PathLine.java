package autoutil.paths;

import geometry.position.Line;
import geometry.position.Point;
import geometry.position.Pose;

public class PathLine extends PathPose{

    private final Line line;

    public PathLine(Pose start, Pose end){
        super(end.p.x, end.p.y, end.ang);
        line = new Line(start.p,end.p);
    }

    public Line getLine(){
        return line;
    }

}

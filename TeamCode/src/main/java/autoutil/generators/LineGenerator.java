package autoutil.generators;

import geometry.position.Line;
import geometry.position.Pose;

public class LineGenerator extends PoseGenerator{
    private Line line;

    @Override
    public void add(Pose start, Pose target) {
        line = new Line(start.getPoint().getCopy(), target.getPoint().getCopy());
    }

    public Line getLine(){ return line; }
}

package autoutil.generators;

import geometry.position.Line;
import geometry.position.Pose;

public class LineGenerator extends Generator{
    private Line line;

    @Override
    public void add(Pose start, Pose target) {
        line = new Line(start.getPoint(), target.getPoint());
    }

    public Line getLine(){ return line; }
}

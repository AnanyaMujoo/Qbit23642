package unused.mecanumold.oldauto;

import geometry.position.Line;
import geometry.framework.Point;
import geometry.position.Pose;
import global.Constants;

@Deprecated
public class PathLine extends PathSegment{

    private final Line line;

    public PathLine(Point p1, Point p2) {
        line = new Line(p1, p2);
    }

    @Override
    public void generatePoints(Pose pose) {
        for (double a = 0; a <= 1; a += Constants.LINE_ACC_PATH) {
//            if (Math.abs(Math.atan2(line.my, line.mx) - pose.getAngle()) < Math.PI/4) {
//                points.add(new Pose(getAt(a), Math.atan2(line.my, line.mx)));
//            } else {
//                points.add(new Pose(getAt(a), Math.atan2(-line.my, -line.mx)));
//            }
        }
    }

    public Point getAt(double t) { return line.getAt(t); }

    @Override
    public void flip(boolean x, boolean y) {
//        if (x) {
//            line.p1.x *= -1;
//            line.p2.x *= -1;
//            line.mx *= -1;
//        }
//        if (y) {
//            line.p1.y *= -1;
//            line.p2.y *= -1;
//            line.my *= -1;
//        }
    }
}

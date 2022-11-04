package unused.mecanumold.oldauto;

import java.util.ArrayList;

import geometry.position.Pose;

@Deprecated
public abstract class PathSegment {
    // A unit of a path, not just a geometric object
    // Classes that extend Path segment may contain geometric objects
    public ArrayList<Pose> points = new ArrayList<>();

    public void changePointsForPath(Pose startOrig) {
//        Pose st = new Pose(new Point(startOrig.p.x, startOrig.p.y), startOrig.ang);
//        st.rotate(-Math.PI/2);
//        for (int i = 0; i < points.size(); i++) {
//            Pose newPoint = points.get(i).getRelativeTo(new Pose(new Point(0, 0), -st.ang));
//            newPoint.translate(st.p.x, st.p.y);
//            points.set(i, newPoint);
//        }
    }

    public void generatePoints(Pose pose) {}

    public void flip(boolean x, boolean y) {}
}

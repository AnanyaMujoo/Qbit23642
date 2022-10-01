package unused.mecanumold.oldauto;

import java.util.ArrayList;

import geometry.circles.Circle;
import geometry.position.Point;
import geometry.position.Pose;

import static java.lang.Math.*;

/**
 * Generates the main arcing motion of the robot
 */
@Deprecated
public class ArcGenerator {
    /**
     * Stores all of the positions from the arc
     */
    private final ArrayList<Pose> poses = new ArrayList<>();

//    public void addNewPos(double x, double y, double ang) {
//        addNewPos(new Pose(new Point(x, y), ang));
//    }
//
//    private void addNewPos(Pose p) {
//        p.translate(poses.get(poses.size() - 1).p.x, poses.get(poses.size() - 1).p.y);
//        p.rotate(p.ang);
//        moveTo(p);
//    }

    /**
     * Adds a new point (with more easy-to-use parameters)
     * @param x Absolute x on the field
     * @param y Absolute y on the field
     * @param ang Absolute heading on the field
     */
    public void moveTo(double x, double y, double ang) {
        moveTo(new Pose(new Point(x, y), ang));
    }

    /**
     * Adds a new point
     * @param p Absolute position to move to
     */
    private void moveTo(Pose p) { poses.add(p); }

    /**
     * Marks the path as complete
     * @return completed path
     */
    public FinalPathArc done() {
        FinalPathArc path = new FinalPathArc();
        for (int i = 0; i < poses.size() - 1; i++) {
            path.addSegments(generateSeg(poses.get(i), poses.get(i + 1)));
        }
        return path;
    }

    /**
     * Generates the segments between two points
     * @param p1 first point
     * @param p2 second point
     * @return Segments to go between two points with arcs
     */
    private ArrayList<PathSegment> generateSeg(Pose p1, Pose p2) {
        ArrayList<PathSegment> ret = generateRelSeg(p1, p2);
        p1.rotate(-PI/2);
        if (ret.size() == 3) {
            // Arcs + tangent line
            ret.get(0).generatePoints(new Pose(new Point(0, 0), PI/2));
            ret.get(1).generatePoints(ret.get(0).points.get(ret.get(0).points.size() - 1));
            ret.get(2).generatePoints(ret.get(1).points.get(ret.get(1).points.size() - 1));
        } else {
            // Arcs
            ret.get(0).generatePoints(new Pose(new Point(0, 0), PI/2));
            ret.get(1).generatePoints(ret.get(0).points.get(ret.get(0).points.size() - 1));
        }
        p1.rotate(PI/2);
        for (PathSegment p : ret) {
            p.changePointsForPath(p1);
            for (int i = 0; i < p.points.size(); i++) {
                if (p.points.get(i).ang > Math.PI) {
                    p.points.get(i).ang -= 2 * Math.PI;
                } else if (p.points.get(i).ang < -Math.PI) {
                    p.points.get(i).ang += 2 * Math.PI;
                }
            }
        }
        return ret;
    }

    /**
     * Generates relative segments between two different points
     * @param p1 First position
     * @param p2_orig Second position
     * @return Relative segments between points
     */
    private ArrayList<PathSegment> generateRelSeg(Pose p1, Pose p2_orig) {
        // Final list to return
        ArrayList<PathSegment> ret = new ArrayList<>();

        // Making it relative (for calculation)
        p1.rotate(-PI/2);
        Pose p2 = p2_orig.getRelativeTo(p1);
        p1.rotate(PI/2);

        // Normalizing angle
        p2.ang %= 2 * PI;
        if (p2.ang > PI) { p2.ang -= 2 * PI; }

        p2.ang = abs(p2.ang) != PI/2 ? (signum(p2.p.x * p2.p.y) * p2.ang) : p2.ang;

        if (p2.ang == 0 && signum(p2.p.x * p2.p.y) == -1) { p2.ang = PI; }

        boolean isNegPiBy2 = p2.ang == -PI/2;
        if (isNegPiBy2) p2.ang *= -1;

        // Flipping (for generation)
        boolean flipX = p2.p.x < 0;
        boolean flipY = p2.p.y < 0;

        p2.p.x = Math.abs(p2.p.x);
        p2.p.y = Math.abs(p2.p.y);

        // Core math to generate arc
        double p = p2.ang - PI/2;
        double dx = p2.p.x;
        double dy = p2.p.y;
        double d = sqrt(pow(dx, 2) + pow(dy, 2));
        double s = sin(p);
        double c = cos(p) - 1;
        double a = 2 + c;
        double b = -(c * dx + s * dy);
        double m = -pow(d, 2)/2;
        double rtDiscriminant = sqrt(pow(b, 2) - 4 * a * m);
        double r, r2;
        r = (-b - rtDiscriminant) / (2 * a);
        r2 = (-b + rtDiscriminant) / (2 * a);

        Circle c1;
        Circle c2;

        // choose between the two circles
        if (abs(r) < abs(r2)) {
            c1 = new Circle(new Point(r, 0), abs(r));
            c2 = new Circle(new Point(dx + r * cos(p), dy + r * sin(p)), abs(r));
        } else {
            c1 = new Circle(new Point(r2, 0), abs(r2));
            c2 = new Circle(new Point(dx + r2 * cos(p), dy + r2 * sin(p)), abs(r2));
        }

        // Find point of intersection efficiently
        PathLine betweenCenters = new PathLine(c1.center, c2.center);
        Point pointOfIntersection = betweenCenters.getAt(0.5);

        // Get the shorter arc from each point to the intersection
        PathArc cir1arc = getShorterArc(c1, new Point(0, 0), pointOfIntersection);
        PathArc cir2arc = getShorterArc(c2, p2.p, pointOfIntersection);

        if (isNegPiBy2) p2.ang *= -1;

        if (cir1arc.goingCW(new Pose(new Point(0, 0), PI/2)) == cir2arc.goingCW(p2)) {
            // TAKE TANGENT LINE BETWEEN
            double mx = c2.center.x - c1.center.x;
            double my = c2.center.y - c1.center.y;

            if (mx != 0) {
                // Tangent line is non-vertical

                // Calculate slope of the tangent line
                double mt = my / mx;

                // Calculate the tangent lines
                double temp1 = (c1.r * mt) / sqrt(pow(mt, 2) + 1);

                double x1 = -temp1 + c1.center.x;
                double y1 = sqrt(pow(c1.r, 2) - pow(x1 - c1.center.x, 2)) + c1.center.y;

                double x2 = -temp1 + c2.center.x;
                double y2 = sqrt(pow(c1.r, 2) - pow(x2 - c2.center.x, 2)) + c2.center.y;

                PathLine tangentLine1 = new PathLine(new Point(x1, y1), new Point(x2, y2));

                PathArc a11 = getShorterArc(c1, new Point(0, 0), new Point(x1, y1));
                PathArc a12 = getShorterArc(c2, new Point(x2, y2), p2.p);
                double arcLensPath1 = a11.getArcLength() + a12.getArcLength();

                x1 = temp1 + c1.center.x;
                y1 = -sqrt(pow(c1.r, 2) - pow(x1 - c1.center.x, 2)) + c1.center.y;

                x2 = temp1 + c2.center.x;
                y2 = -sqrt(pow(c1.r, 2) - pow(x2 - c2.center.x, 2)) + c2.center.y;

                PathLine tangentLine2 = new PathLine(new Point(x1, y1), new Point(x2, y2));

                // Finding arcs to go with the tangent lines
                PathArc a21 = getShorterArc(c1, new Point(0, 0), new Point(x1, y1));
                PathArc a22 = getShorterArc(c2, new Point(x2, y2), p2.p);
                double arcLensPath2 = a21.getArcLength() + a22.getArcLength();

                // Find which path would be longer and choosing the shorter
                if (arcLensPath1 < arcLensPath2) {
                    ret.add(a11);
                    ret.add(tangentLine1);
                    ret.add(a12);
                } else {
                    ret.add(a21);
                    ret.add(tangentLine2);
                    ret.add(a22);
                }
            } else {
                // Tangent line is vertical
                ret.add(new PathLine(new Point(0, 0), new Point(0, c2.center.y)));
                ret.add(getShorterArc(c2, new Point(0, c2.center.y), p2.p));
            }
        } else {
            // Simple: add the two arcs
            ret.add(cir1arc);
            ret.add(cir2arc);
        }

        // Flip back
        for (PathSegment pathSegment : ret) {
            pathSegment.flip(flipX, flipY);
        }

        // Return value
        return ret;
    }

    /**
     * Find and return the arc on a circle with less length (shortest-path finder)
     * @param c Circle
     * @param p1 First point on circle
     * @param p2 Second point on circle
     * @return Shorter arc along circle c with p1 and p2
     */
    private PathArc getShorterArc(Circle c, Point p1, Point p2) {
        PathArc arc1 = new PathArc(c, p1, p2, true);
        PathArc arc2 = new PathArc(c, p2, p1, false);
        return arc1.getArcLength() > arc2.getArcLength() ? arc2 : arc1;
    }

    /**
     * Will the generator generate anything?
     * @return Generator empty?
     */
    public boolean empty() { return poses.size() < 2; }
}

package geometry.circles;

import java.util.ArrayList;

import geometry.position.Point;
import geometry.position.Pose;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class CircularArc extends Circle {
    public double arcSt, arcEnd; // radians

    public CircularArc(Point center, Point start, Point end, double r) {
        super(center, r);
        arcSt = getThetaFromPoint(start);
        arcEnd = getThetaFromPoint(end);
    }

    public CircularArc(Point center, double st, double en, double r) {
        super(center, r);
        arcSt = st;
        arcEnd = en;
    }

    public boolean inArc(Point p) {
        return Math.min(arcSt, arcEnd) <= getThetaFromPoint(p) &&
                getThetaFromPoint(p) <= Math.max(arcSt, arcEnd);
    }

    public ArrayList<Pose> getPoints(double angDivide, boolean trueSt, Pose p) {
        ArrayList<Pose> ret = new ArrayList<>();
        double endOfArc = ((arcSt <= arcEnd) ? 0 : (2 * PI)) + arcEnd;
        for (double i = arcSt; i <= endOfArc; i += angDivide) {
            ret.add(getPositionFromTheta(i));
        }
        if (dis(p, ret.get(0)) > dis(p, ret.get(ret.size() - 1))) {
            ArrayList<Pose> ret2 = new ArrayList<>();
            for (int i = ret.size() - 1; i >= 0; i--) {
                ret2.add(ret.get(i));
            }
            ret = ret2;
        }
//        if (angDis(p, ret.get(0)) > 0.5) {
//            for (Pose pose : ret) {
//                pose.ang += PI;
//            }
//        }
        return ret;
    }

    private double angDis(Pose a, Pose b) {
//        if (a.ang > PI/2 && b.ang < PI/2) {
//            return (PI - a.ang) + (PI - b.ang);
//        }
//        return abs(a.ang - b.ang);
        return 0;
    }

    private double dis(Pose a, Pose b) {
//        return sqrt(pow(a.p.x - b.p.x, 2) + pow(a.p.y - b.p.y, 2));
        return 0;
    }

    public boolean goingCW(Pose p) {
//        if (p.p.x > center.x) {
//            if (p.p.y > center.y) {
//                return p.ang == 0 || (p.ang >= 3 * PI / 2 && p.ang <= 2 * PI);
//            } else if (p.p.y < center.y) {
//                return p.ang >= PI && p.ang <= 3 * PI / 2;
//            } else {
//                return p.ang == 3 * PI / 2;
//            }
//        } else if (p.p.x < center.x) {
//            if (p.p.y > center.y) {
//                return p.ang >= 0 && p.ang <= PI / 2;
//            } else if (p.p.y < center.y) {
//                return p.ang >= PI / 2 && p.ang <= PI;
//            } else {
//                return p.ang == PI/2;
//            }
//        } else {
//            if (p.p.y > center.y) {
//                return p.ang == 0;
//            } else {
//                return p.ang == PI;
//            }
//        }
        return true;
    }

    public double getArcLength() {
        double dAng = arcEnd - arcSt;
        dAng = dAng + ((dAng < 0) ? 2 * PI : 0);
        if (abs(dAng) < 0.1) { return 0; }
        return dAng * r;
    }
}

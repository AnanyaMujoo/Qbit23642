package geometry.position;

import geometry.framework.GeometryObject;
import geometry.framework.Point;

/**
 * NOTE: Uncommented
 */

public class Line extends GeometryObject {

    private final Point ps;
    private final Point pe;
    private final double mx;
    private final double my;

    public Line(Point ps, Point pe){
        this.ps = ps; this.pe = pe; mx = pe.getX()-ps.getX(); my = pe.getY()-ps.getY();
        addPoints(ps, pe);
    }

    public Point getStartPoint(){ return ps; }
    public Point getEndPoint(){ return pe; }

    public Point getAt(double t){
        return new Point ((ps.getX())+(mx*t), (ps.getY())+(my*t));
    }

    public double getSlopeX(){ return mx; }
    public double getSlopeY() {return my; }

    public double getLength(){ return ps.getDistanceTo(pe); }

    public String toString() {
        return "Line {" +
                " ps: " + ps +
                ", pe: " + pe +
                ", mx: " + mx +
                ", my: " + my +
                '}';
    }
}
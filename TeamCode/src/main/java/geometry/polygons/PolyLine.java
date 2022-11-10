package geometry.polygons;

import java.util.ArrayList;

import geometry.framework.Point;
import geometry.polygons.Polygon;
import geometry.position.Line;
import geometry.position.Pose;
import util.template.Iterator;

public class PolyLine extends Polygon {
    boolean connected = false;

    public PolyLine(Point... points){
        addPoints(points);
    }

    public PolyLine(ArrayList<Point> points){ addPoints(points); }
    public PolyLine(ArrayList<Pose> poses, boolean connected){
        Iterator.forAll(poses, pose -> addPoints(pose.getPoint()));
        this.connected = connected;
    }

    @Override
    public ArrayList<Line> getLines() {
        if(connected) {
            return super.getLines();
        }else{
            ArrayList<Line> lines = new ArrayList<>(); for (int i = 1; i < points.size(); i++){ lines.add(new Line(points.get(i-1), points.get(i))); }
            return lines;
        }
    }
}

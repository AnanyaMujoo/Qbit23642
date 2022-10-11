package geometry;

import java.util.ArrayList;
import java.util.Collections;

import geometry.polygons.Rect;
import geometry.position.Line;
import geometry.position.Point;
import geometry.position.Pose;
import geometry.position.Vector;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class CoordinatePlane {

    // TODO 4 NEW Finish all geometric classes in polygon
//
//
//    protected final Pose origin = new Pose(new Point(0, 0), 0);
//
//    private final ArrayList<GeometryObject> objects = new ArrayList<>();
//
//    public void add(GeometryObject... o) { Collections.addAll(objects, o); }
//
//    public void rotate(double ang, AngleType angType) {
//        origin.ang += getAngRad(ang, angType);
//    }
//
//    public void move(double x, double y) { origin.translate(x, y); }
//
//    public void setOrientation(double ang, AngleType angType) {
//        origin.ang = getAngRad(ang, angType);
//    }
//
//    public double getAngRad(double ang, AngleType angType) {
//        return ang * (angType == AngleType.DEGREES ? (PI/180) : 1);
//    }
//
//    // Implements getObjects method – uses blank line (just need it for the type of the variable)
//    public ArrayList<Line> getLines() {
//        return getObjects(Line.class);
//    }
//
//    // Implements getObjects method – uses blank rect (just need it for the type of the variable)
//    public ArrayList<Rect> getRects() {
//        return getObjects(Rect.class);
//    }
//
//    public ArrayList<Vector> getVectors() { return getObjects(Vector.class); }
//
//    public ArrayList<Pose> getPositions() { return getObjects(Pose.class); }
//
//    @SuppressWarnings("unchecked")
//    public <T> ArrayList<T> getObjects(Class<T> type) {
//        ArrayList<T> ret = new ArrayList<>();
//        for (GeometryObject o : getAdjustedObjects()) {
//            if (o.getClass() == type) {
//                ret.add((T) o);
//            }
//        }
//        return ret;
//    }
//
//    private ArrayList<GeometryObject> getAdjustedObjects() {
//        ArrayList<GeometryObject> ret = new ArrayList<>();
//        for (GeometryObject a : objects) {
//            ret.add(a.getRelativeTo(origin));
//        }
//        return ret;
//    }
}
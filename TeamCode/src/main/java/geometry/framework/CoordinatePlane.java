package geometry.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import geometry.framework.GeometryObject;
import geometry.position.Line;
import util.codeseg.ParameterCodeSeg;
import util.template.Iterator;

/**
 * NOTE: Uncommented
 */

public class CoordinatePlane {


    private final ArrayList<GeometryObject> objects = new ArrayList<>();
    public static final Point origin = new Point();

    // TODO 4 NEW Finish all geometric classes in polygon
    // NOTE THAT THERE IS STILL A DEGREES RADIANS ISSUE PLEASE FIX



    public void add(GeometryObject... o) { Collections.addAll(objects, o); }

    private void toAllObjects(ParameterCodeSeg<GeometryObject> code){ Iterator.forAll(objects, code);}

    public void translate(double deltaX, double deltaY){ toAllObjects(o -> o.translate(deltaX, deltaY)); }
    public void rotate(Point anchor, double angle){ toAllObjects(o -> o.rotate(anchor,angle)); }
    public void rotate(double angle){ rotate(origin, angle); }
    public void scale(Point anchor, double angle){ Iterator.forAll(objects, o -> o.scale(anchor,angle)); }
    public void scale(double scale){ scale(origin, scale); }
//
//
//    // Implements getObjects method – uses blank line (just need it for the type of the variable)
//    public ArrayList<Line> getLines() {
//        return getObjects(Line.class);
//    }

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

//    @Override
//    public Point getRelativeTo(Pose origin) {
//        double sx = x - origin.p.x, sy = y - origin.p.y;
//        double nx = sx * cos(origin.ang) + sy * sin(origin.ang);
//        double ny = sx * -sin(origin.ang) + sy * cos(origin.ang);
//        return new Point(nx, ny);
//    }


//    @Override
//    public Pose getRelativeTo(Pose origin) {
//        Pose pos2 = new Pose(p.getRelativeTo(origin), ang);
//        pos2.rotate(-origin.ang);
//        return pos2;
//    }

//    public Vector(double length, double angle) {
//        p = new Point(len * cos(Math.toRadians(angle)), len * sin(Math.toRadians(angle)));
//        theta = atan2(p.getY(), p.getX());
//    }


//    @Override
//    public GeometryObject getRelativeTo(Pose origin) {
//        return new Line(p1.getRelativeTo(origin), p2.getRelativeTo(origin));
//    }


    //Gets a rotated vector of the current vector angle - positive is anticlockwise

//    @Override
//    public Vector getRelativeTo(Pose origin) {
//        double ang = theta + origin.ang;
//        double radius = getLen();
//        return new Vector(cos(ang) * radius, sin(ang) * radius);
//    }
}
package geometry.framework;

import java.util.ArrayList;
import java.util.Collections;

import geometry.polygons.Rect;
import geometry.position.Line;
import geometry.position.Pose;
import geometry.position.Vector;
import util.codeseg.ParameterCodeSeg;
import util.template.Iterator;

/**
 * NOTE: Uncommented
 */

public class CoordinatePlane {


    private final ArrayList<GeometryObject> objects = new ArrayList<>();
    public static final Point origin = new Point();

    public void add(GeometryObject... o) { Collections.addAll(objects, o); }

    private void toAllObjects(ParameterCodeSeg<GeometryObject> code){ Iterator.forAll(objects, code);}

    public void translate(double deltaX, double deltaY){ toAllObjects(o -> o.translate(deltaX, deltaY)); }
    public void rotate(Point anchor, double angle){ toAllObjects(o -> o.rotate(anchor,angle)); }
    public void rotate(double angle){ rotate(origin, angle); }
    public void scale(Point anchor, double angle){ Iterator.forAll(objects, o -> o.scale(anchor,angle)); }
    public void scale(double scale){ scale(origin, scale); }

    public ArrayList<Line> getLines() { return getObjectsOfType(Line.class); }
    public ArrayList<Rect> getRects() { return getObjectsOfType(Rect.class); }
    public ArrayList<Vector> getVectors() { return getObjectsOfType(Vector.class); }
    public ArrayList<Pose> getPoses() { return getObjectsOfType(Pose.class); }
    public <T extends GeometryObject> ArrayList<T> getObjectsOfType(Class<T> type) { return Iterator.forAllOfType(objects, type); }

}
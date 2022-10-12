package geometry.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import util.codeseg.ParameterCodeSeg;
import util.template.Iterator;

/**
 * NOTE: Uncommented
 */

// TODO 4 NEW Geometry package

public abstract class GeometryObject {

    protected Point anchorPoint = new Point();
    protected final ArrayList<Point> points = new ArrayList<>();

    protected final void addPoints(Point... points){ Collections.addAll(this.points, points); }
    public final void setAnchorPoint(Point point){ this.anchorPoint = point; }

    private void toAllPoints(ParameterCodeSeg<Point> code){ Iterator.forAll(points, code);}

    public final void translate(double deltaX, double deltaY){ toAllPoints(p -> p.translate(deltaX, deltaY)); }
    public final void rotate(Point anchor, double angle){ toAllPoints(p -> p.rotate(anchor, angle));}
    public final void rotate(double angle){ rotate(anchorPoint, angle);}
    public final void scale(Point anchor, double scale){ toAllPoints(p -> p.scale(anchor, scale));}
    public final void scale(double scale){ scale(anchorPoint, scale); }

}

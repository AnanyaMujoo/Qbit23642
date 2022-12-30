package elements;

import geometry.circles.Circle;
import geometry.framework.CoordinatePlane;
import geometry.framework.Point;
import geometry.polygons.PolyLine;
import geometry.polygons.Rect;
import geometry.position.Line;
import geometry.position.Pose;
import global.Constants;

public class Field {
    public static final double width = 144*Constants.INCH_TO_CM;
    public static final double tileWidth = 24* Constants.INCH_TO_CM;
    public static final CoordinatePlane plane = new CoordinatePlane();

    static {
       plane.add(new Rect(new Point(), new Point(width, width)));
    }
}

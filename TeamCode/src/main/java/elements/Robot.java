package elements;

import geometry.circles.Circle;
import geometry.framework.CoordinatePlane;
import geometry.framework.Point;
import geometry.polygons.PolyLine;
import geometry.polygons.Rect;
import geometry.position.Pose;

public class Robot{
    public static final double length = 40; // cm
    public static final double width = 32;
    public static final double distanceToCone = 24;
    public static final double clawWidth = 16;
    public static final CoordinatePlane plane = new CoordinatePlane();

    static {
        plane.add(new Rect(new Point(-width/2.0,-length/2.0), new Point(width/2.0,length/2.0)));
        plane.add(new Pose());
        plane.add(new PolyLine(new Point(-clawWidth/2.0, -(distanceToCone+(GameItems.Cone.baseWidth/2.0))), new Point(-clawWidth/4.0, -length/2.0 - 1), new Point(clawWidth/4.0, -length/2.0 - 1), new Point(clawWidth/2.0, -distanceToCone-(GameItems.Cone.baseWidth/2.0))));
        plane.add(new Circle(new Point(0,40), 6));
        plane.add(new Circle(new Point(0, -26), 8));
    }
}

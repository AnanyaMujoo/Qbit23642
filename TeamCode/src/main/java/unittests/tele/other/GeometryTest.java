package unittests.tele.other;

import display.Drawer;
import geometry.circles.Circle;
import geometry.framework.CoordinatePlane;
import geometry.framework.Point;
import geometry.polygons.Rect;
import geometry.polygons.Triangle;
import geometry.position.Line;
import geometry.position.Pose;

public class GeometryTest extends Drawer {

    public static void main(String[] args) { drawWindow(new GeometryTest(), "Display");}

    @Override
    public void define() {
        Line line = new Line(new Point(100, 100), new Point(300,300));
        Pose pose = new Pose(new Point(150,100), 90);
        Triangle triangle = new Triangle(new Point(300,300), new Point(400,500), new Point(600,100));
        Rect rect = new Rect(new Point(300,300), new Point(400,400));
        Circle circle = new Circle(new Point(200,200), 50);
        drawPlane(new CoordinatePlane(line, pose, triangle, rect, circle));
    }
}

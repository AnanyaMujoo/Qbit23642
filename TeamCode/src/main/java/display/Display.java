package display;

import geometry.circles.Circle;
import geometry.framework.Point;
import geometry.position.Line;
import geometry.position.Vector;

public class Display extends Drawer {

    public static void main(String[] args) {

//        drawWindow(new Display(), "Display");

//        Vector v = new Vector(1.0,0);
//        v.rotate(90);
//        Vector v2 = v.getRotated(90);
//        System.out.println(v);
//        System.out.println(v2);

        Circle circle = new Circle(new Point(3,4), 5);
        Point point = new Point(9, -4);
        Point close = circle.getClosestTo(point);
        Line line = new Line(close, point);
        System.out.println(line.getMidpoint());
    }

    @Override
    public void define() {

    }
}

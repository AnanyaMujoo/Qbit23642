package display;

import geometry.framework.Point;
import geometry.polygons.Rect;
import geometry.polygons.Triangle;
import geometry.position.Vector;

public class Display extends Drawer {
    // TODO 4 NEW Create Display

    private static final int width = 700;
    private static final int height = 700;

    public static void main(String[] args) {

        Triangle triangle = new Triangle(new Point(3,3), new Point(4,5), new Point(8,1));
        Rect rect = new Rect(new Point(3,3), new Point(8,8));

        System.out.println(triangle.getArea());
        System.out.println(triangle.getBoundingBox());
        System.out.println(rect.getArea());



//        Point p = new Point(1,2);
//        p.rotate(new Point(), 90);
//        System.out.println(p);
//        Vector v = new Vector(1,2);
//        System.out.println(v.getRotated(90));
//        drawWindow(width, height, "Bbox");


//        Function f = new Function() {
//            @Override
//            public double f(double x) {
//                return Math.pow(x,2);
//            }
//        };
//        Integrator integrator = new Integrator();
//        integrator.defineFunction(f);
//        Differentiator differentiator = new Differentiator();
//        differentiator.defineFunction(f);
//        double step = 0.01;
//        double x = 0;
//        while (x < (3-step)){
//            integrator.integrate(x, step);
//            x += step;
//        }
//        differentiator.differentiate(3, 0.01);
//        System.out.println("Integral: " + Math.round(integrator.getIntegral()));
//        System.out.println("Derivative: " + Math.round(differentiator.getDerivative()));
//        System.out.println("Integral: " + integrator.getIntegral());
        //drawWindow(width, height, "Display");
//        Triangle triangle = new Triangle(new Point(100, 100), new Point(100, 140), new Point(130, 100));
//        double area = triangle.area();
//        Rect bbox = triangle.boundingbox();
//        System.out.println(area);
//        System.out.println(bbox.toString());

//
//        Display display = new Display();
//        display.drawPoint(triangle.points.get(0));
//        display.drawPoint(triangle.points.get(1));
//        display.drawPoint(triangle.points.get(2));
//        drawWindow(width, height, "Bbox");
//        drawPoint(new Point(100,100));
//        drawLine(new Line(new Point(0,0), new Point(60,60)));
//        drawPose(new Pose(new Point(200,200),90, AngleType.DEGREES));
//        drawCircularArc(new Point(100,200),40, 0,90, AngleType.DEGREES);
//        drawCircle(new Point(200, 200), 100);
    }

}

package display;

import org.checkerframework.checker.units.qual.C;

import geometry.circles.Circle;
import geometry.framework.CoordinatePlane;
import geometry.framework.Point;
import geometry.position.Line;
import geometry.position.Pose;
import geometry.position.Vector;
import math.misc.Logistic;

public class Display extends Drawer {

    public static CoordinatePlane plane = new CoordinatePlane();

    static {
        listener = e -> {
            char c = e.getKeyChar();
            if (c == 'q') {
                shouldExit = true;
                System.exit(0);
            }
        };
    }

    public static void main(String[] args) {

        Vector v = new Vector(1.0,0);
        v.rotate(90);
        Vector v2 = v.getRotated(90);
        System.out.println(v);
        System.out.println(v2);

//        Circle circle = new Circle(new Point(3,4), 5);
//        Point point = new Point(9, -4);
//        Point close = circle.getClosestTo(point);
//        Line line = new Line(close, point);
//        System.out.println(line.getMidpoint());
//        Point junctionLocation = new Point(30.5, -158);
//        Pose junctionTargetPose = new Pose(0, 22, 0);
//        Pose startJunctionPose = new Pose(0, 22, 3);
//        double cameraToRobotCenter = 19.5;
////        Pose startOdometryPose = new Pose(-10, -140.0, 60);
//        Pose startOdometryPose = new Pose(-1.2, -130.2, 51);
//
//        double scale = (double) (fieldWidth)/fieldSize;
//
//
//        Circle detectionCircle = new Circle(junctionLocation.getCopy(), startJunctionPose.getPoint().getCopy().getDistanceToOrigin() + cameraToRobotCenter);
//        Point closestPoint = detectionCircle.getClosestTo(startOdometryPose.getPoint()).getCopy();
//        Line error = new Line(closestPoint.getCopy(), startOdometryPose.getPoint().getCopy());
//        Line detection = new Line(junctionLocation.getCopy(), closestPoint.getCopy());

//
//        System.out.println("Error : " + error.getLength());
//        System.out.println("Angle2: " + startJunctionPose.getAngle());
//        System.out.println("Angle : " + (detection.getVector().getRotated(-90).getTheta()+startJunctionPose.getAngle()));
//
//
//
//        plane = new CoordinatePlane(new Pose(0,0,0), detectionCircle.getScaledRadius(scale), detection);
//        convertToField(plane);
//
//        drawWindow(new Display(), "Display");


        Logistic r = new Logistic(Logistic.LogisticParameterType.RP_K, 0.07, 2.0);

        System.out.println(r.fodd(0.01));
        System.out.println(r.fodd(0.5));
        System.out.println(r.fodd(1.0));
    }

    @Override
    public void define() {
//        drawField();
//        drawPlane(autoPlane);
        drawPlane(plane);
    }
}

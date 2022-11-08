package display;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import auton.TerraAuto;
import autoutil.AutoUser;
import geometry.circles.Circle;
import geometry.framework.CoordinatePlane;
import geometry.framework.Point;
import geometry.polygons.Polygon;
import geometry.polygons.Rect;
import geometry.polygons.Triangle;
import geometry.position.Line;
import geometry.position.Pose;
import geometry.position.Vector;
import math.linearalgebra.Matrix2D;
import util.template.Iterator;

public class Display extends Drawer {

    private static final TerraAuto terraAuto = new TerraAuto();

    public static void main(String[] args) {
        terraAuto.define();

//        drawWindow(new Display(), "Display");
    }

    @Override
    public void define() {
//        terraAuto.initAuto();
        CoordinatePlane plane = new CoordinatePlane(new Pose(20,50,90));
        Pose startPose1 = new Pose(20,fieldSize/2.0,0);
        Pose startPose2 = new Pose(fieldSize-20,fieldSize/2.0,180);
        drawField();
        drawOnField(plane, startPose2);
    }
}

package display;

import org.checkerframework.checker.units.qual.C;

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

    public static void main(String[] args) {
        drawWindow(new Display(), "Display");
    }

    @Override
    public void define() {

    }
}

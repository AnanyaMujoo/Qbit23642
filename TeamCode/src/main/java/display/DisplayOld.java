package display;


import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import unused.mecanumold.oldauto.ArcGenerator;
import unused.mecanumold.oldauto.FinalPathArc;
import unused.mecanumold.oldauto.PathSegment;
import geometry.position.Pose;

import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class DisplayOld extends JPanel {

    // TOD3 NEW
    // Make this a actual frame work that lets you draw things

    private final int height = 700;
    private final int width = 700;
    private final int xScale = 5;
    private final int yScale = 5;
    private FinalPathArc pathToDisplay;

    public void genTestPlane(){
        ArcGenerator arcGenerator = new ArcGenerator();
        arcGenerator.moveTo(0,0, PI/2);
        arcGenerator.moveTo(70, 30, 0);
        arcGenerator.moveTo(-50,30, PI);
        arcGenerator.moveTo(-70, 30, PI/4);
//        arcGenerator.moveTo(30,30, PI/2);
//        arcGenerator.moveTo(0, 0, -PI/2);
//        arcGenerator.moveTo(25, 20, 3 * PI/2);

        //(10, 5, pi) , (30, 5, pi), (30, 30, pi/2), (0, 0, -pi/2) , (25, 20, 3pi/2)

        pathToDisplay = arcGenerator.done();

    }

    @Override
    public void paintComponent(Graphics g) {
        for (PathSegment ps : pathToDisplay.segments) {
            for (Pose p : ps.points) {
                g.fillOval(pX(p.p.x), pY(p.p.y), 5, 5);
            }
        }
    }

    private int pX(double x){
        return (int)(x*xScale) + width/2;
    }
    private int pY(double y){
        return height/2 - (int)(y*yScale);
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Coordinate Frame 1");
        DisplayOld display = new DisplayOld();
        display.genTestPlane();
        window.add(display);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(display.height, display.width);
        window.setVisible(true);
    }
}

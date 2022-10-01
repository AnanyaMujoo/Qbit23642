package display;


import static java.lang.Math.PI;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import unused.mecanumold.oldauto.ArcGenerator;
import unused.mecanumold.oldauto.FinalPathArc;
import unused.mecanumold.oldauto.PathSegment;
import geometry.position.Pose;

/**
 * NOTE: Uncommented
 */

public class DisplayPath extends JPanel {

    private final int height = 1000;
    private final int width = 1000;
    private final int xScale = 3;
    private final int yScale = 3;
    private FinalPathArc pathToDisplay;

    public void genTestPlane(){
        ArcGenerator arcGenerator = new ArcGenerator();
        arcGenerator.moveTo(0,0, PI/2);
        arcGenerator.moveTo(70, 30, 0);
        arcGenerator.moveTo(-50,30, PI);
        arcGenerator.moveTo(-70, 30, PI/4);

        //(10, 5, pi) , (30, 5, pi), (30, 30, pi/2), (0, 0, -pi/2) , (25, 20, 3pi/2)

        pathToDisplay = arcGenerator.done();

    }

    @Override
    public void paintComponent(Graphics g) {
        for (PathSegment ps : pathToDisplay.segments) {
            for (Pose p : ps.points) {
                g.fillOval(pX(p.p.x), pY(p.p.y), 5, 5);
                System.out.println(p.p.x + " " + p.p.y + " " + p.ang);
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
        DisplayPath display = new DisplayPath();
        display.genTestPlane();
        window.add(display);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(display.height, display.width);
        window.setVisible(true);
    }
}

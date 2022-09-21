package display;


import static java.lang.Math.PI;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import autoutil.executors.Executor;
import autoutil.executors.MecanumExecutorArcsPID;
import geometry.circles.AngleType;
import geometry.position.Pose;

/**
 * NOTE: Uncommented
 */

public class DisplayExecutor extends JPanel {

    private final int height = 1000;
    private final int width = 1000;
    private final int xScale = 5;
    private final int yScale = 5;

    private Executor executor;

    public void genTestPlane(){
        executor = new MecanumExecutorArcsPID(new LinearOpMode() {
            @Override
            public void runOpMode() {}
        });
//        executor.addSetpoint(70, 29, PI/2, AngleType.RADIANS);
//        executor.addSetpoint(-50, 40, -PI/2, AngleType.RADIANS);
//        executor.addSetpoint(-65, 40, -PI/2, AngleType.RADIANS);
////        executor.addSetpoint(-60, 40, -3 * PI/4, AngleType.RADIANS);
//        executor.addSetpoint(-75, 35, -PI/2, AngleType.RADIANS);
        executor.addSetpoint(70, 30, PI/2, AngleType.RADIANS);
        executor.addSetpoint(-50, 43, -PI/2, AngleType.RADIANS);
        executor.addSetpoint(-60, 30, PI, AngleType.RADIANS);
        executor.addSetpoint(-90, 0, -PI/2, AngleType.RADIANS);
        executor.complete();
    }

    @Override
    public void paintComponent(Graphics g) {
        for (ArrayList<Pose> poses : executor.paths) {
            for (Pose p : poses) {
                g.fillOval(pX(p.p.x), pY(p.p.y), 5, 5);
                System.out.println(p.p.x + " " + p.p.y + " " + p.ang);
            }
            System.out.println("---");
        }
        System.out.println("---");
        System.out.println("---");
    }

    private int pX(double x){
        return (int)(x*xScale) + width/2;
    }
    private int pY(double y){
        return height/2 - (int)(y*yScale);
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Coordinate Frame 1");
        DisplayExecutor display = new DisplayExecutor();
        display.genTestPlane();
        window.add(display);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(display.height, display.width);
        window.setVisible(true);
    }
}

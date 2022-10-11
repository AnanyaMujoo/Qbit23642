package display;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import geometry.position.Point;
import geometry.position.Pose;

import static java.lang.Math.PI;
import static java.lang.Math.*;

/**
 * NOTE: Uncommented
 */

public class PathTester extends JPanel {
//    private final int height = 800;
//    private final int width = 800;
//    private final int xScale = 10;
//    private final int yScale = 10;
//    private Graphics2D g;
//
//
//    public void testPose(){
//        Pose pose1 = new Pose(new Point(10,10), PI/2);
//        drawPose(pose1);
//        pose1.translate(5,5);
//        drawPose(pose1);
//        pose1.rotate(PI/4);
//        drawPose(pose1);
//        Pose pose2 = new Pose(new Point(8,10), -PI/4);
//        drawPose(pose2);
////        Pose pose3 = pose1.getRelativeTo(pose2);
////        drawPose(pose3);
//    }
//    public void testPoint(){
//        Point point1 = new Point(5,5);
//        drawPoint(point1);
////        Point point2 = point1.getRelativeTo(new Pose(new Point(3,3), -PI/8));
////        drawPoint(point2);
//    }
//    private void drawPose(Pose p){
//        drawVect(p.p, p.ang);
//    }
//    private void drawPoint(Point p){
//        g.setColor(Color.black);
//        g.fillOval(pX(p.x), pY(p.y),3,3);
//    }
//    private void drawLine(Point p1, Point p2){
//        g.setColor(Color.darkGray);
//        g.drawLine(pX(p1.x)+1, pY(p1.y)+1, pX(p2.x)+1, pY(p2.y)+1);
//    }
//    private void drawVect(Point p, double a){
//        drawLine(p, new Point(p.x+cos(a), p.y+sin(a)));
//        drawPoint(p);
//    }
//
//    private void initGraphics(Graphics graphics){
//        g = (Graphics2D) graphics;
//        g.setStroke(new BasicStroke(3));
//        g.setColor(Color.black);
//    }
//
//    @Override
//    public void paintComponent(Graphics g) {
//        initGraphics(g);
////        testPoint(); // Works
////        testPose(); // Works
//    }
//
//    private int pX(double x){
//        return (int)(x*xScale);
//    }
//    private int pY(double y){
//        return (int)(height-(y*yScale)-41);
//    }
//
////    public static void main(String[] args) {
////        JFrame window = new JFrame("Coordinate Frame 1");
////        PathTester display = new PathTester();
////        window.add(display);
////        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////        window.setSize(display.height, display.width);
////        window.setVisible(true);
////    }
}

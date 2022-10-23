package display;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import geometry.framework.Point;
import geometry.position.Line;
import geometry.position.Pose;

public class Drawer extends JPanel {
    protected Graphics2D g;
    protected final int pointSize = 5;
    protected Color pointColor = Color.BLACK;
    protected final int lineWidth = 2;
    protected Color lineColor = new Color(0, 102, 0);
    protected final int poseLength = 15;
    protected final Color poseColor = new Color(102, 0, 0);
    protected final int arcWidth = 2;
    protected final Color arcColor = new Color(122, 56, 3);
    protected final int circleWidth = 2;
    protected final Color circleColor = new Color(72, 76, 3);

    protected final ArrayList<Pose> poses = new ArrayList<>();

    // TOD4 NEW
    // Finish this


    public void drawPoint(Point p){
        g.setColor(pointColor);
//        g.fillOval((int) p.x, (int) p.y, pointSize, pointSize);
    }
    public void drawLine(Line l){
        g.setColor(lineColor);
        g.setStroke(new BasicStroke(lineWidth));
//        g.drawLine((int) l.p1.x+3, (int) l.p1.y+3, (int) l.p2.x+3, (int) l.p2.y+3);
    }
    public void drawPose(Pose p){
//        g.setColor(poseColor);
//        g.setStroke(new BasicStroke(lineWidth));
//        Vector poseLine = new Vector(poseLength,p.ang, AngleType.RADIANS);
//        double offset = 3;
//        g.drawLine((int) (p.p.x+offset), (int) (p.p.y+offset), (int) (p.p.x + poseLine.getX()+offset), (int) (p.p.y + poseLine.getY()+offset));
//        drawPoint(p.p);
    }

    public void drawCircle(Point c, double r){
//        g.setColor(circleColor);
//        g.setStroke(new BasicStroke(circleWidth));
//        g.drawOval((int) c.x, (int) c.y, (int) r,(int) r);
    }

    public void drawField(){
//        final BufferedImage image;
//        File f = new File(System.getProperty("user.dir")+ "\\TeamCode\\src\\main\\java\\display\\ff3.png");
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
//        try {
//            image = ImageIO.read(f);
//            g.drawImage(image, 0, 0, 790, 770, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

    }



//
//
//    public void define(){
//        drawField();
//
//        addWaypoint(0,0,0);
//        addWaypoint(0,30,-60);
//        addWaypoint(15,55,-135);
//        addWaypoint(10,25,-115);
//        addWaypoint(10,-5,-90);
//        addWaypoint(-10,-10,-90);
//        addWaypoint(-70,-10,-90);
//        addWaypoint(-10,-12,-90);
//        addWaypoint(10,-7,-90);
//        addWaypoint(10,23,-115);
////        addWaypoint(15,53,-135);
////        addWaypoint(-70, 50, -90);
//        // Just Duck
////        addWaypoint(20,20,45);
////        addWaypoint(50,30,90);
////        addWaypoint(-20,65,135);
////        addWaypoint(60,70,90);
//
//        // Everything
////        addWaypoint(-50,55,-90);
////        addWaypoint(-105,55,-135);
////        addWaypoint(-110,25,-115);
////        addWaypoint(-110,-5,-90);
////        addWaypoint(-130,-10,-90);
////        addWaypoint(-190,-10,-90);
//    }
//
//    public double toPixX(double cm){
//        return (cm*2.13);
//    }
//    public double toPixY(double cm){
//        return (cm*2.09);
//    }
//
    @Override
    public void paintComponent(Graphics g) {
        this.g = (Graphics2D) g;
//        define();
////        Point start = new Point(367-22, 154);
//        Point start = new Point(22, 154);
////        Point start = new Point(22, 274);
////        Point start = new Point(367-22, 274);
//        ArrayList<Pose> newPoses = new ArrayList<>();
//        for (Pose p: poses){
////            double x = toPixX(p.p.y+start.x);
////            double y = toPixY(p.p.x+start.y);
////            double h = p.ang + AngleType.degToRad(0);
////            newPoses.add(new Pose(new Point(x,y),h));
//        }
//        for (int i = 0; i < newPoses.size()-1; i++){
//            drawLine(new Line(newPoses.get(i).p,newPoses.get(i+1).p));
//        }
//        for(Pose p1: newPoses) {
//            drawPose(p1);
//        }

//
//
//        g.dispose();
    }
//
//
//
    public static void drawWindow(int width, int height, String name){
        JFrame window = new JFrame(name);
        Drawer drawer = new Drawer();
        window.add(drawer);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(height, width);
        window.setVisible(true);
    }

//
//
//
//
//
//
//
//
//
//
//    public void whatToDraw() {
////        Triangle triangle = new Triangle(new Point(200,240), new Point(300,180), new Point(260,160));
////        double area = triangle.area();
////        Rect bbox = triangle.boundingbox();
////        System.out.println(area);
////        System.out.println(bbox.toString());
////        Point p1 = triangle.points.get(0);
////        Point p2 = triangle.points.get(1);
////        Point p3 = triangle.points.get(2);
////        drawLine(new Line(p1, p2));
////        drawLine(new Line(p2, p3));
////        drawLine(new Line(p1, p3));
////        drawPoint(p1);
////        drawPoint(p2);
////        drawPoint(p3);
////        Point r1 = new Point(bbox.getX1(), bbox.getY1());
////        Point r2 = new Point(bbox.getX1(), bbox.getY2());
////        Point r3 = new Point(bbox.getX2(), bbox.getY2());
////        Point r4 = new Point(bbox.getX2(), bbox.getY1());
////        pointColor = Color.RED;
////        lineColor = Color.BLUE;
////        drawLine(new Line(r1, r2));
////        drawLine(new Line(r2, r3));
////        drawLine(new Line(r3, r4));
////        drawLine(new Line(r4, r1));
////        drawPoint(r1);
////        drawPoint(r2);
////        drawPoint(r3);
////        drawPoint(r4);
////    }
//    }
}

package display;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import geometry.circles.Circle;
import geometry.framework.CoordinatePlane;
import geometry.framework.GeometryObject;
import geometry.framework.Point;
import geometry.polygons.PolyLine;
import geometry.polygons.Polygon;
import geometry.polygons.Quadrilateral;
import geometry.polygons.Rect;
import geometry.position.Line;
import geometry.position.Pose;
import util.ExceptionCatcher;
import util.codeseg.CodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.template.Iterator;

public abstract class Drawer extends JPanel {
    protected Graphics2D g;
    protected final int pointSize = 5;
    protected Color pointColor = Color.BLACK;
    protected final int lineWidth = 2;
    protected Color lineColor = new Color(13, 102, 10);
    protected final int poseLength = 15;
    protected final Color poseColor = new Color(102, 0, 0);
    protected final int circleWidth = 2;
    protected final Color circleColor = new Color(220, 120, 19);

    private static final int width = 700;
    private static final int height = 700;
    private static final int fieldWidth = width-15;
    private static final int fieldHeight = height-35;

    public static final double fieldSize = 358; // cm

    public static boolean shouldExit = false;
    public static int step = 0;
    public static boolean lastStep = false;

    public static final double refreshRate = 50;


    public final Timer timer = new Timer((int)(1000/refreshRate), actionEvent -> repaint());



    public abstract void define();

    public void setColor(Color color){ g.setColor(color); }
    public void setStroke(int width){ g.setStroke(new BasicStroke(width));}

    public void drawField(){
        drawImage("powerPlay.png",  fieldWidth,fieldHeight, 0.4);
    }

    public void drawImage(String filename, int width, int height, double transparency) {
        final BufferedImage image;
        File f = new File(System.getProperty("user.dir") + "\\TeamCode\\src\\main\\java\\display\\" + filename);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) transparency));
        try {
            image = ImageIO.read(f);
            g.drawImage(image, 0, 0, width, height, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public void drawLine(Line line, Color color) { setColor(color); setStroke(lineWidth); g.drawLine((int) line.getStartPoint().getX(),(int) line.getStartPoint().getY(),(int) line.getEndPoint().getX(),(int) line.getEndPoint().getY()); }
    public void drawLine(Line line){ drawLine(line, lineColor); }

    public void drawCircle(Circle circle){ setColor(circleColor); setStroke(circleWidth); g.drawOval((int) (circle.getCenterX()-circle.getRadius()), (int) (circle.getCenterY() - circle.getRadius()), (int) (2*circle.getRadius()), (int) (2*circle.getRadius())); }

    public void drawPolygon(Polygon polygon){
        Iterator.forAll(polygon.getLines(), this::drawLine);
    }

    public void drawPoint(Point point){ setColor(pointColor); g.fillOval((int) point.getX() - (pointSize/2), (int) point.getY() - (pointSize/2), (int) (pointSize), (int) (pointSize)); }

    public void drawPose(Pose pose){ drawLine(new Line(pose.getPoint(), pose.getAngleUnitVector().getScaled(poseLength).getPoint().getTranslated(pose.getX(), pose.getY())), poseColor); drawPoint(pose.getPoint());}

    public void drawPlane(CoordinatePlane coordinatePlane){
        Iterator.forAll(coordinatePlane.getLines(), this::drawLine);
        Iterator.forAll(coordinatePlane.getPoses(), this::drawPose);
        Iterator.forAll(coordinatePlane.getPolygons(), this::drawPolygon);
        Iterator.forAll(coordinatePlane.getCircles(), this::drawCircle);
    }

    public static void convertToField(CoordinatePlane coordinatePlane, Pose startPose){
        coordinatePlane.rotate(startPose.getAngle()-90);
        coordinatePlane.toPoses(pose -> pose.rotateOrientation(90));
        coordinatePlane.reflectPoses();
        coordinatePlane.translate(startPose.getX(), startPose.getY());
        coordinatePlane.reflectY();
        coordinatePlane.translate(0, fieldSize);
        coordinatePlane.scaleX(((double) fieldWidth)/fieldSize);
        coordinatePlane.scaleY(((double) fieldHeight)/fieldSize);
    }

    public static GeometryObject convertToField(GeometryObject object, Pose startPose){
        CoordinatePlane coordinatePlane = new CoordinatePlane(object);
        convertToField(coordinatePlane, startPose);
        return coordinatePlane.getAll().get(0);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = (Graphics2D) g;
        define();
        this.g.dispose();
    }

    protected static ParameterCodeSeg<KeyEvent> listener;

    private static final KeyListener keyListener = new KeyListener() {
        @Override
        public void keyPressed(KeyEvent e) {
            listener.run(e);
        }
        @Override
        public void keyTyped(KeyEvent keyEvent) {}
        @Override
        public void keyReleased(KeyEvent keyEvent) {}
    };

    public static void drawWindow(Drawer drawer, String name){
        JFrame window = new JFrame(name);
        window.addKeyListener(keyListener);
        window.add(drawer);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(width, height);
        window.setVisible(true);
        drawer.timer.start();
        while (!shouldExit){}
        drawer.timer.stop();
    }

    public static CoordinatePlane getRobot(Pose startPose, Pose pose){
        CoordinatePlane robot = new CoordinatePlane();
        robot.add(new Rect(new Point(-16,-20), new Point(16,20)));
        robot.add(new Pose());
        robot.add(new PolyLine(new Point(-8, -28), new Point(-3, -23), new Point(3, -23), new Point(8,-28)));
        robot.add(new Circle(new Point(0,44), 6));
        robot.add(new Circle(new Point(0, -27), 8));
        robot.reflectY(); robot.reflectX();
        robot.rotate(pose.getAngle());
        robot.translate(pose.getX(), pose.getY());
        convertToField(robot, startPose);
        return robot;
    }

    // TOD 5 Add resize method for width and height
}

package display;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import auton.TerraAuto;
import autoutil.AutoFramework;
import geometry.framework.CoordinatePlane;
import geometry.framework.Point;
import geometry.position.Line;
import geometry.polygons.PolyLine;
import geometry.position.Pose;
import geometry.position.Vector;
import util.template.Iterator;

public class AutoSimulator extends Drawer{

    // TODO MAKE SIMULATION

    public static void main(String[] args) {
        setAuto(new TerraAuto(), new Pose(25,fieldSize/2.0,0));
        drawWindow(new AutoSimulator(), "Auto Simulator");
    }

    @Override
    public void define() {
        simulateAuto();
        drawField(); drawPlane(autoPlane); drawPlane(robot);
        currentTime += (1.0/refreshRate);
    }




    private static CoordinatePlane autoPlane;
    private static CoordinatePlane robot;
    private static Pose startPose;
    private static Pose robotPose = new Pose();
    private static ArrayList<Pose> poses = new ArrayList<>();
    private static ArrayList<Line> lines = new ArrayList<>();
    private static int segmentIndex = 0;
    public static double currentTime = 0;

    private static void setAuto(AutoFramework auto, Pose startPose) {
        auto.setup();
        if(auto.isFlipped()){ startPose.scaleX(-1); startPose.translate(fieldSize, 0); startPose.rotateOrientation(180);}
        autoPlane = auto.getAutoPlane();

        poses = autoPlane.getCopyOfPoses();
        PolyLine path = new PolyLine(poses, false); lines = path.getLines();

        convertToField(autoPlane, startPose);
        AutoSimulator.startPose = startPose;

        updateRobotPose(new Pose());
    }

    public static void updateRobotPose(Pose velocity){
        velocity.scale(1.0/refreshRate); velocity.scaleOrientation(1.0/refreshRate);
        robotPose.translate(velocity.getX(), velocity.getY());
        robotPose.rotate(velocity.getAngle());
        robot = getRobot(startPose, robotPose);
    }

    public static void updateRobotPose(Point position, double heading){
        robotPose = new Pose(position, heading);
        robot = getRobot(startPose, robotPose);
    }

    public static void updateRobotPose(double heading){
        robotPose = new Pose(robotPose.getPoint(), heading+startPose.getAngle());
        robot = getRobot(startPose, robotPose);
    }


    public static void simulateAuto(){
        if(segmentIndex < lines.size()) {
            Line currentLine = lines.get(segmentIndex);
            double lastHeading = poses.get(segmentIndex).getAngle();
            double targetHeading = poses.get(segmentIndex+1).getAngle();


            double deltaHeading = targetHeading - lastHeading;
            double maxVel = 100; double maxRot = 270;
            double totalTime = (currentLine.getLength() / maxVel);
            double turningTime = (Math.abs(deltaHeading) / maxRot);

            Point currentPoint = robotPose.getPoint();
            double currentHeading = robotPose.getAngle()-startPose.getAngle();

            if(totalTime > 0.01){
                currentPoint = currentLine.getAt(currentTime / totalTime);
            }

            if(turningTime > 0.01){
                currentHeading = lastHeading + (deltaHeading * currentTime / turningTime);
            }

            if(currentTime / totalTime <= 1) {
                updateRobotPose(currentPoint, currentHeading);
            }else{
                if(currentTime / turningTime <= 1) {
                    updateRobotPose(currentHeading);
                }else{
                    nextSegment();
                }
            }
        }
    }

    private static void nextSegment(){
        segmentIndex++;
        currentTime = 0;
    }







}

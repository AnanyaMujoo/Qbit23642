package display;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Locale;

import automodules.AutoModuleUser;
import auton.TerraAuto.*;
import autoutil.AutoFramework;
import geometry.framework.CoordinatePlane;
import geometry.framework.Point;
import geometry.position.Line;
import geometry.polygons.PolyLine;
import geometry.position.Pose;
import util.codeseg.CodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.template.Iterator;

public class AutoSimulator extends Drawer{

    private static final Pose startLower = new Pose(20.5,fieldSize/2.0 - 89,180);
    private static final Pose startUpper = new Pose(20.5,fieldSize/2.0 + 89,180);
    private static final double maxMovingVelocity = 150; // cm per sec
    private static final double maxTurningVelocity = 340; // deg per sec
    private static final double speedUp = 1.0;
    private static final boolean developmentMode = true;

    public static void main(String[] args) {
//        setAuto(new TerraAutoLowerBlue(), startLower);
//        setAuto(new TerraAutoUpperBlue(), startUpper);
//        setAuto(new TerraAutoLowerRed(), startLower);
//        setAuto(new TerraAutoUpperRed(), startUpper);
//        setAuto(new TerraAutoTest(), startLower);

//        setAuto(new TerraAutoLowerBlueSimple(), startLower);
//        setAuto(new TerraAutoUpperBlueSimple(), startLower);
//        setAuto(new TerraAutoLowerRedSimple(), startLower);
//        setAuto(new TerraAutoUpperRedSimple(), startLower);

        setAuto(AutoModuleUser.Cycle, startLower);

        drawWindow(new AutoSimulator(), "Auto Simulator");
    }

    @Override
    public void define() {
        simulateAuto();
        drawField();
        drawPlane(autoPlane);
        drawPlane(robot);
        currentTime += (speedUp/refreshRate);
    }




    private static CoordinatePlane autoPlane;
    private static CoordinatePlane robot;
    private static Pose startPose;
    private static Pose robotPose = new Pose();
    private static ArrayList<Pose> poses = new ArrayList<>();
    private static ArrayList<Line> lines = new ArrayList<>();
    private static int segmentIndex = 0;
    public static double currentTime = 0;
    public static ElapsedTime timer = new ElapsedTime();

    private static void setAuto(AutoFramework auto, Pose startPose) {
        auto.setup();
        if(auto.isFlipped()){ startPose.scaleX(-1); startPose.translate(fieldSize, 0); startPose.rotateOrientation(180);}
        autoPlane = auto.getAutoPlane();
        autoPlane.removeRedundantObjects();

        poses = autoPlane.getCopyOfPoses();
        PolyLine path = new PolyLine(poses, false); lines = path.getLines();

        convertToField(autoPlane, startPose);
        AutoSimulator.startPose = startPose;

        updateRobotPose(new Pose());
        timer.reset();
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
        robotPose = new Pose(robotPose.getPoint(), heading);
        robot = getRobot(startPose, robotPose);
    }


    public static void simulateAuto(){
        if(!developmentMode) {
            if (segmentIndex < lines.size()) {
                Line currentLine = lines.get(segmentIndex);
                double lastHeading = poses.get(segmentIndex).getAngle();
                double targetHeading = poses.get(segmentIndex + 1).getAngle();


                double deltaHeading = targetHeading - lastHeading;
                double totalTime = (currentLine.getLength() / maxMovingVelocity);
                double turningTime = (Math.abs(deltaHeading) / maxTurningVelocity);

                Point currentPoint = robotPose.getPoint();
                double currentHeading = robotPose.getAngle();

                Pose targetPose = new Pose(currentLine.getEndPoint(), targetHeading);

                if (totalTime > 0.01) {
                    currentPoint = currentLine.getAt(currentTime / totalTime);
                }
                if (turningTime > 0.01) {
                    currentHeading = lastHeading + (deltaHeading * currentTime / turningTime);
                }

                if (currentTime / totalTime <= 1) {
                    if (currentTime / turningTime <= 1) {
                        updateRobotPose(currentPoint, currentHeading);
                    } else {
                        updateRobotPose(currentPoint, robotPose.getAngle());
                    }
                } else {
                    if (currentTime / turningTime <= 1) {
                        updateRobotPose(currentHeading);
                    } else {
                        updateRobotPose(targetPose.getPoint(), targetPose.getAngle());
                        nextSegment();
                    }
                }
            } else if (segmentIndex == lines.size()) {
                System.out.println("Time taken: " + timer.seconds() * speedUp);
                System.out.println("Estimated Robot Time: " + 2.32 * (timer.seconds() * speedUp));
                segmentIndex++;
            }
        }else{
            if(step < poses.size()) {
                if(!editingMode){
                    Pose targetPose = poses.get(step).getCopy();
                    updateRobotPose(targetPose.getPoint(), targetPose.getAngle());
                }else{
                    updateRobotPose(robotPose.getPoint(), robotPose.getAngle());
                }
            }else{
                step = poses.size()-1;
            }
        }
    }

    private static void nextSegment(){
        segmentIndex++;
        currentTime = 0;
    }

    public static boolean editingMode = false;

    static {
        listener = e -> {
            char c = e.getKeyChar();
            if(c == 'q'){ shouldExit = true; System.exit(0); }
            if(c == 'e'){
                editingMode = !editingMode;
                if(editingMode){
                    System.out.printf(Locale.US, "Editing Pose %d, %s %n", step, robotPose.toString());
                }else{
                    System.out.printf(Locale.US, "Saved Pose %d, New %s Code %.1f, %.1f, %.1f %n", step, robotPose.toString(), -robotPose.getX(), -robotPose.getY(), robotPose.getAngle());
                    autoPlane.remove(convertToField(poses.get(step).getCopy(), startPose));
                    poses.set(step, robotPose);
                    autoPlane.add(convertToField(robotPose.getCopy(), startPose));
                }
            }
            if(c == 'p'){ System.out.printf(Locale.US, " Pose %d, %s %n", step, robotPose.toString()); }
            double v = !e.isShiftDown() ? 3.0 : 0.5;
            if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                if(!editingMode) {if(step < 99){step++; lastStep = true; }}else {
                    if(!e.isAltDown()) {
                        robotPose.add(new Pose(0, -v, 0));
                    }else{
                        robotPose.add(new Pose(0, 0, -v));
                    }
                }
            }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                if(!editingMode){if(step > 0){step--; lastStep = false; }}else {
                    if(!e.isAltDown()) {
                        robotPose.add(new Pose(0, v, 0));
                    }else{
                        robotPose.add(new Pose(0, 0, v));
                    }
                }
            }else if(e.getKeyCode() == KeyEvent.VK_UP){
                if(editingMode){
                    if(!e.isAltDown()) {
                        robotPose.add(new Pose(v, 0, 0));
                    }
                }
            }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                if(editingMode){
                    if(!e.isAltDown()) {
                        robotPose.add(new Pose(-v, 0, 0));
                    }
                }
            }
        };
    }

}

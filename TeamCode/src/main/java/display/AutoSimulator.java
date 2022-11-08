package display;

import auton.TerraAuto;
import autoutil.AutoFramework;
import geometry.framework.CoordinatePlane;
import geometry.position.Pose;

public class AutoSimulator extends Drawer{

    // TODO MAKE SIMULATION

    public static void main(String[] args) {
        setAuto(new TerraAuto(), new Pose(20,fieldSize/2.0,0));
        drawWindow(new AutoSimulator(), "Auto Simulator");
    }

    @Override
    public void define() {
        updateRobotPose(0,1,0);
        drawField(); drawPlane(autoPlane); drawPlane(robot);
    }




    private static CoordinatePlane autoPlane;
    private static CoordinatePlane robot;
    private static Pose startPose;
    private static final Pose robotPose = new Pose();

    private static void setAuto(AutoFramework auto, Pose startPose) {
        auto.setup();
        if(auto.isFlipped()){ startPose.scaleX(-1); startPose.translate(fieldSize, 0); startPose.rotateOrientation(180);}
        autoPlane = auto.getAutoPlane();
        convertToField(autoPlane, startPose);
        AutoSimulator.startPose = startPose;
        updateRobotPose(0,0,0);
    }

    public static void updateRobotPose(double dx, double dy, double dt){
        // convert to velocity?
        robotPose.translate(dx, dy);
        robotPose.rotate(dt);
        robot = getRobot(startPose, robotPose);
    }






}

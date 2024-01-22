package robotparts.sensors;

import org.openftc.easyopencv.OpenCvCameraRotation;

import autoutil.vision.Scanner;
import geometry.position.Pose;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.ICamera;

import static global.General.hardwareMap;

public class Cameras extends RobotPart {
    public ICamera cam;

    @Override
    public void init() {
        cam = create("ecam", ElectronicType.ICAMERA_EXTERNAL);
    }
    public void start(boolean view){ cam.start(view); }
    public void startAndResume(boolean view){ start(view); resume(); }
    public void pause(){ cam.pause(); }
    public void resume(){ cam.resume(); }
    public void setScanner(Scanner scanner){ cam.setScanner(scanner);}
    //public void startVuforia(boolean view){ cam.startVuforia(view); }
    //public boolean updateVuforia(){ return cam.updateVuforia(); }
    public Pose getPoseFromVuforia(){ return cam.getPose(); }
    @Override
    public void halt(){ cam.halt(); }
    public double getFPS(){ return cam.getFramesPerSecond(); }


    /**
     * Used to get the monitor view Id (To view what the camera is seeing)
     * @return monitor id
     */
    public static int getCameraMonitorViewId(){ return hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()); }

}

package robotparts.sensors;

import org.openftc.easyopencv.OpenCvCameraRotation;

import autoutil.vision.Scanner;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.ICamera;

import static global.General.hardwareMap;

public class Cameras extends RobotPart {
    private ICamera cam;

    @Override
    public void init() {
//        cam = create("ecam", ElectronicType.ICAMERA_EXTERNAL);
        cam = create("ecam", ElectronicType.ICAMERA_EXTERNAL_DISPLAY);
//        cam = createInternalCamera(OpenCvCameraRotation.UPRIGHT, false);
    }

    public void start(){ cam.start(false, 1); }
    public void pause(){ cam.pause(); }
    public void resume(){ cam.resume(); }
    public void setScanner(Scanner scanner){ cam.setScanner(scanner);}
    @Override
    public void halt(){ cam.halt(); }
    public double getFPS(){ return cam.getFramesPerSecond(); }

    /**
     * Used to get the monitor view Id (To view what the camera is seeing)
     * @return monitor id
     */
    public static int getCameraMonitorViewId(){ return hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()); }

}

package robotparts.sensors;

import org.openftc.easyopencv.OpenCvCameraRotation;

import autoutil.vision.Scanner;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.input.ICamera;

import static global.General.hardwareMap;

public class Cameras extends RobotPart {
    private ICamera ecam;
    private ICamera icam;

    @Override
    public void init() {
        ecam = create("ecam", ElectronicType.ICAMERA_EXTERNAL);
//        ecam = create("ecam", ElectronicType.ICAMERA_EXTERNAL_DISPLAY);
//        icam = createInternalCamera(OpenCvCameraRotation.UPRIGHT, false);
    }

    public void startExternalCamera(){ ecam.start(); }

    public void setExternalScanner(Scanner scanner){ecam.setScanner(scanner);}

    public void stopExternalCamera(){ ecam.halt(); }

    public double getExternalFPS(){ return ecam.getFramesPerSecond(); }

    public void startInternalCamera(){
        icam.start();
    }

    public void setInternalScanner(Scanner scanner){icam.setScanner(scanner);}

    public void stopInternalCamera(){ icam.halt(); }

    public double getInternalFPS(){
        return icam.getFramesPerSecond();
    }


    /**
     * Used to get the monitor view Id (To view what the camera is seeing)
     * @return monitor id
     */
    public static int getCameraMonitorViewId(){
        return hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    }

}

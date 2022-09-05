package robotparts.sensors;

import org.openftc.easyopencv.OpenCvCameraRotation;

import autoutil.vision.Scanner;
import robotparts.RobotPart;
import robotparts.electronics.input.ICamera;

public class Cameras extends RobotPart {
    private ICamera ecam;
    private ICamera icam;

    @Override
    public void init() {
        ecam = createExternalCamera("ecam", OpenCvCameraRotation.SIDEWAYS_RIGHT, false);
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

}

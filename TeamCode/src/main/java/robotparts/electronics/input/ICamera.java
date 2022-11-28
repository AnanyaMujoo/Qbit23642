package robotparts.electronics.input;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import autoutil.vision.Scanner;
import robotparts.Electronic;

public class ICamera extends Electronic {
    private final OpenCvInternalCamera camera;
    private final CameraType cameraType;
    private final OpenCvCameraRotation orientation;
    private final int width = 320;
    private final int height = 240;

    public ICamera(OpenCvInternalCamera cam, CameraType t, OpenCvCameraRotation rotation){
        this.camera =  cam;
        this.cameraType = t;
        this.orientation = rotation;
    }

    public void pause(){ camera.pauseViewport(); }
    public void resume(){ camera.resumeViewport(); }

    public void startStreaming(){ camera.startStreaming(width, height, orientation); }
    public void stopStreaming(){ camera.stopStreaming(); }

    public void setScanner(Scanner scanner){
        camera.setPipeline(scanner);
    }

    public void start(boolean flash, int zoom){
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener(){
            @Override
            public void onOpened() {
                camera.startStreaming(width, height, orientation);
                if(flash) { camera.setFlashlightEnabled(true);}
                if(zoom != 1){ camera.setZoom(zoom);}
                camera.pauseViewport();
            }
            @Override
            public void onError(int errorCode) {}
        });
    }

    public double getFramesPerSecond(){
        return camera.getFps();
    }

    @Override
    public void halt(){
        camera.closeCameraDevice();
    }

    public enum CameraType{
        INTERNAL,
        EXTERNAL
    }
}

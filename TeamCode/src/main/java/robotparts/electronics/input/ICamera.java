package robotparts.electronics.input;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

import autoutil.vision.Scanner;
import robotparts.Electronic;

public class ICamera extends Electronic {
    private final OpenCvCamera camera;
    private final CameraType cameraType;
    private final OpenCvCameraRotation orientation;
    private final int width = 320;
    private final int height = 240;

    public ICamera(OpenCvCamera cam, CameraType t, OpenCvCameraRotation rotation){
        this.camera =  cam;
        this.cameraType = t;
        this.orientation = rotation;
    }

    public void startStreaming(){ camera.startStreaming(width, height, orientation); }
    public void stopStreaming(){ camera.stopStreaming(); }

    public void setScanner(Scanner scanner){
        camera.setPipeline(scanner);
    }

    public void start(){
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener(){
            @Override
            public void onOpened() {camera.startStreaming(width, height, orientation); }
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

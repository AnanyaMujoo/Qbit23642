package robotparts.electronics.input;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import autoutil.vision.Scanner;
import robotparts.Electronic;

public class ICamera extends Electronic {
    /**
     * Internal camera object
     */
    private final OpenCvCamera camera;
    /**
     * Type of camera
     */
    private final CameraType cameraType;
    /**
     * Orientation of camera
     */
    private final OpenCvCameraRotation orientation;
    /**
     * Width of stream
     */
    private final int width = 320;
    /**
     * Height of stream
     */
    private final int height = 240;


    /**
     * Constructor
     * @param cam
     * @param t
     * @param rotation
     */
    public ICamera(OpenCvCamera cam, CameraType t, OpenCvCameraRotation rotation){
        this.camera =  cam;
        this.cameraType = t;
        this.orientation = rotation;
    }

    /**
     * Pause the viewport
     */
    public void pause(){ camera.pauseViewport(); }

    /**
     * Resume/start the viewport
     */
    public void resume(){ camera.resumeViewport(); }

    /**
     * Set the scanner for the camera
     * @param scanner
     */
    public void setScanner(Scanner scanner){ camera.setPipeline(scanner); }

    /**
     * Start the camera (with true for viewing)
     * @param view
     */
    public void start(boolean view){
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener(){
            @Override
            public void onOpened() {
                camera.startStreaming(width, height, orientation);
                if(!view) {
                    pause();
                }
            }
            @Override
            public void onError(int errorCode) {}
        });
    }

    /**
     * Get frames per second
     * @return FPS
     */
    public double getFramesPerSecond(){
        return camera.getFps();
    }

    /**
     * Stop the camera
     */
    @Override
    public void halt(){
        camera.closeCameraDevice();
    }

    /**
     * Type of camera
     */
    public enum CameraType{
        INTERNAL,
        EXTERNAL
    }
}

package robotparts.electronics.input;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;
import java.util.List;

import autoutil.generators.Generator;
import autoutil.vision.Scanner;
import elements.Field;
import geometry.framework.Point;
import geometry.position.Pose;
import global.General;
import robotparts.Electronic;
import util.template.Iterator;

import static global.Constants.VUFORIA_KEY;
import static global.Constants.VUFORIA_TARGET_HEIGHT_CM;
import static global.General.cameraMonitorViewId;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

public class ICamera extends Electronic {

    /**
     * Internal camera object
     */
    private OpenCvCamera camera;

    private WebcamName name;
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

//    private VuforiaLocalizer vuforia;
//    private VuforiaTrackables targets;
//    private ArrayList<VuforiaTrackable> allTrackables = new ArrayList<>();


    private final Point cameraLocation = new Point(10,-20); // cm
    private final double cameraHeightFromField = 15; // cm

    private OpenGLMatrix lastLocation;
    private Pose currentPose = new Pose();


    /**
     * Constructor
     * @param cam
     * @param t
     * @param rotation
     */

    public ICamera(WebcamName cam, CameraType t, OpenCvCameraRotation rotation){
        this.camera = OpenCvCameraFactory.getInstance().createWebcam(cam, cameraMonitorViewId);
        name = cam;
        this.cameraType = t;
        this.orientation = rotation;
    }

    public ICamera(CameraType t, OpenCvCameraRotation rotation){
        this.camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
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
                //camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                if(cameraType.equals(CameraType.INTERNAL)) {
                    camera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
                }
                camera.startStreaming(width, height, orientation);
                if(!view) {
                    pause();
                }
            }
            @Override
            public void onError(int errorCode) {}
        });
    }


//    public void startVuforia(boolean view){
//        VuforiaLocalizer.Parameters parameters;
//        if(view){
//            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
//            camera = OpenCvCameraFactory.getInstance().createVuforiaPassthrough(vuforia, parameters, cameraMonitorViewId);
//        }else {
//            parameters = new VuforiaLocalizer.Parameters();
//            camera = OpenCvCameraFactory.getInstance().createVuforiaPassthrough(vuforia, parameters);
//        }
//
//        parameters.vuforiaLicenseKey = VUFORIA_KEY;
//        parameters.cameraName = name;
//        parameters.useExtendedTracking = false;
//
//        vuforia = ClassFactory.getInstance().createVuforia(parameters);
//        targets = this.vuforia.loadTrackablesFromAsset("PowerPlay");
//
//        allTrackables = new ArrayList<>();
//        allTrackables.addAll(targets);

    float halfField = (float) (Field.width *10*0.5);
    float oneAndHalfTile = (float) (Field.tileWidth*10*1.5);
    float mmTargetHeight = (float) (VUFORIA_TARGET_HEIGHT_CM*10);
//        identifyTarget(0, "Red Audience Wall",   -halfField,  -oneAndHalfTile, mmTargetHeight, 90, 0,  90);
//        identifyTarget(1, "Red Rear Wall",        halfField,  -oneAndHalfTile, mmTargetHeight, 90, 0, -90);
//        identifyTarget(2, "Blue Audience Wall",  -halfField,   oneAndHalfTile, mmTargetHeight, 90, 0,  90);
//        identifyTarget(3, "Blue Rear Wall",       halfField,   oneAndHalfTile, mmTargetHeight, 90, 0, -90);

    OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
            .translation((float) cameraLocation.getY(), (float) -cameraLocation.getX(), (float) cameraHeightFromField)
            .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));

//        Iterator.forAll(allTrackables, trackable -> ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(name, cameraLocationOnRobot));
//
//        targets.activate();

//        start(view);
//    }

//    public boolean updateVuforia(){
//        boolean targetVisible = false;
//
//        for (VuforiaTrackable trackable : allTrackables) {
//            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
////                telemetry.addData("Visible Target", trackable.getName());
//                targetVisible = true;
//                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
//                if (robotLocationTransform != null) {
//                    lastLocation = robotLocationTransform;
//                }
//                break;
//            }
//        }

//        if (targetVisible) {
//            VectorF translation = lastLocation.getTranslation();
//            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
//            currentPose = new Pose(translation.get(0)/10.0, translation.get(1)/10.0, rotation.thirdAngle);
////            telemetry.addData("Pos (inches)", "{X, Y, Z} = %.1f, %.1f, %.1f",
////                    translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);
////            telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
//        }
//        else {
////            telemetry.addData("Visible Target", "none");
//        }
//        return targetVisible;
//    }

    public Pose getPose(){
        return currentPose;
    }






    /***
     * Identify a target by naming it, and setting its position and orientation on the field
     * @param targetIndex
     * @param targetName
     * @param dx, dy, dz  Target offsets in x,y,z axes
     * @param rx, ry, rz  Target rotations in x,y,z axes
     */
//    public void identifyTarget(int targetIndex, String targetName, float dx, float dy, float dz, float rx, float ry, float rz) {
//        VuforiaTrackable aTarget = targets.get(targetIndex);
//        aTarget.setName(targetName);
//        aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz)
//                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
//    }

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
        camera.closeCameraDeviceAsync(new OpenCvCamera.AsyncCameraCloseListener() {
            @Override
            public void onClose() {}
        });

    }

    /**
     * Type of camera
     */
    public enum CameraType{
        INTERNAL,
        EXTERNAL
    }
}
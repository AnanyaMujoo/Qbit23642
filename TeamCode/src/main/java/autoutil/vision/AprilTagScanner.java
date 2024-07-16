package autoutil.vision;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Autonomous
public abstract class AprilTagScanner extends LinearOpMode{
    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag = null;

    private VisionPortal visionPortal;
    private void initAprilTag(){
        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder()
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();

        //aprilTag.setDecimation(3)


        visionPortal = new VisionPortal.Builder()
                .addProcessor(aprilTag)
                .setCamera(hardwareMap.get(WebcamName.class, "ecam"))
                .build();

    }
public void findAprilTag() {

    initAprilTag();
    setManualExposure(6,250);
    waitForStart();
    while (opModeIsActive()) {
        List<AprilTagDetection> detectedTags = aprilTag.getDetections();
        for (AprilTagDetection dtag : detectedTags) {
            if (dtag.metadata != null) {
                telemetry.addData("x", dtag.ftcPose.x);
                telemetry.addData("y", dtag.ftcPose.y);
                telemetry.addData("z", dtag.ftcPose.z);
                telemetry.addData("Range", desiredTag.ftcPose.range);
                telemetry.addData("Bearing", desiredTag.ftcPose.bearing);
                telemetry.addData("Yaw", desiredTag.ftcPose.yaw);
            } else {
                telemetry.addData("error", "unknown tag- metadata is null");
            }

        }
    }
}
    private void  setManualExposure( int exposureMS, int gain) {
        // Wait for the camera to be open, then use the controls

        if (visionPortal == null) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                sleep(20);
            }
            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }

}
}

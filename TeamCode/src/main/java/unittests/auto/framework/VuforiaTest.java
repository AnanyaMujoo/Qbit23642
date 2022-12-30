package unittests.auto.framework;

import geometry.position.Pose;
import unittests.auto.AutoUnitTest;

import static global.General.log;

public class VuforiaTest extends AutoUnitTest {


    @Override
    public void init() {
        camera.startVuforia(true);
    }

    @Override
    protected void run() {
        boolean targetVisible = camera.updateVuforia();
        Pose robotPose = camera.getPoseFromVuforia();
        log.show("Target Visible", targetVisible);
        log.show("Robot Pose", robotPose);
    }
}

package unittests.tele.hardware;

import autoutil.vision.CaseScanner;
import unittests.tele.TeleUnitTest;

import static global.General.log;

public class InternalCameraTest extends TeleUnitTest {
    /**
     * Scanner for team element
     */
    CaseScanner teamElementScanner = new CaseScanner();

    /**
     * Team element scanner setting and starting
     */
    @Override
    public void start() {
//        camera.setInternalScanner(teamElementScanner);
//        camera.startInternalCamera();
    }

    /**
     * Loop through and log fps
     */
    @Override
    protected void loop() {
//        log.show("Internal Camera FPS", camera.getInternalFPS());
        log.show("Detected Case", teamElementScanner.getCase());
    }

    /**
     * Stop internal camera
     */
    @Override
    public void stop() {
//        camera.stopInternalCamera();
    }
}

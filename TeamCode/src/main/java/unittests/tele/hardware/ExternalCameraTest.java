package unittests.tele.hardware;

import autoutil.vision.CaseScanner;
import unittests.tele.TeleUnitTest;

import static global.General.log;

public class ExternalCameraTest extends TeleUnitTest {
    /**
     * Scanner for team element
     */
    CaseScanner teamElementScanner = new CaseScanner();


    /**
     * Team element scanner setting and starting
     */
    @Override
    public void start() {
        camera.setExternalScanner(teamElementScanner);
        camera.startExternalCamera();
    }

    /**
     * Loop through and log fps
     */
    @Override
    protected void loop() {
        log.show("External Camera FPS", camera.getInternalFPS());
        log.show("Detected Case", teamElementScanner.getCase());
    }

    /**
     * Stop external camera
     */
    @Override
    public void stop() {
        camera.stopExternalCamera();
    }
}

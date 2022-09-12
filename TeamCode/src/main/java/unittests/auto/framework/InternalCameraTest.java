package unittests.auto.framework;

import autoutil.vision.TeamElementScanner;
import unittests.auto.AutoUnitTest;

import static global.General.bot;
import static global.General.log;

public class InternalCameraTest extends AutoUnitTest {
    /**
     * Scanner for team element
     */
    TeamElementScanner teamElementScanner = new TeamElementScanner();

    /**
     * Team element scanner setting and starting
     */
    @Override
    public void init() {
        bot.camera.setInternalScanner(teamElementScanner);
        bot.camera.startInternalCamera();
    }

    /**
     * Loop through and log fps
     */
    @Override
    protected void run() {
        whileActive(() -> {
            log.show("Internal Camera FPS", bot.camera.getInternalFPS());
            log.show("Detected Case", teamElementScanner.getCase());
        });
    }

    /**
     * Stop internal camera
     */
    @Override
    public void stop() {
        bot.camera.stopInternalCamera();
    }
}

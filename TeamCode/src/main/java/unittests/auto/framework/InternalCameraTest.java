package unittests.auto.framework;

import autoutil.vision.TeamElementScanner;
import unittests.auto.AutoUnitTest;

import static global.General.bot;
import static global.General.log;

public class InternalCameraTest extends AutoUnitTest {

    TeamElementScanner teamElementScanner = new TeamElementScanner();

    @Override
    public void init() {
        bot.camera.setInternalScanner(teamElementScanner);
        bot.camera.startInternalCamera();
    }

    @Override
    protected void run() {
        whileActive(() -> {
            log.show("Internal Camera FPS", bot.camera.getInternalFPS());
            log.show("Detected Case", teamElementScanner.getCase());
        });
    }

    @Override
    public void stop() {
        bot.camera.stopInternalCamera();
    }
}

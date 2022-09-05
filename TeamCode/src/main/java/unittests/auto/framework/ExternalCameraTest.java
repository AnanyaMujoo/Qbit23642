package unittests.auto.framework;

import autoutil.vision.TeamElementScanner;
import unittests.auto.AutoUnitTest;

import static global.General.bot;
import static global.General.log;

public class ExternalCameraTest extends AutoUnitTest {
    TeamElementScanner teamElementScanner = new TeamElementScanner();

    @Override
    public void init() {
        bot.camera.setExternalScanner(teamElementScanner);
        bot.camera.startExternalCamera();
    }

    @Override
    protected void run() {
        whileActive(() -> {
            log.show("External Camera FPS", bot.camera.getInternalFPS());
        });
    }

    @Override
    public void stop() {
        bot.camera.stopExternalCamera();
    }
}

package unittests.tele.sensor;

import autoutil.vision.JunctionScanner;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;

import static global.General.gph1;

public class JunctionScannerTest extends TeleUnitTest {

    private final JunctionScanner junctionScanner = new JunctionScanner();

    @Override
    public void init() {
        camera.setExternalScanner(junctionScanner);
        camera.startExternalCamera();
        gph1.link(Button.B, camera::showExternalCamera);
        gph1.link(Button.A, camera::hideExternalCamera);
    }

    @Override
    protected void loop() {

    }

    @Override
    public void stop() {
        camera.stopExternalCamera();
    }
}

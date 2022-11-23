package unittests.tele.sensor;

import autoutil.vision.JunctionScanner;
import geometry.position.Vector;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;

import static global.General.gph1;
import static global.General.log;

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

        junctionScanner.message();
        double herr = junctionScanner.angleToJunction;
        double derr = 14-junctionScanner.distanceToJunction;
        double dpow = derr/30;
        double hpow = herr/90;

        Vector pow = new Vector(0, Math.abs(derr) < 40 ? dpow : 0);
        pow.rotate(-herr);
        drive.move(pow.getY(), pow.getX(), Math.abs(herr) < 45 ? hpow : 0);
    }

    @Override
    public void stop() {
        camera.stopExternalCamera();
    }
}

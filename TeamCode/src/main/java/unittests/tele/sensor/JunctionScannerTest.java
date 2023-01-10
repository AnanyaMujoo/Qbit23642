package unittests.tele.sensor;

import autoutil.vision.JunctionScanner;
import autoutil.vision.JunctionScannerAll;
import geometry.position.Pose;
import geometry.position.Vector;
import math.polynomial.Linear;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;
import util.template.Precision;

import static global.General.gph1;

public class JunctionScannerTest extends TeleUnitTest {

    private final Pose target = new Pose(0,18.5, -5);
    private final JunctionScannerAll junctionScanner = new JunctionScannerAll();

    @Override
    public void init() {
        camera.setScanner(junctionScanner);
        camera.start(true);
        JunctionScannerAll.resume();
//        gph1.link(Button.RIGHT_BUMPER, camera::resume);
//        gph1.link(Button.LEFT_BUMPER, camera::pause);
        gph1.link(Button.B, BackwardAllTele);
        gph1.link(Button.Y, ForwardAll);
    }

    @Override
    protected void loop() {
        junctionScanner.message();
//
//        Linear yCurve = new Linear(0.018, 0.06);
//        Linear hCurve = new Linear(0.007, 0.04);
//
//
//        Pose error = target.getSubtracted(junctionScanner.getPose());
//        error.invertOrientation();
//
//        if(Math.abs(error.getY()) > 15 || Math.abs(error.getAngle()) > 15){ error = new Pose(); }
//
//        Vector pow = new Vector(0, yCurve.fodd(error.getY()));
//        pow.rotate(-error.getAngle());
//        double h = hCurve.fodd(error.getAngle());
//
//        drive.move(Precision.clip(pow.getY(), 0.5) + (gph1.ry*0.5),  (gph1.rx*0.5), Precision.clip(h, 0.5) + (gph1.lx*0.5));
//
//        lift.move(0);

    }

    @Override
    public void stop() {
        camera.halt();
    }
}

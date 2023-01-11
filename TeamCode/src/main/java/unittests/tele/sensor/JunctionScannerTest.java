package unittests.tele.sensor;

import autoutil.vision.JunctionScanner;
import autoutil.vision.JunctionScannerAll;
import geometry.framework.CoordinatePlane;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Modes;
import math.linearalgebra.Vector3D;
import math.polynomial.Linear;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;
import util.template.Precision;

import static global.General.gph1;
import static global.General.log;
import static global.Modes.AttackMode.STICKY;

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


    private Pose startOdometryPose = new Pose();
    private Pose startJunctionPose = new Pose();
    private Pose lastError = new Pose();

    @Override
    protected void loop() {
        junctionScanner.message();

        Vector3D attackPow = new Vector3D();
        Linear yCurve = new Linear(0.025, 0.06);
        Linear hCurve = new Linear(0.008, 0.04);
        Pose error = JunctionScannerAll.getError();

//        if(!error.equals(new Pose())
////                && ((lastError.getDistanceTo(error) < 2 && Math.abs(lastError.getAngle() - error.getAngle()) < 2) || lastError.equals(new Pose()))
//                ){
//            startOdometryPose = odometry.getPose().getCopy();
//            startJunctionPose = JunctionScannerAll.getPose().getCopy();
//        }
        startOdometryPose = odometry.getPose().getCopy();
        startJunctionPose = JunctionScannerAll.getPose().getCopy();

        log.show("Start Odometry Pose" , startOdometryPose);
        log.show("OdometryPose" , odometry.getPose());
        CoordinatePlane plane = new CoordinatePlane(odometry.getPose().getCopy());
        plane.setStartInverse(startOdometryPose);
        Pose currentPose = plane.getPoses().get(0).getCopy();
        currentPose = currentPose.getOnlyPointRotated(startJunctionPose.getAngle());
        log.show("Start Junction Pose" , startJunctionPose);
        log.show("CurrentPose" , currentPose);

        currentPose = currentPose.getAdded(startJunctionPose);
//        currentPose.invertOrientation();
//        log.show(currentPose);
        error = JunctionScannerAll.target.getSubtracted(currentPose);
        error.invertOrientation();

        lastError = error.getCopy();

        log.show("CurrentError" , error);

        if(gph1.ry > 0 || gph1.rx > 0 || gph1.lx > 0){
            error = new Pose();
            lastError = new Pose();
        }

        Vector junctionPow = new Vector(0, yCurve.fodd(error.getY()));
        junctionPow.rotate(-error.getAngle()); junctionPow.limitLength(0.4);
        attackPow = new Vector3D(junctionPow, Precision.clip(hCurve.fodd(error.getAngle()), 0.2));

        Pose power = drive.getMoveSmoothPower(gph1.ry, gph1.rx, gph1.lx);
        drive.move(attackPow.getY() + power.getX(), power.getY(), attackPow.getZ() + power.getAngle());


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

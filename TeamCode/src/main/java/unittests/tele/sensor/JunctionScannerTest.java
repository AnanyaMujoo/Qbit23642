package unittests.tele.sensor;

import autoutil.vision.JunctionScannerAll;
import geometry.framework.CoordinatePlane;
import geometry.position.Pose;
import geometry.position.Vector;
import math.linearalgebra.Vector3D;
import math.polynomial.Linear;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;
import util.template.Precision;

import static global.General.gph1;

public class JunctionScannerTest extends TeleUnitTest {

//    private final Pose target = new Pose(0,18.5, -5);
    private final JunctionScannerAll junctionScanner = new JunctionScannerAll();

    @Override
    public void init() {
        camera.setScanner(junctionScanner);
        camera.start(true);
        JunctionScannerAll.resume();
//        gph1.link(Button.B, BackwardAllTele);
//        gph1.link(Button.Y, ForwardAll);
    }


    private Pose startOdometryPose = new Pose();
    private Pose startJunctionPose = new Pose();
    private Pose startTarget = JunctionScannerAll.getTarget();
    private final Linear yCurve = new Linear(0.022, 0.05);
    private final Linear hCurve = new Linear(0.004, 0.05);
    private Pose error = new Pose();
    private Pose lastError = new Pose();
    private boolean tracking = false;

    @Override
    protected void loop() {
        junctionScanner.message();
//
//        drive.moveSmooth(gph1.ry, gph1.rx, gph1.lx);
//
//        lift.move(0);

//        log.show("Start Odometry Pose" , startOdometryPose);
//        log.show("OdometryPose" , odometry.getPose());

//        log.show("Start Junction Pose" , startJunctionPose);
//        log.show("CurrentPose" , currentPose);

//        log.show("CurrentError" , error);
        //                    && error.withinY(new Pose(), 2, 2) &&

//        Vector3D attackPow = new Vector3D();
//        Pose currentPose = JunctionScannerAll.getPose();
//        boolean inRange = currentPose.within(startTarget, 15, 30);
//        if(inRange){ tracking = true; }
//        if(tracking) {
//            if (inRange && lastError.withinY(error, 2, 2)) {
//                startOdometryPose = odometry.getPose();
//                startJunctionPose = currentPose.getCopy();
//                startTarget = JunctionScannerAll.getTarget();
//            }
//            if(!startOdometryPose.equals(new Pose())) {
//                currentPose = CoordinatePlane.applyCoordinateTransform(odometry.getPose(), p -> p.setStartInverse(startOdometryPose)).getOnlyPointRotated(startJunctionPose.getAngle()).getAdded(startJunctionPose);
//                error = startTarget.getSubtracted(currentPose).getOrientationInverted();
//                lastError = error.getCopy();
//
//                if(Math.abs(gph1.ry) > 0.7 || Math.abs(gph1.rx) > 0.7 || Math.abs(gph1.lx) > 0.7){ tracking = false; }
//
//                Vector junctionPow = new Vector(0, yCurve.fodd(error.getY()));
//                junctionPow.rotate(-error.getAngle()); junctionPow.limitLength(0.3);
//                attackPow = new Vector3D(junctionPow, Precision.clip(hCurve.fodd(error.getAngle()), 0.2));
//            }
//        }else{
//            startOdometryPose = new Pose();
//            startJunctionPose = new Pose();
//            error = new Pose();
//            lastError = new Pose();
//            startTarget = JunctionScannerAll.getTarget();
//        }
//
//        log.show("Tracking", tracking);
//        log.show("Set", !startOdometryPose.equals(new Pose()));
//        log.show("Error", error);

//        Pose power = drive.getMoveSmoothPower(gph1.ry, gph1.rx, gph1.lx);
//        drive.move(attackPow.getY() + power.getX(), attackPow.getX() + power.getY(), attackPow.getZ() + power.getAngle());
//
//        lift.move(0);


    }

    @Override
    public void stop() {
        camera.halt();
    }
}

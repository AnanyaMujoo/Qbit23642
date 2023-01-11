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

//    private final Pose target = new Pose(0,18.5, -5);
    private final JunctionScannerAll junctionScanner = new JunctionScannerAll();

    @Override
    public void init() {
        camera.setScanner(junctionScanner);
        camera.start(true);
        JunctionScannerAll.resume();
        gph1.link(Button.B, BackwardAllTele);
        gph1.link(Button.Y, ForwardAll);
    }


    private Pose startOdometryPose = new Pose();
    private Pose startJunctionPose = new Pose();
    private Pose startTarget = JunctionScannerAll.getTarget();
    private final Linear yCurve = new Linear(0.009, 0.07);
    private final Linear hCurve = new Linear(0.002, 0.05);
    private Pose error = new Pose();
    private boolean tracking = false;

    @Override
    protected void loop() {
        junctionScanner.message();

//        log.show("Start Odometry Pose" , startOdometryPose);
//        log.show("OdometryPose" , odometry.getPose());

//        log.show("Start Junction Pose" , startJunctionPose);
//        log.show("CurrentPose" , currentPose);

//        log.show("CurrentError" , error);

        Vector3D attackPow = new Vector3D();
        Pose currentPose = JunctionScannerAll.getPose();
        boolean inRange = currentPose.within(startTarget, 20, 40);
        if(inRange){ tracking = true; }
        if(tracking) {
            if (inRange && error.within(new Pose(), 2, 3)) {
                startOdometryPose = odometry.getPose();
                startJunctionPose = currentPose.getCopy();
                startTarget = JunctionScannerAll.getTarget();
            }
            if(!startOdometryPose.equals(new Pose())) {
                currentPose = CoordinatePlane.applyCoordinateTransform(odometry.getPose(), p -> p.setStartInverse(startOdometryPose)).getOnlyPointRotated(startJunctionPose.getAngle()).getAdded(startJunctionPose);
                error = startTarget.getSubtracted(currentPose).getOrientationInverted();

                if(gph1.ry > 0.9){ tracking = false; }

                Vector junctionPow = new Vector(0, yCurve.fodd(error.getY()));
                junctionPow.rotate(-error.getAngle()); junctionPow.limitLength(0.25);
                attackPow = new Vector3D(junctionPow, Precision.clip(hCurve.fodd(error.getAngle()), 0.2));
            }
        }else{
            startOdometryPose = new Pose();
            startJunctionPose = new Pose();
            error = new Pose();
            startTarget = JunctionScannerAll.getDefaultTarget();
        }

        Pose power = drive.getMoveSmoothPower(gph1.ry, gph1.rx, gph1.lx);
        drive.move(attackPow.getY() + power.getX(), attackPow.getX() + power.getY(), attackPow.getZ() + power.getAngle());


    }

    @Override
    public void stop() {
        camera.halt();
    }
}

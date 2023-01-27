package unittests.auto.framework.movement;

import java.util.ArrayList;

import geometry.position.Pose;
import geometry.position.Vector;
import math.polynomial.Linear;
import unittests.auto.AutoUnitTest;
import util.template.Precision;

import static global.General.gph1;
import static global.General.log;

public class DistanceSensorStraightenTest extends AutoUnitTest {

    public static final Pose odometryCenterToDistanceSensor = new Pose(-14,6,0);

    // TODO TEST
    @Override
    protected void run() {
        final double turn = 0.2;
        distanceSensors.ready();

        ArrayList<Double> distances = new ArrayList<>();
        distances.add(getDis());
        distances.add(getDis());
        whileActive(() -> distances.get(distances.size() - 2) <= distances.get(distances.size()-1), () -> {
            distances.add(getDis());
            drive.move(0,0,turn);
        });
        drive.halt();

        whileActive(() -> {
            log.show("Distances", distances);
        });

//
//        Double[] startDistance = new Double[]{distanceSensors.getRightDistance()*scale};
//        Pose[] startPose = new Pose[]{odometry.getPose().getAdded(odometryCenterToDistanceSensor.getOnlyPointRotated(odometry.getHeading()))};
//        whileActive( () -> {
//            double currentDistance = distanceSensors.getRightDistance()*scale;
//            if(!Precision.difference(startDistance[0], currentDistance, 1)){
//                Pose endPose = odometry.getPose().getAdded(odometryCenterToDistanceSensor.getOnlyPointRotated(odometry.getHeading()));
//
//                double theta = endPose.getAngle() - startPose[0].getAngle();
//                Vector odometryDelta = endPose.getSubtracted(startPose[0]).getVector().getRotated(-startPose[0].getAngle() - 90);
//                Vector total = new Vector(0, -startDistance[0]).getAdded(odometryDelta).getAdded(new Vector(theta + 90).getScaled(currentDistance));
//                double currentHeading = -total.getTheta() + theta;
//                gyro.setHeading(currentHeading);
//                startPose[0] = endPose.getCopy();
//                startDistance[0] = currentDistance;
//            }
//            drive.move(0, 0, turn * Math.signum(gyro.getHeading()));
//
//
//        });


//
//
//
//        double startDistance = distanceSensors.getRightDistance() * scale;
//        Pose startPose = odometry.getPose().getAdded(odometryCenterToDistanceSensor.getOnlyPointRotated(odometry.getHeading()));
//        whileActive(() -> Precision.difference(distanceSensors.getRightDistance() * scale, startDistance, 3), () -> {
//            drive.move(0, 0, turn);
//        });
//        drive.halt();
//        pause(0.1);
//        Pose endPose = odometry.getPose().getAdded(odometryCenterToDistanceSensor.getOnlyPointRotated(odometry.getHeading()));
//        double endDistance = distanceSensors.getRightDistance() * scale;
//
//        double theta = endPose.getAngle() - startPose.getAngle();
//        Vector odometryDelta = endPose.getSubtracted(startPose).getVector().getRotated(-startPose.getAngle() - 90);
//        Vector total = new Vector(0, -startDistance).getAdded(odometryDelta).getAdded(new Vector(theta + 90).getScaled(endDistance));
//        double currentHeading = -total.getTheta() + theta;
//        gyro.setHeading(currentHeading);
//
//        pause(0.1);
//        whileActive(() -> !Precision.range(gyro.getHeading(), 0.3), () -> {
//            drive.move(0, 0, turn * Math.signum(gyro.getHeading()));
//        });
//        drive.halt();




//        whileActive(() -> {
//            log.show("StartPose", startPose);
//            log.show("EndPose", endPose);
//            log.show("StartDis", startDistance);
//            log.show("EndDis", endDistance);
//            log.show("Theta", theta);
//            log.show("OdometryDelta", odometryDelta);
//            log.show("Total", total);
//            log.show("Total Theta", total.getTheta());
//            log.show("CurrentHeading", currentHeading);
//            log.show("NowHeading", gyro.getHeading());
//        });




//        double initialDistance2 = currentDistance[0];
//        double initialHeading = gyro.getHeading();
//        Double[] currentDistance2 = {initialDistance2};
//        whileActive(() -> currentDistance2[0] < initialDistance2, () -> {
//            turn(startPose, turn * Math.signum(initialDistance - initialDistance2));
//            currentDistance2[0] = distanceSensors.getRightDistance();
//        });
//        drive.halt();
//        double finalHeading = gyro.getHeading();
//        double halfHeading = (finalHeading - initialHeading)/2.0;
//        gyro.setHeading(halfHeading);
//        pause(0.2);
//        Pose startPose2 = odometry.getPose();
//        whileActive(() -> Precision.range(gyro.getHeading(), 1), () -> {
//            turn(startPose2, turn*Math.signum(gyro.getHeading()));
//        });
    }

    public void turn(Pose startPose, double pow){
        Pose dsStartPose = startPose.getAdded(odometryCenterToDistanceSensor.getOnlyPointRotated(startPose.getAngle()));
        Pose currentPose = odometry.getPose();
        Pose dsCurrentPose = currentPose.getAdded(odometryCenterToDistanceSensor.getOnlyPointRotated(currentPose.getAngle()));
        Pose error = dsStartPose.getSubtracted(dsCurrentPose);
        Vector power = error.getVector().getUnitVector().getScaled(0.1);
        drive.move(power.getY(), power.getX(), pow);
    }

    public double getDis(){
        double scale = 1.061224;
        return distanceSensors.getRightDistance()*scale;
    }
}

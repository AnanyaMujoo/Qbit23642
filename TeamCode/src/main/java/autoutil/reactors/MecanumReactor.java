package autoutil.reactors;

import autoutil.generators.Generator;
import autoutil.generators.PoseGenerator;
import geometry.position.Pose;
import robotparts.sensors.GyroSensors;

import static global.General.bot;
import static global.General.log;

public abstract class MecanumReactor extends Reactor {

    @Override
    public void init() {
        movementController.setProcessVariable(() -> getPose().getX(), () -> getPose().getY());
        headingController.setProcessVariable(() -> getPose().getAngle());
        headingController.setProcessError(() -> processThetaError(headingController.getRawError()));
        nextTarget();
    }

    @Override
    public Pose getPose() { return odometry.getPose(); }

    @Override
    public void setTarget(Pose pose) {
        movementController.setTarget(pose.getPoint());
        headingController.setTarget(pose.getAngle());
    }

    @Override
    public void nextTarget() {
        movementController.reset();
        headingController.reset();
    }

    @Override
    public boolean isAtTarget() { return movementController.isAtTarget() && headingController.isAtTarget(); }

    @Override
    public void moveToTarget(PoseGenerator generator) {
        movementController.update(getPose(), generator);
        headingController.update(getPose(), generator);
        drive.move(movementController.getOutputY(), movementController.getOutputX(), headingController.getOutput());
    }

    public static double processThetaError(double error){
        while (error < -180) { error += 360; }
        while (error > 180) { error -= 360; }
        return error;
    }
}
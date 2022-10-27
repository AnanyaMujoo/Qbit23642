package autoutil.reactors;

import geometry.position.Pose;
import robotparts.sensors.GyroSensors;

import static global.General.bot;
import static global.General.log;

public abstract class MecanumReactor extends Reactor {

    @Override
    public void init() {
        movementController.setProcessVariable(() -> getPose().getX(), () -> getPose().getY());
        headingController.setProcessVariable(() -> getPose().getAngle());
        headingController.setProcessError(() -> GyroSensors.processThetaError(headingController.getRawError()));
        nextTarget();
    }

    @Override
    public Pose getPose() {
        return new Pose(bot.mecanumOdometry.getPose());
    }

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
    public boolean isAtTarget() {
        return movementController.isAtTarget() && headingController.isAtTarget();
    }

    @Override
    public void moveToTarget() {
        movementController.update(getPose(), pathSegment);
        headingController.update(getPose(), pathSegment);

        bot.mecanumDrive.move(movementController.getOutputY(), movementController.getOutputX(), headingController.getOutput());
        log.show("Ypow", movementController.getOutputY());
        log.show("errr", movementController.yController.getError());
//        log.show("yPID state (Err, Int, Der)", Arrays.toString(controllers.get(1).getErrorState()));
//        log.show("xPID state (Err, Int, Der)", Arrays.toString(controllers.get(0).getErrorState()));
//        log.show("hPID state (Err, Int, Der)", Arrays.toString(controllers.get(2).getErrorState()));
    }
}
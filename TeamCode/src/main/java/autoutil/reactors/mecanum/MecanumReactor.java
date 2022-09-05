package autoutil.reactors.mecanum;

import java.util.Arrays;

import autoutil.reactors.Reactor;
import geometry.position.Point;
import geometry.position.Pose;
import geometry.position.Vector2;
import robotparts.sensors.GyroSensors;

import static global.General.bot;
import static global.General.log;

public abstract class MecanumReactor extends Reactor {

    @Override
    public void init() {
        movementController.setProcessVariable(() -> getPose().p.x, () -> getPose().p.y);
        headingController.setProcessVariable(() -> getPose().ang);
//        headingController.setProcessError(() -> GyroSensors.processThetaError(headingController.getRawError()));
        nextTarget();
    }

    @Override
    public Pose getPose() {
        return new Pose(bot.odometry.getPose());
    }

    @Override
    public void setTarget(double[] target) {
        movementController.setTarget(target);
        headingController.setTarget(target[2]);
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

        bot.drive.move(movementController.getOutputY(), movementController.getOutputX(), headingController.getOutput());
        log.show("Ypow", movementController.getOutputY());
        log.show("errr", movementController.yController.getError());
//        log.show("yPID state (Err, Int, Der)", Arrays.toString(controllers.get(1).getErrorState()));
//        log.show("xPID state (Err, Int, Der)", Arrays.toString(controllers.get(0).getErrorState()));
//        log.show("hPID state (Err, Int, Der)", Arrays.toString(controllers.get(2).getErrorState()));
    }
}
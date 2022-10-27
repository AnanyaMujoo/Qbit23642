package unittests.tele.framework.movement;

import java.util.Arrays;

import automodules.AutoModule;
import automodules.stage.Exit;
import automodules.stage.Main;
import automodules.stage.Stage;
import geometry.position.Vector;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.gph1;
import static global.General.log;

public class OdometryTest extends TeleUnitTest {
    // TODO FINISH


    @Override
    protected void start() {
        gph1.link(Button.RIGHT_BUMPER, new AutoModule(new Stage(
                drive.usePart(),
                new Main(() -> {
                    double hPow = -twoOdometry.getHeading()*0.001 - Math.signum(twoOdometry.getX())*0.02;
                    double xPow = -twoOdometry.getX()*0.05 - Math.signum(twoOdometry.getX())*0.02;
                    double yPow = -twoOdometry.getY()*0.05 - Math.signum(twoOdometry.getY())*0.02;
                    Vector powerVector = new Vector(xPow, yPow).getRotated(-twoOdometry.getHeading()).getScaled(1);
                    drive.move(powerVector.getY(), powerVector.getX(), hPow);
                }),
                new Exit(() -> Math.abs(twoOdometry.getHeading()-180) < 2),
                drive.returnPart())));
    }

    @Override
    protected void loop() {
        log.show("Use left stick x to move in circle");
        log.show("more encoder separation = forward offset after 180 degree");

        double xPow = -twoOdometry.getX()*0.05 - Math.signum(twoOdometry.getX())*0.02;
        double yPow = -twoOdometry.getY()*0.05 - Math.signum(twoOdometry.getY())*0.02;

        Vector powerVector = new Vector(xPow, yPow).getRotated(-twoOdometry.getHeading()).getScaled(1);

        drive.move(powerVector.getY() + gph1.ry/2.0, powerVector.getX() + gph1.rx/2.0, gph1.lx/2.0);

        log.show("Odometry Pose", twoOdometry);
    }
}

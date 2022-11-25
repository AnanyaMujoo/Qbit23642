package unittests.tele.framework.movement;

import automodules.AutoModule;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import teleutil.button.Button;
import teleutil.button.OnTurnOffEventHandler;
import teleutil.button.OnTurnOnEventHandler;
import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gph1;
import static global.General.log;

public class OdometryTest extends TeleUnitTest {
    @Override
    protected void start() {
        gph1.link(Button.Y, odometry::reset);
//        gph1.link(Button.RIGHT_BUMPER, () -> odometry.angle2 += Math.toRadians(1.0));
//        gph1.link(Button.LEFT_BUMPER, () -> odometry.angle2 -= Math.toRadians(1.0));
//        gph1.link(Button.RIGHT_BUMPER, () -> odometry.angle3 += Math.toRadians(1.0));
//        gph1.link(Button.LEFT_BUMPER, () -> odometry.angle3 -= Math.toRadians(1.0));
        gph1.link(Button.RIGHT_BUMPER, () -> odometry.mode += 1);
        gph1.link(Button.LEFT_BUMPER, () -> odometry.mode -= 1);
    }

    @Override
    protected void loop() {
        log.show("Odometry Pose", odometry);
        log.show("Angle 2", odometry.angle2);
    }

}

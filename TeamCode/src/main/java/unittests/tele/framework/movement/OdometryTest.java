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
import math.trigonmetry.Trig;
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
        gph1.link(Button.DPAD_UP, () -> odometry.scaleRight += 0.001);
        gph1.link(Button.DPAD_DOWN, () -> odometry.scaleRight -= 0.001);
//        gph1.link(Button.DPAD_UP, () -> odometry.angleLeft += Trig.rad(0.2));
//        gph1.link(Button.DPAD_DOWN, () -> odometry.angleLeft -= Trig.rad(0.2));
//        gph1.link(Button.DPAD_RIGHT, () -> odometry.angleRight += Trig.rad(0.2));
//        gph1.link(Button.DPAD_LEFT, () -> odometry.angleRight -= Trig.rad(0.2));
        gph1.link(Button.RIGHT_BUMPER, () -> odometry.mode += 1);
        gph1.link(Button.LEFT_BUMPER, () -> odometry.mode -= 1);
    }

    @Override
    protected void loop() {
        log.show("Odometry Pose", odometry);
        log.show("Mode", odometry.mode);
        log.show("Angle Left", Trig.deg(odometry.angleLeft));
        log.show("Angle Right", Trig.deg(odometry.angleRight));
        log.show("Scale Right", odometry.scaleRight);
    }

}

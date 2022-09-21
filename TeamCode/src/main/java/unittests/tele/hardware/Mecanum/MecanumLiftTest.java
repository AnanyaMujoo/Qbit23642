package unittests.tele.hardware.Mecanum;

import static global.General.automodules;
import static global.General.bot;
import static global.General.gamepad2;
import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;

import teleutil.button.Button;
import unittests.tele.TeleUnitTest;

public class MecanumLiftTest extends TeleUnitTest {
    @Override
    public void init() {
//        gph1.link(Button.Y, automodules.LiftUpAlliance);
//        gph2.link(Button.Y, automodules.LiftReset);
    }

    @Override
    protected void loop() {
        log.show("Left Encoder", bot.lift.motorUp.getPosition());
        log.show("Right Encoder", bot.lift.motorDown.getPosition());
        bot.lift.move(-gamepad2.right_stick_y);
    }
}

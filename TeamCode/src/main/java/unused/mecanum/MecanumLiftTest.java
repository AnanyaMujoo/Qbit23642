package unused.mecanum;

import static global.General.bot;
import static global.General.gamepad2;
import static global.General.log;

import unittests.tele.TeleUnitTest;

public class MecanumLiftTest extends TeleUnitTest {
    @Override
    public void init() {
//        gph1.link(Button.Y, automodules.LiftUpAlliance);
//        gph2.link(Button.Y, automodules.LiftReset);
    }

    @Override
    protected void loop() {
        log.show("Left Encoder", bot.mecanumLift.motorUp.getPosition());
        log.show("Right Encoder", bot.mecanumLift.motorDown.getPosition());
        bot.mecanumLift.move(-gamepad2.right_stick_y);
    }
}

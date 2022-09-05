package unittests.tele.hardware;

import robotparts.electronics.positional.PMotor;
import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gamepad1;
import static global.General.log;

public class StallDetectorTest extends TeleUnitTest {

    @Override
    protected void loop() {

        bot.tankLift.move(-gamepad1.right_stick_y);
        log.show("Power", bot.tankLift.getElectronicsOfType(PMotor.class).get("li").getPower());
        log.show("Speed", bot.tankLift.getElectronicsOfType(PMotor.class).get("li").getStallDerivative());
    }
}

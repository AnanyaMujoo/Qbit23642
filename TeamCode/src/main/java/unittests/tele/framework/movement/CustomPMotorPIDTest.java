package unittests.tele.framework.movement;

import robotparts.electronics.positional.PMotor;
import robotparts.hardware.Lift;
import teleutil.button.Button;
import teleutil.button.OnPressEventHandler;
import unittests.tele.TeleUnitTest;
import unused.mecanumold.MecanumLift;

import static global.General.bot;
import static global.General.gph1;
import static global.General.log;

public class CustomPMotorPIDTest extends TeleUnitTest {

    // TODO 4 Test

    private final MecanumLift part = mecanumLift;
    private final PMotor motor = part.motorUp;

    @Override
    public void init() {
        motor.scalePIDFCoefficients(0.5,1,1,1);
    }

    @Override
    protected void start() {
        gph1.link(Button.B, LiftUpTopFast);
        gph1.link(Button.X, bot::cancelAutoModules);
    }

    @Override
    protected void loop() {
        log.show("Default Coeffs", motor.getDefaultPIDFCoefficients().toString());
        log.show("Current Coeffs", motor.getCurrentPIDFCoefficients().toString());
    }
}

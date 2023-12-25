package unittests.tele.framework.movement;

import robotparts.electronics.positional.PMotor;
import robotparts.hardware.Lift;
import teleutil.button.Button;
import teleutil.button.OnPressEventHandler;
import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gph1;
import static global.General.log;

public class CustomPMotorPIDTest extends TeleUnitTest {


    private final PMotor motor = lift.motorRight;

    @Override
    public void init() {
        motor.scalePIDFCoefficients(0.3,2,1,1);
    }

    @Override
    protected void start() {
//        gph1.link(Button.B, LiftUpTopFast);
//        gph1.link(Button.Y, LiftReset);
        gph1.link(Button.X, bot::cancelAutoModules);
        lift.maintain();
    }

    @Override
    protected void loop() {
        log.show("Default Coeffs", motor.getDefaultPIDFCoefficients().toString());
        log.show("Current Coeffs", motor.getCurrentPIDFCoefficients().toString());
    }
}

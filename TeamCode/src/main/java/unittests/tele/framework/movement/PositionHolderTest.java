package unittests.tele.framework.movement;

import autoutil.controllers.control1D.PositionHolder;
import robotparts.electronics.positional.PMotor;
import robotparts.hardware.Lift;
import teleutil.button.Button;
import unittests.tele.TeleUnitTest;

import static global.General.gph1;
import static global.General.log;

public class PositionHolderTest extends TeleUnitTest {

    private final Lift part = lift;
    private final PMotor motor = part.motorRight;
    private final PositionHolder positionHolder = motor.getPositionHolder();

    @Override
    protected void start() {
//        gph1.link(Button.B, BackwardNew);
//        gph1.link(Button.Y, ForwardNew);
    }

    @Override
    protected void loop() {
        part.move(gph1.ry);
        log.show("Endoder");
        log.show("pos", motor.getMotorEncoder().getPos());
        log.show("Speed (deg/s)", motor.getMotorEncoder().getAngularVelocity());
        log.show("Output (power)", positionHolder.getOutput());
        log.show("Current (amps)", motor.getMotorEncoder().getCurrent());
    }
}

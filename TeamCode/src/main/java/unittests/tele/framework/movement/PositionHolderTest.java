package unittests.tele.framework.movement;

import autoutil.controllers.control1D.PositionHolder;
import robotparts.electronics.positional.PMotor;
import robotparts.hardware.Lift;
import unittests.tele.TeleUnitTest;

import static global.General.gph1;
import static global.General.log;

public class PositionHolderTest extends TeleUnitTest {

    // TODO 4 Test

    private final Lift part = lift;
    private final PMotor motor = part.motorUp;
    private final PositionHolder positionHolder = motor.getPositionHolder();


    @Override
    public void init() {
        part.restPowUp = 0.0;
        motor.setRestPower(0.0);
    }

    @Override
    protected void loop() {
        part.move(gph1.ry);
        log.show("Speed (deg/s)", positionHolder.getCurrentValue());
        log.show("Output (power)", positionHolder.getOutput());
    }
}

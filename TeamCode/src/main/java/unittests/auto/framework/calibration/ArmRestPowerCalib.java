package unittests.auto.framework.calibration;

import unittests.auto.AutoUnitTest;
import util.iter.FinalDouble;

import static global.General.log;

public class ArmRestPowerCalib extends AutoUnitTest {

    @Override
    protected void run() {
        FinalDouble power = new FinalDouble();
        whileActive(() -> lift.liftRight.getPosition() < 20, () -> {
            lift.move(power.get());
            power.increment(0.001);
        });
        lift.halt();
        whileTime(() -> {log.show("Lift Rest Power", power.get());}, 10);
    }
}

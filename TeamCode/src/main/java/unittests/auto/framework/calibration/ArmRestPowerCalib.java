package unittests.auto.framework.calibration;

import unittests.auto.AutoUnitTest;
import util.iter.FinalDouble;

import static global.General.log;

public class ArmRestPowerCalib extends AutoUnitTest {

    @Override
    protected void run() {
        FinalDouble power = new FinalDouble();
        whileActive(() -> liftDONOTUSE.liftRight.getPosition() < 20, () -> {
            liftDONOTUSE.move(power.get());
            power.increment(0.001);
        });
        liftDONOTUSE.halt();
        whileTime(() -> {log.show("Lift Rest Power", power.get());}, 10);
    }
}

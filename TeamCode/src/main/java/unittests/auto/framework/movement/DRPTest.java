package unittests.auto.framework.movement;

import autoutil.controllers.control1D.DRP;
import robotparts.hardware.Drive;
import unittests.auto.AutoUnitTest;

import static global.General.log;

public class DRPTest extends AutoUnitTest {

    private DRP testDRP;

    @Override
    protected Drive getTestPart() { return drive; }

    @Override
    protected void start() {
        twoOdometryV2.reset();
        testDRP = new DRP(0.003, 0.03);
    }

    @Override
    protected void run() {

        testDRP.setProcessVariable(twoOdometryV2::getY);
        testDRP.setTarget(20);

        whileActive(() -> !testDRP.isAtTarget(), () -> {
            testDRP.update();
            log.show("Error, Output", "\n"+ testDRP.getError()+", \n"+ testDRP.getOutput());
            getTestPart().move(testDRP.getOutput(),0, 0);
        });

        testDRP.reset();

        getTestPart().move(0,0, 0);

        log.show("Done with movement");

    }
}

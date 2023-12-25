package unittests.auto.framework.movement;

import autoutil.controllers.control1D.DRP;
import robotparts.hardware.Drive;
import unittests.auto.AutoUnitTest;

import static global.General.log;

public class DRPTest extends AutoUnitTest {

    private DRP testDRP;

    @Override
    protected void start() {
        odometry.reset();
        testDRP = new DRP(0.03, 0.03);
    }

    @Override
    protected void run() {

        testDRP.setProcessVariable(odometry::getY);
        testDRP.setTarget(20);

        whileActive(() -> !testDRP.isAtTarget(), () -> {
            testDRP.update();
            log.show("Error, Output", "\n"+ testDRP.getError()+", \n"+ testDRP.getOutput());
            drive.move(testDRP.getOutput(),0, 0);
        });

        testDRP.reset();

        drive.move(0,0, 0);

        log.show("Done with movement");

    }
}

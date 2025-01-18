package unittests.auto.framework.movement;

import java.util.Arrays;

import autoutil.controllers.control1D.PAR;
import unittests.auto.AutoUnitTest;
import static global.General.bot;
import static global.General.log;

public class PARTest extends AutoUnitTest {

    private PAR testPAR;


    @Override
    protected void start() {
        odometry.reset();
        testPAR = new PAR(0.003, 0.45, 0.08);
    }

    @Override
    protected void run() {
        log.show("Coefficients ", Arrays.toString(testPAR.getCoefficients()));

        testPAR.setProcessVariable(odometry::getY);
        testPAR.setTarget(20);

        log.show("Target (20)", testPAR.getTarget());

        whileActive(() -> !testPAR.isAtTarget(), () -> {
            testPAR.update();
            log.show("Error, Output", "\n"+ testPAR.getError()+", \n"+ testPAR.getOutput());
            drive.move(testPAR.getOutput(),0, 0);
        });

        testPAR.reset();
        drive.move(0,0, 0);

        log.show("Done with movement");

    }
}

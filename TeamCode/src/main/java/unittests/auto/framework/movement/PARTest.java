package unittests.auto.framework.movement;

import java.util.Arrays;

import autoutil.controllers.control1D.PAR;
import autoutil.controllers.control1D.PID;
import robotparts.hardware.Drive;
import unittests.auto.AutoUnitTest;
import unused.mecanumold.MecanumDrive;

import static global.General.bot;
import static global.General.log;

public class PARTest extends AutoUnitTest {

    private PAR testPAR;

    @Override
    protected MecanumDrive getTestPart() {
        return mecanumDrive;
    }

    @Override
    protected void start() {
        odometry.reset();
        testPAR = new PAR(0.003, 0.45, 0.08);
    }

    @Override
    protected void run() {
        log.show("Coefficients ", Arrays.toString(testPAR.getCoefficients()));

        testPAR.setProcessVariable(() -> bot.odometry.getCurY());
        testPAR.setTarget(20);

        log.show("Target (20)", testPAR.getTarget());

        whileActive(() -> !testPAR.isAtTarget(), () -> {
            testPAR.update();
            log.show("Error, Output", "\n"+ testPAR.getError()+", \n"+ testPAR.getOutput());
            getTestPart().move(testPAR.getOutput(),0, 0);
        });

        testPAR.reset();
        getTestPart().move(0,0, 0);

        log.show("Done with movement");

    }
}

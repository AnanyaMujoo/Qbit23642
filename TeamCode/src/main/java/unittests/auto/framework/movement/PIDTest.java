package unittests.auto.framework.movement;

import java.util.Arrays;

import autoutil.controllers.control1D.PID;
import unused.mecanumold.MecanumDrive;
import unittests.auto.AutoUnitTest;

import static global.General.bot;
import static global.General.log;

public class PIDTest extends AutoUnitTest {
    /**
     * Tests PID (Proportional Integral Derivative)
     */

    @Override
    protected MecanumDrive getTestPart() {
        return mecanumDrive;
    }

    /**
     * PID object to test
     */
    PID testPID = new PID(PID.PIDParameterType.DEFAULT_ALL, .05,0.005,0.005, 0.2, 0.05, 50.0, 5.0);

    @Override
    protected void run() {
        log.show("Coefficients ", Arrays.toString(testPID.getCoefficients()));

        testPID.setProcessVariable(() -> bot.odometry.getCurY());
        testPID.setTarget(20);
        testPID.setMinimumOutput(0.05);
        testPID.setMaximumTime(0.05);
        testPID.setMaximumDerivative(10);

        log.show("Target (20)", testPID.getTarget());

        whileActive(() -> !testPID.isAtTarget(), () -> {
            testPID.update();
            log.show("Error, Output", "\n"+ testPID.getError()+", \n"+ testPID.getOutput());
            getTestPart().move(testPID.getOutput(),0, 0);
        });

        testPID.reset();
        getTestPart().move(0,0, 0);

        log.show("Done with movement");

    }
}

package unittests.auto.framework.movement;

import autoutil.executors.MecanumExecutorArcsPID;
import unittests.auto.AutoUnitTest;

// TODO 4 REMOVE
public class ArcTest extends AutoUnitTest {
    @Override
    public void defineExecutorAndAddPoints() {
        executor = new MecanumExecutorArcsPID(linearOpMode);
        addExecutorFuncs(
            setPoint(10, 10, Math.PI/2)
        );
    }
}

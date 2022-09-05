package unittests.auto.movement;

import autoutil.executors.MecanumExecutor;
import autoutil.executors.MecanumExecutorArcsPID;
import autoutil.executors.TankExecutor;
import geometry.circles.AngleType;
import unittests.auto.AutoUnitTest;

public class ArcTest extends AutoUnitTest {
    @Override
    public void defineExecutorAndAddPoints() {
        executor = new MecanumExecutorArcsPID(linearOpMode);
        addExecutorFuncs(
            setPoint(10, 10, Math.PI/2)
        );
    }
}

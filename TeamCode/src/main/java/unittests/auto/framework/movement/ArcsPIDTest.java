package unittests.auto.framework.movement;

import static java.lang.Math.PI;

import autoutil.executors.MecanumExecutorArcsPID;
import unittests.auto.AutoUnitTest;


// TODO 4 REMOVE
public class ArcsPIDTest extends AutoUnitTest {
    @Override
    public void defineExecutorAndAddPoints() {
        executor = new MecanumExecutorArcsPID(linearOpMode);
        addExecutorFuncs(
                setPoint(10, 10, PI/2),
                setPoint(0, 0, 0)
        );
    }
}

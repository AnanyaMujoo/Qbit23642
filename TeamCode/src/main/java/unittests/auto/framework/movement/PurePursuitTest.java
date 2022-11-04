package unittests.auto.framework.movement;

import autoutil.AutoSegment;
import autoutil.Executor;
import autoutil.generators.LineGenerator;
import autoutil.reactors.MecanumPurePursuitReactor;
import geometry.position.Pose;
import unittests.auto.AutoUnitTest;

public class PurePursuitTest extends AutoUnitTest {
    /**
     * Test pure pursuit
     */

    /**
     * Run method for testing
     */
    @Override
    protected void run() {

//        generator.addAutoModule(automodules.DuckTele);

        lineGenerator.add(new Pose(), new Pose(0,40,0));
        mecanumDefaultWayPoint.run(linearOpMode);
        lineGenerator.add(new Pose(), new Pose(0,40,0));
        mecanumDefaultWayPoint.run(linearOpMode);
    }
}
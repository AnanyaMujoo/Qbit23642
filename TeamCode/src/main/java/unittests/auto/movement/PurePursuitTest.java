package unittests.auto.movement;

import autoutil.executors.MecanumExecutor;
import autoutil.generators.LineGenerator;
import autoutil.generators.PoseGenerator;
import autoutil.reactors.mecanum.MecanumPIDReactor;
import autoutil.reactors.mecanum.MecanumPurePursuitReactor;
import geometry.position.Point;
import geometry.position.Pose;
import unittests.auto.AutoUnitTest;
import static global.General.*;

public class PurePursuitTest extends AutoUnitTest {
    /**
     * Test pure pursuit
     */

    /**
     * Generators, reactors, and executors
     */
    LineGenerator generator;
    MecanumPurePursuitReactor reactor;
    MecanumExecutor executor;

    /**
     * Run method for testing
     */
    @Override
    protected void run() {
        generator = new LineGenerator();
        reactor = new MecanumPurePursuitReactor();
        executor = new MecanumExecutor(linearOpMode);

//        generator.addAutoModule(automodules.DuckTele);

        generator.add(40,0,0);
        generator.add(40,40,0);

        executor.setPath(generator.getPath());
        executor.setReactor(reactor);
        executor.followPath();
    }
}
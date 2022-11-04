package unittests.auto.framework.movement;

import autoutil.Executor;
import autoutil.generators.LineGenerator;
import autoutil.reactors.MecanumPurePursuitReactor;
import unittests.auto.AutoUnitTest;

public class PurePursuitTest extends AutoUnitTest {
    /**
     * Test pure pursuit
     */

    /**
     * Generators, reactors, and executors
     */
    LineGenerator generator;
    MecanumPurePursuitReactor reactor;
    Executor executor;

    /**
     * Run method for testing
     */
    @Override
    protected void run() {
        generator = new LineGenerator();
        reactor = new MecanumPurePursuitReactor();
        executor = new Executor(linearOpMode);

//        generator.addAutoModule(automodules.DuckTele);

        generator.add(40,0,0);
        generator.add(40,40,0);

        executor.setPath(generator.getPath());
        executor.setReactor(reactor);
        executor.followPath();
    }
}
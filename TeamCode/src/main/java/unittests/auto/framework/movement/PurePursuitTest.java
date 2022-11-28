package unittests.auto.framework.movement;

import autoutil.AutoConfig;
import autoutil.AutoFramework;
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

    @Override
    protected void start() {
        setAuto(new AutoFramework() {
            @Override
            public void define() {
                setConfig(mecanumDefaultConfig);
            }

            @Override
            public void initAuto() {
                addWaypoint(0, 40, 0);
            }
        });
    }
}
package auton;

import autoutil.executors.Executor;
import autoutil.executors.MecanumExecutor;
import autoutil.generators.Generator;
import autoutil.generators.LineGenerator;
import autoutil.generators.PoseGenerator;
import autoutil.reactors.Reactor;
import autoutil.reactors.MecanumPIDReactor;
import autoutil.reactors.MecanumPurePursuitReactor;
import autoutil.vision.CaseScanner;
import autoutil.vision.TeamElementScanner;

// TODO 4 NEW Rename to Auto/Combine with autoframework, this should be currentAuto or smt
public abstract class MecanumAuto extends AutoFramework{
    @Override
    public Executor getExecutor() {
        return new MecanumExecutor(this);
    }

    @Override
    public Reactor getSetpointReactor() {
        return new MecanumPIDReactor();
    }

    @Override
    public Reactor getWaypointReactor() {
        return new MecanumPurePursuitReactor();
    }

    @Override
    public Generator getSetpointGenerator() {
        return new PoseGenerator();
    }

    @Override
    public Generator getWaypointGenerator() {
        return new LineGenerator(lastPose);
    }

    @Override
    public CaseScanner getCaseScanner() { return new TeamElementScanner(); }
}

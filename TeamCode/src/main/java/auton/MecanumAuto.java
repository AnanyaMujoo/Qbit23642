package auton;

import autoutil.executors.ExecutorNew;
import autoutil.executors.MecanumExecutor;
import autoutil.generators.Generator;
import autoutil.generators.LineGenerator;
import autoutil.generators.PoseGenerator;
import autoutil.reactors.Reactor;
import autoutil.reactors.mecanum.MecanumPIDReactor;
import autoutil.reactors.mecanum.MecanumPurePursuitReactor;
import autoutil.vision.CaseScanner;
import autoutil.vision.TeamElementScanner;

public abstract class MecanumAuto extends AutoFramework{
    @Override
    public ExecutorNew getExecutor() {
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

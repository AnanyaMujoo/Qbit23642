package autoutil.generators;

import automodules.stage.Exit;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import autoutil.reactors.Reactor;
import geometry.position.Pose;

public class PoseGenerator extends Generator{
    @Override
    public void add(Pose start, Pose target) {}

    @Override
    public Stage getStage(Reactor reactor) {
        return new Stage(reactor.mainTarget(this), reactor.exitTarget(), reactor.stopTarget());
    }
}
package autoutil.generators;

import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.reactors.Reactor;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.codeseg.CodeSeg;

public class BreakpointGenerator extends Generator{

    private final CodeSeg code;

    public BreakpointGenerator(CodeSeg code){
        this.code = code;
    }

    @Override
    public void add(Pose start, Pose target) {}

    @Override
    public Stage getStage(Reactor reactor) {
        return new Stage(new Main(code), RobotPart.exitAlways());
    }

}

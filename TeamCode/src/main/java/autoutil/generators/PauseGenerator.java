package autoutil.generators;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Stage;
import autoutil.reactors.Reactor;
import geometry.position.Pose;
import util.Timer;

import static global.General.bot;

public class PauseGenerator extends Generator{

    private final double time;
    private final Timer timer = new Timer();

    public PauseGenerator(double time){
        this.time = time;
    }

    public void startPausing(){
        timer.reset();
    }

    public boolean isDonePausing(){
        return timer.seconds() > time;
    }

    @Override
    public void add(Pose start, Pose target) {}

    @Override
    public Stage getStage(Reactor reactor) {
        return new Stage(new Initial(this::startPausing), new Initial(bot.drive::halt), new Exit(this::isDonePausing));
    }
}

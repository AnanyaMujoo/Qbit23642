package autoutil.generators;

import automodules.AutoModule;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.reactors.Reactor;
import geometry.position.Pose;
import robotparts.RobotPart;

import static global.General.bot;

public class AutoModuleGenerator extends Generator{
    private AutoModule automodule;
    private boolean isConcurrent;
    public boolean isCancel = false;
    public AutoModuleGenerator(boolean isCancel){
        this.isCancel = isCancel;
    }
    public AutoModuleGenerator(AutoModule automodule, boolean isConcurrent){
        this.automodule = automodule;
        this.isConcurrent = isConcurrent;
    }
    public void runAutoModule(){
        bot.addAutoModule(automodule);
    }
    public boolean isDoneWithAutoModule(){
        return bot.rfsHandler.getRfsQueue().isEmpty();
    }

    @Override
    public void add(Pose start, Pose target) {}

    @Override
    public Stage getStage(Reactor reactor) {
        if(!isCancel){
            if(!isConcurrent){
                return new Stage(new Initial(this::runAutoModule), new Initial(bot::halt), new Exit(this::isDoneWithAutoModule));
            }else{
                return new Stage(new Initial(this::runAutoModule), RobotPart.exitAlways());
            }
        }else{
            bot.cancelAutoModules();
            return new Stage(RobotPart.exitAlways());
        }
    }
}

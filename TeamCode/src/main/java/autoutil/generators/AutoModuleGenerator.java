package autoutil.generators;

import automodules.AutoModule;
import geometry.position.Pose;

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
    public boolean isConcurrent(){return isConcurrent;}

    @Override
    public void add(Pose start, Pose target) {

    }
}

package autoutil.paths;

import automodules.AutoModule;

import static global.General.bot;

public class PathAutoModule extends PathSegment {
    private AutoModule automodule;
    private boolean isConcurrent;
    public boolean isCancel = false;
    public PathAutoModule(boolean isCancel){
        this.isCancel = isCancel;
    }
    public PathAutoModule(AutoModule automodule, boolean isConcurrent){
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

}
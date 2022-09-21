package autoutil.paths;

import automodules.StageList;

import static global.General.bot;

public class PathAutoModule extends PathSegment2{
    private StageList automodule;
    private boolean isConcurrent;
    public boolean isCancel = false;
    public PathAutoModule(boolean isCancel){
        this.isCancel = isCancel;
    }
    public PathAutoModule(StageList automodule, boolean isConcurrent){
        this.automodule = automodule;
        this.isConcurrent = isConcurrent;
    }
    public void runAutoModule(){
        bot.addAutoModule(automodule);
    }
    public boolean isDoneWithAutoModule(){
        return bot.rfsHandler.rfsQueue.isEmpty();
    }
    public boolean isConcurrent(){return isConcurrent;}

}
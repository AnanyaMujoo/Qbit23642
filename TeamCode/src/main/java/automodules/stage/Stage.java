package automodules.stage;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;

import util.codeseg.ParameterCodeSeg;

public class Stage {
    /**
     * Stage has an arraylist of stagecomponents
     */
    private final ArrayList<StageComponent> components = new ArrayList<>();
    /**
     * Has the stage started
     */
    private volatile boolean hasStarted = false;
    /**
     * Is this stage a pause?
     */
    private volatile boolean isPause = false;

    /**
     * Create a stage using stage components
     * @param stageComponents
     */
    public Stage(StageComponent...stageComponents){
        components.addAll(Arrays.asList(stageComponents));
    }

    /**
     * Create a stage that is a pause
     * @param isPause
     */
    public Stage(boolean isPause){
        this.isPause = isPause;
    }

    /**
     * Has the stage started
     * @return hasStarted
     */
    public boolean hasStarted(){
        return hasStarted;
    }

    /**
     * Start the stage
     */
    public void start(){
        runForAllStageComponents(StageComponent::start);
        hasStarted = true;
    }

    /**
     * Loop
     */
    public void loop(){
        runForAllStageComponents(StageComponent::loop);
    }

    /**
     * Should the stage stop
     * @return should stop
     */
    public boolean shouldStop(){
        for(StageComponent sc: components){
            if(sc.shouldStop()){
                return true;
            }
        }
        return false;
    }

    /**
     * Run on stop
     */
    public void runOnStop(){
        runForAllStageComponents(StageComponent::runOnStop);
        hasStarted = false;
    }

    /**
     * Is this stage a pause
     * @return isPause
     */
    public boolean isPause(){
        return isPause;
    }

    /**
     * Internal method to run for all stage components using a parameter code seg
     * @param code
     */
    private void runForAllStageComponents(ParameterCodeSeg<StageComponent> code){
        for(StageComponent sc: components){
            code.run(sc);
        }
    }

}

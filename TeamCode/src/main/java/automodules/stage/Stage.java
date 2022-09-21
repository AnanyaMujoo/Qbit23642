package automodules.stage;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;

import robotparts.RobotPart;
import util.codeseg.ParameterCodeSeg;
import util.template.Iterator;

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
        addDefaults();
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
        Iterator.forAll(components, StageComponent::start);
        hasStarted = true;
    }

    /**
     * Loop
     */
    public void loop(){
        Iterator.forAll(components, StageComponent::loop);
    }

    /**
     * Should the stage stop
     * @return should stop
     */
    public boolean shouldStop(){
        return Iterator.forAllCondition(components, StageComponent::shouldStop);
    }

    /**
     * Run on stop
     */
    public void runOnStop(){
        Iterator.forAll(components, StageComponent::runOnStop);
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
     * Add default components if they are not added
     */
    private void addDefaults(){
        if(Iterator.forAllCount(components, comp -> comp instanceof Exit) == 0){
            components.add(RobotPart.exitAlways());
        }
    }

}

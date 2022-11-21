package automodules.stage;

import java.util.ArrayList;
import java.util.Arrays;

import robotparts.RobotPart;
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

    public Stage(ArrayList<StageComponent> stageComponents){
        components.addAll(stageComponents); addDefaults();
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
        return Iterator.forAllConditionOR(components, StageComponent::shouldStop);
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

    public Stage combine(Stage stage){
        Iterator.forAll(stage.components, components::add);
        return this;
    }

    public Stage combine(StageComponent... stageComponents){
        components.addAll(Arrays.asList(stageComponents));
        return this;
    }

    /**
     * The new stage "attaches" to the old one. NOTE: This is different than combine, because the exit condition of the new stage is still the same
     * @param stage
     * @return attached stage
     */

    // TODO CLEAN Make less sketch
    public Stage attach(Stage stage){
        final ArrayList<StageComponent> oldComponents = new ArrayList<>(this.components);
        int[] exitCode = {0};
        return new Stage(oldComponents){
            @Override
            public void loop() {
                if(exitCode[0] == 0){
                    super.loop();
                    if(stage.shouldStop()){ exitCode[0] = 1; }
                }else if(exitCode[0] == 1){
                    Iterator.forAll(oldComponents, StageComponent::loop);
                    stage.runOnStop();
                    exitCode[0] = 2;
                }else{
                    Iterator.forAll(oldComponents, StageComponent::loop);
                }
            }

            @Override
            public boolean shouldStop(){ return Iterator.forAllConditionOR(oldComponents, StageComponent::shouldStop); }

            @Override
            public void runOnStop() {
                super.runOnStop();
                exitCode[0] = 0;
            }
        }.combine(stage);
    }

}

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
     * The new stage "attaches" to the old one. NOTE: This is different than combine, because the exit condition of the new stage overlaps
     * [ --- old stage --- ]
     * [ ------ new stage ------ ]
     * OR
     *  [ ------ old stage ------ ]
     *  [ --- new stage --- ]
     * @param stage
     * @return attached stage
     */
    public Stage attach(Stage stage){
        final ArrayList<StageComponent> oldComponents = new ArrayList<>(this.components);
        final ArrayList<StageComponent> newComponents = new ArrayList<>(stage.components);
        final int[] exitCode = {0};
        return new Stage(oldComponents){
            @Override
            public void loop() { switch (exitCode[0]){
                case 0: Iterator.forAll(oldComponents, StageComponent::loop); Iterator.forAll(newComponents, StageComponent::loop); if(Iterator.forAllConditionOR(newComponents, StageComponent::shouldStop)){ exitCode[0] = 1; } if(Iterator.forAllConditionOR(oldComponents, StageComponent::shouldStop)){ exitCode[0] = 3; } break;
                case 1: Iterator.forAll(newComponents, StageComponent::runOnStop); exitCode[0] = 2; break;
                case 2: Iterator.forAll(oldComponents, StageComponent::loop); if(Iterator.forAllConditionOR(oldComponents, StageComponent::shouldStop)){ exitCode[0] = 5; } break;
                case 3: Iterator.forAll(oldComponents, StageComponent::runOnStop); exitCode[0] = 4; break;
                case 4: Iterator.forAll(newComponents, StageComponent::loop); if(Iterator.forAllConditionOR(newComponents, StageComponent::shouldStop)){ exitCode[0] = 5; } break;
            }}
            @Override public boolean shouldStop(){ return exitCode[0] == 5; }
            @Override public void runOnStop() { super.runOnStop(); exitCode[0] = 0; }
        }.combine(stage);
    }
}

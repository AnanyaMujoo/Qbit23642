package automodules;

import java.util.ArrayList;
import java.util.Arrays;

import automodules.stage.Stage;

public class AutoModule {
    /**
     * List of stages
     */
    private final ArrayList<Stage> stages = new ArrayList<>();

    /**
     * Create a stage list using the robot parts used
     * @param stageArray
     */
    public AutoModule(Stage... stageArray){
        stages.addAll(Arrays.asList(stageArray));
    }

    public AutoModule add(Stage... stageArray){
        stages.addAll(Arrays.asList(stageArray));
        return this;
    }

    public AutoModule add(AutoModule... other) {
        for (AutoModule list : other) {
            stages.addAll(list.stages);
        }
        return this;
    }

    /**
     * Get the arraylist of stages
     * @return stages
     */
    public ArrayList<Stage> getStages(){
        return stages;
    }
}

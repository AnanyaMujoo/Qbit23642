package automodules;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;

import automodules.stage.Stage;
import util.codeseg.CodeSeg;
import util.template.Iterator;

public class AutoModule {
    /**
     * List of stages
     */
    private final ArrayList<Stage> stages = new ArrayList<>();
    private CodeSeg startCode = () -> {};

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
        Iterator.forAll(other, list -> stages.addAll(list.stages));
        return this;
    }

    public AutoModule setStartCode(CodeSeg startCode){ this.startCode = startCode; return this; }

    public void runStartCode(){ startCode.run(); }

    /**
     * Get the arraylist of stages
     * @return stages
     */
    public ArrayList<Stage> getStages(){
        return stages;
    }
}

package util.condition;

import java.util.HashMap;

import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;

import static global.General.fault;

public class DecisionList {
    /**
     * Class to store a list of decisions
     */


    /**
     * Map from decisions to codesegs that run
     */
    private final HashMap<Decision, CodeSeg> decisions = new HashMap<>();

    /**
     * Condition to check
     */
    private final ReturnCodeSeg<Decision> condition;

    /**
     * Constructor to create decision using condition
     * @param condition
     */
    public DecisionList(ReturnCodeSeg<Decision> condition){
        this.condition = condition;
    }

    /**
     * Add an option to the decision
     * @param decision
     * @param codeSeg
     * @return the decision list
     */
    public DecisionList addOption(Decision decision, CodeSeg codeSeg){
        decisions.put(decision, codeSeg);
        return this;
    }

    /**
     * Run the matching code depending on the current condition
     */
    public void check(){
        Decision currentDecision = condition.run();
        if(decisions.containsKey(currentDecision)){
            decisions.get(currentDecision).run();
        }else{
            fault.warn("No option " + currentDecision.toString() + " found", Expectation.SURPRISING, Magnitude.MODERATE);
        }
    }
}

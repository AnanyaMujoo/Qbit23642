package util.condition;

import java.util.HashMap;

import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;

import static global.General.fault;

public class DecisionList {
    private final HashMap<Decision, CodeSeg> decisions = new HashMap<>();
    private final ReturnCodeSeg<Decision> condition;

    public DecisionList(ReturnCodeSeg<Decision> condition){
        this.condition = condition;
    }

    public DecisionList addOption(Decision decision, CodeSeg codeSeg){
        decisions.put(decision, codeSeg);
        return this;
    }

    public void check(){
        Decision currentDecision = condition.run();
        if(decisions.containsKey(currentDecision)){
            decisions.get(currentDecision).run();
        }else{
            fault.warn("No option " + currentDecision.toString() + " found", Expectation.SURPRISING, Magnitude.MODERATE);
        }
    }

    public interface Decision {}
}

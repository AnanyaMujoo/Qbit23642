package util.condition;

import java.util.HashMap;

import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;

import static global.General.fault;

public class OutputList<T> {

    /**
     * Class to store a list of outputs
     */


    /**
     * Map from decisions to outputs
     */
    private final HashMap<Decision, T> outputs = new HashMap<>();

    /**
     * Condition to check
     */
    private final ReturnCodeSeg<Decision> condition;

    /**
     * Constructor to create decision using condition
     * @param condition
     */
    public OutputList(ReturnCodeSeg<Decision> condition){
        this.condition = condition;
    }

    /**
     * @param decision
     * @param obj
     * @return outputList
     */
    public OutputList<T> addOption(Decision decision, T obj){
        outputs.put(decision, obj);
        return this;
    }

    public T check(){
        Decision currentDecision = condition.run();
        T o = null;
        if(outputs.containsKey(currentDecision)){
            o = outputs.get(currentDecision);
        }else{
            fault.check("No option " + currentDecision.toString() + " found", Expectation.SURPRISING, Magnitude.CRITICAL);
        }
        return o;
    }
}

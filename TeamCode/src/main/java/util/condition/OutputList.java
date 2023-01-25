package util.condition;

import java.util.HashMap;
import java.util.Objects;

import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;

import static global.General.fault;

public class OutputList {

    /**
     * Class to store a list of outputs
     */


    /**
     * Map from decisions to outputs
     */
    private final HashMap<Decision, ReturnCodeSeg<Object>> outputs = new HashMap<>();

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

    public OutputList(ReturnCodeSeg<Boolean> condition, Object ifTrue, Object ifFalse){ this(condition, () -> ifTrue, () -> ifFalse); }
    public OutputList(ReturnCodeSeg<Boolean> condition, ReturnCodeSeg<Object> ifTrue, Object ifFalse){ this(condition, ifTrue, () -> ifFalse); }
    public OutputList(ReturnCodeSeg<Boolean> condition, Object ifTrue, ReturnCodeSeg<Object> ifFalse){ this(condition, () -> ifTrue, ifFalse); }
    public OutputList(ReturnCodeSeg<Boolean> condition, ReturnCodeSeg<Object> ifTrue, ReturnCodeSeg<Object> ifFalse){ this.condition = () -> condition.run() ? BooleanDecision.TRUE : BooleanDecision.FALSE; addOption(BooleanDecision.TRUE, ifTrue); addOption(BooleanDecision.FALSE, ifFalse); }

    /**
     * @param decision
     * @param obj
     * @return outputList
     */
    public OutputList addOption(Decision decision, Object obj){
        outputs.put(decision, () -> obj);
        return this;
    }

    /**
     * Add a return codeseg to output for dynamic outputs
     * @param decision
     * @param obj
     * @return
     */
    public OutputList addOption(Decision decision, ReturnCodeSeg<Object> obj){
        outputs.put(decision, obj);
        return this;
    }


    public <T> T check(){
        Decision currentDecision = condition.run();
        T o = null;
        if(outputs.containsKey(currentDecision)){
            o = (T) outputs.get(currentDecision).run();
        }else{
            fault.check("No option " + currentDecision.toString() + " found", Expectation.SURPRISING, Magnitude.CRITICAL);
        }
        return o;
    }

    public enum BooleanDecision implements Decision { FALSE,  TRUE; }
}

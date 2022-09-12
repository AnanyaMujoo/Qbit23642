package util.template;

import util.Timer;

public interface Precision {
    /**
     * Used for precision tasks involving time
     */


    /**
     * Internal timer objects
     */
    Timer inputTime = new Timer();
    Timer outputTime = new Timer();

    /**
     * Reset precision timers
     */
    default void resetPrecisionTimers(){
        inputTime.reset();
        outputTime.set(1000);
    }

    /**
     * Is the input true for some amount of time
     * @param condition
     * @param time
     * @return true if input stayed true for time
     */
    default boolean isInputTrueForTime(boolean condition, double time){
        if(condition){
            return inputTime.seconds() > time;
        }else{
            inputTime.reset();
            return false;
        }
    }

    /**
     * Outputs true for some amount of time
     * @param condition
     * @param time
     * @return true if condition has been true int the last time
     */
    default boolean outputTrueForTime(boolean condition, double time){
        if(condition){
            outputTime.reset();
            return true;
        }else{
            return outputTime.seconds() < time;
        }
    }
}

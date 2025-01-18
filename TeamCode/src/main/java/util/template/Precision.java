package util.template;

import util.Timer;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.codeseg.ReturnParameterCodeSeg;

public class Precision {
    /**
     * Used for precision tasks involving time
     */

    /**
     * Internal timer objects
     */
    private final Timer inputTime = new Timer();
    private final Timer outputTime = new Timer();
    private final Timer throttleTime = new Timer();
    private final Timer debounceTimer = new Timer();

    private Object lastThrottleVal;
    private int counter = 0;

    public void reset(){
        inputTime.reset();
        outputTime.set(1000);
        throttleTime.reset();
        debounceTimer.reset();
    }

    /**
     * Is the input true for some amount of time
     * @param condition
     * @param time
     * @return true if input stayed true for time
     */
    public boolean isInputTrueForTime(boolean condition, double time){
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
    public boolean outputTrueForTime(boolean condition, double time){
        if(condition){
            outputTime.reset();
            return true;
        }else{
            return outputTime.seconds() < time;
        }
    }

    public boolean isInputTrueForCount(boolean condition, int count){
        if(condition){
            counter += 1;
            if(counter >= count){
                counter = 0;
                return true;
            }else{
                return false;
            }
        }else{
            counter = 0;
            return false;
        }
    }


    /**
     * Helper methods for running on a condition
     * @param condition
     * @param onTrue
     * @return condition
     */
    public static boolean runOnCondition(boolean condition, CodeSeg onTrue){
        if(condition){
            onTrue.run();
            return true;
        }else{
            return false;
        }
    }

    public static boolean runOnCondition(boolean condition, CodeSeg onTrue, CodeSeg onFalse){
        if(condition){
            onTrue.run();
            return true;
        }else{
            onFalse.run();
            return false;
        }
    }


    public static ReturnParameterCodeSeg<Double, Double> invert(ReturnParameterCodeSeg<Double, Double> input){
        return output -> output/input.run(1.0);
    }


    @SuppressWarnings("unchecked")
    public  <T> T throttle(ReturnCodeSeg<T> output, double millis){
        if(throttleTime.seconds() > (millis/1000.0)){
            lastThrottleVal = output.run();
            throttleTime.reset();
        }
        return (T) lastThrottleVal;
    }


    @SuppressWarnings("unchecked")
    public void throttle(CodeSeg code, double millis){
        if(throttleTime.seconds() > (millis/1000.0)){
            code.run();
            throttleTime.reset();
        }
    }

    public static double clip(double value, double low, double high){ return Math.min(Math.max(value, low), high); }
    public static double clip(double value, double range){ return clip(value, -range, range); }

    public static double round(double value, int places){ double shift = Math.pow(10, places); return ((int)(value*shift))/shift; }
    public static double round3(double value){ return round(value, 3); }

    public static double attract(double value, double attractor, double range){ return  Math.abs(value - attractor) < range ? attractor : value; }
    public static double attract(double value, double range){ return attract(value, 0, range); }

    public static boolean range(double value, double max){ return difference(value, 0, max); }
    public static boolean range(double value, double min, double max){ return value > min && value < max; }
    public static boolean difference(double value1, double value2, double max){ return Math.abs(value1 - value2) < max;}
}

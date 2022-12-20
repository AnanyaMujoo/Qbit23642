package util.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;

import util.Timer;
import util.codeseg.CodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.codeseg.ReturnParameterCodeSeg;

import static global.General.log;

public interface Iterator {
    /**
     * Iterator interface, used for making loops with conditions
     */


    /**
     * Private timer for internal methods
     */
    Timer time = new Timer();

    /**
     * Method to define the condition to check (make this return true for default behavior)
     * @return true
     */
    boolean condition();

    /**
     * Do something while the condition is true (default behavior)
     * @param code
     */
    default void whileActive(CodeSeg code){
        boolean oldShouldUpdate = log.getShouldUpdateOnShow();
        log.setShouldUpdateOnShow(false);
        while (condition()){
            code.run();
            log.showTelemetry();
        }
        log.setShouldUpdateOnShow(oldShouldUpdate);
    }

    /**
     * Do something while the condition is true and the active condition is true
     * @param code
     */
    default void whileActive(ReturnCodeSeg<Boolean> active, CodeSeg code){
        boolean oldShouldUpdate = log.getShouldUpdateOnShow();
        log.setShouldUpdateOnShow(false);
        while (condition() && active.run()){
            code.run();
            log.showTelemetry();
        }
        log.setShouldUpdateOnShow(oldShouldUpdate);
    }

    /**
     * Do something for some amount of time
     * @param code
     * @param secs
     */
    default void whileTime(CodeSeg code, double secs){
        boolean oldShouldUpdate = log.getShouldUpdateOnShow();
        log.setShouldUpdateOnShow(false);
        time.reset();
        while (condition() && time.seconds() < secs){
            code.run();
            log.showTelemetry();
        }
        log.setShouldUpdateOnShow(oldShouldUpdate);
    }

    /**
     * Pause for some amount of seconds
     * @param secs
     */
    default void pause(double secs){ whileTime(() -> {}, secs); }


    /**
     * Static helper methods for looping through lists
     * @param list
     * @param code
     * @param <T>
     */
    static<T> void forAll(ArrayList<T> list, ParameterCodeSeg<T> code){
        for(T obj: list){
            code.run(obj);
        }
    }

    static<T> void forAll(Collection<T> list, ParameterCodeSeg<T> code){
        for(T obj: list){
            code.run(obj);
        }
    }

    static<T> void forAll(TreeMap<?, T> list, ParameterCodeSeg<T> code){
        for(T obj: list.values()){
            code.run(obj);
        }
    }

    static<T> void forAll(T[] list, ParameterCodeSeg<T> code){
        for(T obj: list){
            code.run(obj);
        }
    }

    static <T> int forAllCount(ArrayList<T> list, ReturnParameterCodeSeg<T, Boolean> code){
        int count = 0;
        for(T obj: list){
            count += code.run(obj) ? 1:0;
        }
        return count;
    }

    static <T> boolean forAllConditionOR(ArrayList<T> list, ReturnParameterCodeSeg<T, Boolean> code){
        for(T obj: list){
            if(code.run(obj)){
                return true;
            }
        }
        return false;
    }

    static <T> boolean forAllConditionAND(ArrayList<T> list, ReturnParameterCodeSeg<T, Boolean> code){
        for(T obj: list){
            if(!code.run(obj)){
                return false;
            }
        }
        return true;
    }
    @SuppressWarnings("unchecked")
    static <T> ArrayList<T> forAllOfType(ArrayList<? super T> list, Class<T> type){
        ArrayList<T> out = new ArrayList<>();
        for(Object obj: list){
            if(obj.getClass().equals(type)){
                out.add((T) obj);
            }
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    static <T> ArrayList<T> forAllOfExtendedType(ArrayList<? super T> list, Class<T> type){
        ArrayList<T> out = new ArrayList<>();
        for(Object obj: list){
            if(type.isInstance(obj)){
                out.add((T) obj);
            }
        }
        return out;
    }

    static void forAllRun(ArrayList<CodeSeg> codeSegments){ forAll(codeSegments, CodeSeg::run); }

    static double forAllAverage(ArrayList<Double> list){
        final double[] sum = {0};
        Iterator.forAll(list, l->sum[0]+=l);
        return (double)sum[0]/list.size();
    }

    static <T> T forAllCompareMax(ArrayList<T> list, ReturnParameterCodeSeg<T, Double> code){
        ArrayList<Double> values = new ArrayList<>();
        Iterator.forAll(list, o -> values.add(code.run(o)));
        return list.get(maxIndex(values));
    }

    static <T> T forAllCompareMin(ArrayList<T> list, ReturnParameterCodeSeg<T, Double> code){
        ArrayList<Double> values = new ArrayList<>();
        Iterator.forAll(list, o -> values.add(code.run(o)));
        return list.get(minIndex(values));
    }

    static int minIndex(ArrayList<Double> arr){ int ind = 0; double min = arr.get(0); for (int i = 0; i < arr.size() ; i++) { if(arr.get(i) < min){min = arr.get(i); ind = i;} } return ind; }

    static int maxIndex(ArrayList<Double> arr){ int ind = 0; double max = arr.get(0); for (int i = 0; i < arr.size() ; i++) { if(arr.get(i) > max){max = arr.get(i); ind = i;} } return ind; }

    static int minIndex(double... arr){ int ind = 0; double min = arr[0]; for (int i = 0; i < arr.length ; i++) { if(arr[i] < min){min = arr[i]; ind = i;} } return ind; }

    static <T> void removeCondition(ArrayList<T> list, ReturnParameterCodeSeg<T, Boolean> condition){ for (int i = list.size() - 1; i >= 0; --i) { if (condition.run(list.get(i))) { list.remove(i); } } }
}

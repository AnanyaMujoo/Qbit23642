package debugging;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.*;
import java.util.Objects;

import teleutil.Selector;
import util.store.Item;
import util.template.Iterator;

import static global.General.*;

public class Logger {
    /**
     * Hashmap of logs
     * NOTE: Hashmaps preserve order unlike treemaps
     */
    private final LinkedHashMap<String, Log> logs = new LinkedHashMap<>();
    /**
     * The log number since the start
     */
    private int logNum = 0;

    private boolean shouldUpdateOnShow = false;

    public void setShouldUpdateOnShow(boolean value){
        shouldUpdateOnShow = value;
    }

    public boolean getShouldUpdateOnShow(){ return shouldUpdateOnShow; }

    private void updateOnShow(){
        if(shouldUpdateOnShow){telemetry.update();}
    }

    /**
     * Shows the output on telemetry
     * @param val
     */
    public void show(Object val){
        telemetry.addData("Show", val);
        updateOnShow();
    }
    public void show(String caption, Object val){
        telemetry.addData(caption, val);
        updateOnShow();
    }

    /**
     * Records the value and doesn't display telemetry
     * @param val
     */
    public void record(String name, Object val){
        if(!logs.containsKey(name)){
            logs.put(name, new Log("Log #"+logNum+": "+name));
            logNum++;
        }
        Objects.requireNonNull(logs.get(name)).addNewOnly(val);
    }

    /**
     * Shows and records telemetry
     * @param caption
     * @param val
     */
    public void showAndRecord(String caption, Object val){
        show(caption, val);
        record(caption, val);
    }

    /**
     * Creates a header to separate different parts of telemetry
     * @param name
     */
    public void header(String name){
        show("--------" + name.toUpperCase() + "--------");
    }

    /**
     * Create a list and show what is the current value being selected
     * @param values
     * @param currentIndex
     */
    public void list(ArrayList<String> values, int currentIndex){
        for(int i = 0; i < values.size(); i++){
            String num = Integer.toString(i);
            /**
             * Lol formatting issues be like
             */
            if(i < 10){
                num = "0" + num;
            }
            if(i != currentIndex) {
                telemetry.addData("Item " + num, "    " + values.get(i));
            }else{
                telemetry.addData("Item " + num, "--> " + values.get(i));
            }
        }
        updateOnShow();
    }

    /**
     * Updates the telemetry and shows the logs that noTelemetry is false
     */
    public void showTelemetry(){
        telemetry.update();
    }

    /**
     * Sets noTelemetry to true for all the logs (so they effectively arent displayed)
     */
    public void clearTelemetry(){
        telemetry.clear();
    }

    /**
     * Shows the logs at the end of the code
     * To see the logs go to logcat and then the assert tab
     */
    public void showLogs(){
        Iterator.forAll(logs.entrySet(), entry -> {
            String name = entry.getValue().getName();
            String value = entry.getValue().getValues().toString();
            android.util.Log.println(android.util.Log.ASSERT, name, value);
        });
        android.util.Log.println(android.util.Log.ASSERT, "Number of logs", Integer.toString(logNum));
    }
}

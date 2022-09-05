package util.template;

import util.Timer;

public interface Precision {
    Timer timer1 = new Timer();
    Timer timer2 = new Timer();

    default void resetTimer(){
        timer1.reset();
        timer2.set(100);
    }

    default boolean isTrueForTime(boolean condition, double time){
        if(condition){
            return timer1.seconds() > time;
        }else{
            timer1.reset();
        }
        return false;
    }

    default boolean trueForTime(boolean condition, double time){
        if(condition){
            timer2.reset();
            return true;
        }else{
            return timer2.seconds() < time;
        }
    }
}

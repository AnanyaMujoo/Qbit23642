package util.template;

import util.Timer;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;

import static global.General.log;

public interface Iterator {
    /**
     * Iterator interface, used for making loops with conditions
     */


    /**
     * Private timer for internal methods
     */
    Timer timer = new Timer();

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
        timer.reset();
        while (condition() && timer.seconds() < secs){
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
}

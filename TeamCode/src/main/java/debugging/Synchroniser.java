package debugging;

import util.Timer;
import util.User;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.fault;
import static global.General.fieldSide;
import static global.General.log;
import static global.Constants.*;
import static global.General.mainUser;

public class Synchroniser {
    /**
     * Was the ready method called in common?
     */
    private boolean wasReadyCalled = false;
    /**
     * Was the update method called in common?
     */
    private boolean wasUpdateCalled = false;

    private boolean wasActivateCalled = false;
    /**
     * The number of updates since the start
     */
    private int numUpdates = 0;
    /**
     * The timer to determine the lag
     */
    private final Timer lagTimer = new Timer();

    /**
     * Reset the delay to be updated
     */
    public void resetDelay(){
        numUpdates = 0;
        wasReadyCalled = true;
        lagTimer.reset();
    }

    public void logReady(){
        if(!wasActivateCalled){
            log.show("Ready");
            log.showTelemetry();
            wasActivateCalled = true;
        }
    }

    /**
     * Add to the number of updates
     */
    public void update(){
        if(!wasUpdateCalled){
            wasUpdateCalled = true;
        }
        numUpdates++;
    }

    /**
     * Gets the delay in milliseconds
     * @return delay
     */
    public double getDelay(){
        return (1000*lagTimer.seconds())/numUpdates;
    }

    /**
     * Logs the delay and warns if either sync was not updated or the robot is experiencing lag as determined by the minimum refresh rate
     */
    public void logDelay() {
        if(wasReadyCalled) {
            fault.warn("Sync was never updated", Expectation.UNEXPECTED, Magnitude.CRITICAL, numUpdates == 0, false);
            /**
             * The amount of delay between refreshes
             */
            log.record("Delay (ms)", getDelay());
            if(!mainUser.equals(User.AUTO)) {
                fault.warn("Robot is lagging", Expectation.EXPECTED, Magnitude.CRITICAL, getDelay() < (1000 / MINIMUM_REFRESH_RATE), true);
            }
        }
        /**
         * If ready was called but update was not called in common then something is wrong
         */
        fault.check("Super.loop was not called", Expectation.EXPECTED, Magnitude.CRITICAL, wasReadyCalled && !wasUpdateCalled, false);
    }
}

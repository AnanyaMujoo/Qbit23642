package util;


import java.util.ArrayList;

import global.Constants;
import util.codeseg.ExceptionCodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.condition.Status;
import util.template.Iterator;

import static global.General.*;

// TODO PROBLEM Threads dont stop in the middle of init in auton?????

public class TerraThread extends Thread{
    /**
     * Class for handling and creating thread
     * NOTE: Thread should not be used raw without this class
     */


    /**
     * currentStatus represents the status of the thread
     * NOTE: The thread status is active by default which means it will run the moment it starts
     */
    private volatile Status currentStatus = Status.ACTIVE;

    /**
     * Code that will run in a loop in the update method, nothing by default
     */
    private volatile ExceptionCodeSeg<RuntimeException> updateCode = () -> {};

    /**
     * Was an exception thrown inside the thread?
     */
    private volatile boolean wasExceptionThrown = false;

    /**
     * Arraylist of all TerraThread instances
     */
    public static ArrayList<TerraThread> allTerraThreads = new ArrayList<>();

    /**
     * Name of this instance
     */
    private final String name;

    /**
     * Constructor, creates thread using name and adds it to the arraylist
     * @param name
     */
    public TerraThread(String name){
        this.name = name;
        allTerraThreads.add(this);
    }

    /**
     * Initializes the TerraThread by resetting the arraylist (Remove any old threads)
     */
    public static void resetAllThreads(){
        allTerraThreads = new ArrayList<>();
    }

    /**
     * Set the update code (code which will be executed in this thread in a loop)
     * @link updateCode
     * @param cs
     */
    public synchronized void setExecutionCode(ExceptionCodeSeg<RuntimeException> cs){
        updateCode = cs;
    }

    /**
     * Stop updating the thread
     * NOTE: Threads cannot be stopped, only stopped from updating (technically different things)
     */
    public synchronized void stopUpdating(){currentStatus = Status.DISABLED;}

    /**
     * Run method overriden from Thread, all of updateCode will run here
     * This happens until the status is set to disabled through stopUpdating
     * NOTE: This will update according to the thread refresh rate in constants
     */

    @Override
    public void run() {
        /**
         * Run until the currentStatus is disabled
         */
        while (!currentStatus.equals(Status.DISABLED)){
            /**
             * Run the update code in a loop
             * NOTE: If the code throws a runtime exception that will be handled and the thread will stop
             */
            try {
                updateCode.run();
            } catch (RuntimeException r){
                wasExceptionThrown = true;
                stopUpdating();
            }
            /**
             * Wait according to the thread refresh rate
             */
            ExceptionCatcher.catchInterrupted(()-> sleep(1000/Constants.THREAD_REFRESH_RATE));
        }
    }

    /**
     * Set status
     * @param status
     */
    public synchronized void setStatus(Status status){
        currentStatus = status;
    }

    /**
     * Get the status
     * @return status
     */
    public synchronized Status getStatus(){
        return currentStatus;
    }

    /**
     * Check if this thread had an exception thrown inside it
     * NOTE: This will throw another exception but in the main thread so it is visible and will not just crash the app
     */
    private synchronized void checkForException(){
        if(wasExceptionThrown){
            fault.warn("Exception thrown from inside thread " + name, Expectation.SURPRISING, Magnitude.CATASTROPHIC);
        }
    }

    /**
     * Loop Through all threads for exceptions
     */
    public static synchronized void checkAllThreadsForExceptions(){
        Iterator.forAll(allTerraThreads, TerraThread::checkForException);
    }

    /**
     * Get number of started threads
     * @return started thread
     */
    public static synchronized int getNumberOfStartedThreads(){
        return allTerraThreads.size();
    }

    /**
     * Get number of active threads
     * @return active threads
     */
    public static synchronized int getNumberOfActiveThreads(){
        return Iterator.forAllCount(allTerraThreads, thread -> !thread.wasExceptionThrown);
    }

    /**
     * Stop updating all thread
     */
    public static void stopUpdatingAllThreads(){
        Iterator.forAll(allTerraThreads, TerraThread::stopUpdating);
    }
}

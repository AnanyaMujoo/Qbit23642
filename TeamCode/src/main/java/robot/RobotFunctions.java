package robot;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import automodules.AutoModule;
import util.User;
import util.codeseg.ExceptionCodeSeg;
import util.condition.Status;
import util.Timer;
import automodules.stage.Stage;
import util.template.Iterator;

import static global.General.*;
import static robot.RobotFramework.robotFunctionsThread;

public class RobotFunctions {

    // TODO 4 FIX Problem where some stages dont run?

    /**
     * List of all robot functions currently in the queue (LinkedList is a FIFO Queue)
     *
     */
    public final Queue<Stage> rfsQueue = new LinkedList<>();
    /**
     * Timer for robotfunctions, resets after every stage is run
     */
    public final Timer timer = new Timer();
    /**
     * Define the updateCode codeseg to contain the code that will run in the Thread
     */
    public ExceptionCodeSeg<RuntimeException> updateCode = () -> {
        /**
         * Check if the robot has access to move for robotfunctions user or ROFU
         */
        bot.checkAccess(User.ROFU);
        /**
         * If the robotfunctions queue is not empty
         */
        synchronized (rfsQueue) {
            if (!rfsQueue.isEmpty()) {
                /**
                 * Get the oldest stage
                 */
                Stage s = rfsQueue.peek();
                /**
                 * If the stage has not started start it,
                 * Then run the loop code
                 */
                if (!Objects.requireNonNull(s).hasStarted()) {
                    s.start();
                }
                s.loop();
                /**
                 * If the stage should stop, then run on stop code, remove the stage, and reset the timer
                 * Otherwise, set the thread to Status.IDLE to prevent unnecessary lag
                 */
                if (s.shouldStop() && !s.isPause()) {
                    s.runOnStop();
                    rfsQueue.poll();
                    timer.reset();
                } else if (s.isPause()) {
                    robotFunctionsThread.setStatus(Status.IDLE);
                }
            } else {
                robotFunctionsThread.setStatus(Status.IDLE);
            }
        }
    };

    /**
     * Resume the robotfunctions,
     * if the queue is not empty and the oldest stage is a pause then delete it and start the thread again
     * (by setting the status to active)
     */
    public void resume() {
        if (!rfsQueue.isEmpty() && rfsQueue.peek().isPause()) {
            rfsQueue.poll();
            timer.reset();
            robotFunctionsThread.setStatus(Status.ACTIVE);
        }
    }

    /**
     * Initialize the update code in the thread,
     * Make the first stage a pause so the thread doesn't start updating until resume is called
     */
    public void init(){
        addToQueue(new Stage(true));
        robotFunctionsThread.setExecutionCode(updateCode);
    }

    /**
     * Add the automodule by resetting the time and starting the thread
     * Add all of the stages in the automodule to the queue
     * @param autoModule
     */
    public final void addAutoModule(AutoModule autoModule){
        synchronized (rfsQueue) {
            if (rfsQueue.isEmpty()) { robotFunctionsThread.setStatus(Status.ACTIVE); }
            rfsQueue.addAll(autoModule.getStages());
        }
    }

    /**
     * Add a specific stage to the the queue
     * @param s
     * @link addAutoModule
     */
    public final void addToQueue(Stage s) {
        synchronized (rfsQueue) {
            if (rfsQueue.isEmpty()) { robotFunctionsThread.setStatus(Status.ACTIVE); }
            rfsQueue.add(s);
        }
    }

    /**
     * Pause the robotfunction queue in the current state after the stage has ended
     */
    public final void pauseNow() {
        Queue<Stage> newStages = new LinkedList<>();
        if (!rfsQueue.isEmpty()) {
            newStages.add(rfsQueue.poll());
        }
        newStages.add(new Stage(true));
        while (!rfsQueue.isEmpty()) {
            newStages.add(rfsQueue.poll());
        }
        Iterator.forAll(newStages, rfsQueue::add);
    }

    /**
     * Empty the queue and reset the timer
     */
    public final void emptyQueue(){
        synchronized (rfsQueue) {
            if (!rfsQueue.isEmpty()) {
                Stage s = rfsQueue.peek();
                rfsQueue.clear();
                s.runOnStop();
            } else {
                rfsQueue.clear();
            }
            timer.reset();
        }
    }
}
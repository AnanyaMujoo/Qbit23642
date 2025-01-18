package robot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import automodules.stage.Exit;
import util.Timer;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;

public class BackgroundTask {
    /**
     * Class to represent background task
     */

    /**
     * Code to run
     */
    private final CodeSeg task;
    /**
     * Timer object
     */
    private final Timer timer = new Timer();
    /**
     * Exit condition
     */
    private final Exit exit;
    /**
     * Has the background task been started?
     */
    private volatile boolean hasBeenStarted = false;


    /**
     * Create background task with code
     * @param task
     */
    public BackgroundTask(CodeSeg task){
        this.task = task;
        exit = new Exit(() -> false);
    }

    /**
     * Create background task with time limit
     * @param task
     * @param timeActive
     */
    public BackgroundTask(CodeSeg task, double timeActive){
        this.task = task;
        exit = new Exit(() -> timer.seconds() > timeActive);
    }

    /**
     * Create background task with custom exit condition
     * @param task
     * @param exit
     */
    public BackgroundTask(CodeSeg task, Exit exit){
        this.task = task;
        this.exit = exit;
    }

    /**
     * Run the background task
     */
    public void run(){
        if(!hasBeenStarted){
            timer.reset();
            hasBeenStarted = true;
        }
        task.run();
    }

    /**
     * Is the background task done
     * @return isDone
     */
    public boolean isDone(){
        return exit.shouldStop();
    }




}

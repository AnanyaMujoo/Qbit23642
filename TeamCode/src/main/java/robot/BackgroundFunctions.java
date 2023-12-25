package robot;

import java.util.ArrayList;

import util.User;
import util.codeseg.ExceptionCodeSeg;
import util.condition.Status;
import util.template.Iterator;

import static global.General.bot;
import static robot.RobotFramework.backgroundThread;

public class BackgroundFunctions {
    /**
     * Class to run parts of the robot in the background (stabilize lift, read encoder values, etc)
     */

    // TOD 5 Make a functions class which robot functions, background functions and independent functions all extend


    /**
     * List of tasks to run
     */
    public final ArrayList<BackgroundTask> tasks;

    /**
     * Remove all previous tasks in constructor
     */
    public BackgroundFunctions(){ tasks = new ArrayList<>(); }

    /**
     * Initialize the background thread with looping through all tasks
     */
    public void init(){
        ExceptionCodeSeg<RuntimeException> updateCode = () -> {
            bot.checkAccess(User.BACK);
            if(!tasks.isEmpty()){
                synchronized (tasks) { Iterator.forAll(tasks, BackgroundTask::run); }
                removeCompletedTasks();
            } else {
                backgroundThread.setStatus(Status.IDLE);
            }
        };
        backgroundThread.setExecutionCode(updateCode);
        backgroundThread.setStatus(Status.ACTIVE);
    }

    /**
     * Add a new background task
     * @param task
     */
    public final void addBackgroundTask(BackgroundTask task){
        synchronized (tasks){ tasks.add(task); }
    }

    /**
     * Clear task list
     */
    public final void emptyTaskList(){ synchronized (tasks){ tasks.clear(); } }

    /**
     * Remove tasks that are completed
     */
    private void removeCompletedTasks(){
        synchronized (tasks) {
            Iterator.removeCondition(tasks, BackgroundTask::isDone);
        }
    }
}

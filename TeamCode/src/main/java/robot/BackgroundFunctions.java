package robot;

import java.util.ArrayList;

import util.User;
import util.codeseg.ExceptionCodeSeg;
import util.condition.Status;
import util.template.Iterator;

import static global.General.bot;
import static robot.RobotFramework.backgroundThread;

public class BackgroundFunctions {

    public volatile ArrayList<BackgroundTask> tasks;
    public volatile ArrayList<BackgroundTask> taskBuffer;

    public BackgroundFunctions(){
        tasks = new ArrayList<>();
        taskBuffer = new ArrayList<>();
    }

    private volatile boolean clearTasks = false;

    public ExceptionCodeSeg<RuntimeException> updateCode = () -> {
        bot.checkAccess(User.BACK);
        if(!tasks.isEmpty()){
            Iterator.forAll(tasks, BackgroundTask::run);
            removeCompletedTasks();
            checkAndClearTaskList();
        } else {
            backgroundThread.setStatus(Status.IDLE);
        }
    };

    public void init(){
        backgroundThread.setExecutionCode(updateCode);
        backgroundThread.setStatus(Status.ACTIVE);
    }

    public final synchronized void addBackgroundTask(BackgroundTask task){
        if(!clearTasks) {
            tasks.add(task);
        }else {
            taskBuffer.add(task);
        }
    }

    public final synchronized void emptyTaskList(){ clearTasks = true; }

    private synchronized void removeCompletedTasks(){ for(int i = tasks.size() - 1; i >= 0; --i) { if(tasks.get(i).isDone()) { tasks.remove(i); }}}

    private synchronized void checkAndClearTaskList(){
        if(clearTasks){
            tasks.clear();
            tasks.addAll(taskBuffer);
            taskBuffer.clear();
            clearTasks = false;
        }
    }
}

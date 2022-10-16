package robot;

import java.util.ArrayList;

import util.User;
import util.codeseg.ExceptionCodeSeg;
import util.condition.Status;
import util.template.Iterator;

import static global.General.bot;
import static robot.RobotFramework.backgroundThread;

public class BackgroundFunctions {

    // TODO 4 FIX Problem where some stages dont run?

    public final ArrayList<BackgroundTask> tasks;
    public final ArrayList<BackgroundTask> taskBuffer;

    public BackgroundFunctions(){
        tasks = new ArrayList<>();
        taskBuffer = new ArrayList<>();
    }

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

    public final void addBackgroundTask(BackgroundTask task){
        synchronized (tasks){ tasks.add(task); }
    }

    public final void emptyTaskList(){ synchronized (tasks){ tasks.clear(); } }

    private void removeCompletedTasks(){
        synchronized (tasks) {
            for (int i = tasks.size() - 1; i >= 0; --i) {
                if (tasks.get(i).isDone()) {
                    tasks.remove(i);
                }
            }
        }
    }
}

package robot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import automodules.AutoModule;
import automodules.stage.Stage;
import util.Timer;
import util.User;
import util.codeseg.ExceptionCodeSeg;
import util.condition.Status;
import util.template.Iterator;

import static global.General.bot;
import static robot.RobotFramework.backgroundThread;
import static robot.RobotFramework.robotFunctionsThread;

public class BackgroundFunctions {

    public volatile ArrayList<BackgroundTask> tasks;

    public BackgroundFunctions(){
        tasks = new ArrayList<>();
    }

    public ExceptionCodeSeg<RuntimeException> updateCode = () -> {
        bot.checkAccess(User.BACK);
        if(!tasks.isEmpty()){
            Iterator.forAll(tasks, BackgroundTask::run);
            Iterator.forAll(tasks, task -> { if(task.isDone()){ tasks.remove(task); }});
        } else {
            backgroundThread.setStatus(Status.IDLE);
        }
    };

    public void init(){
        backgroundThread.setExecutionCode(updateCode);
    }

    public final void addBackgroundTask(BackgroundTask task){
        if (tasks.isEmpty()) {
            backgroundThread.setStatus(Status.ACTIVE);
        }
        tasks.add(task);
    }

    public final void emptyTaskList(){
        tasks.clear();
    }
}

package robot;

import android.os.Build;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import androidx.annotation.RequiresApi;
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
            removeCompletedTasks();
        } else {
            backgroundThread.setStatus(Status.IDLE);
        }
    };

    public void init(){
        backgroundThread.setExecutionCode(updateCode);
        backgroundThread.setStatus(Status.ACTIVE);
    }

    public final void addBackgroundTask(BackgroundTask task){
        tasks.add(task);
    }

    public final void emptyTaskList(){
        tasks.clear();
    }

    private void removeCompletedTasks(){ for(int i = tasks.size() - 1; i >= 0; --i) { if(tasks.get(i).isDone()) { tasks.remove(i); }}}
}

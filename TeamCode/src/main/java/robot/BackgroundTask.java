package robot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import automodules.stage.Exit;
import util.Timer;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;

public class BackgroundTask {

    private final CodeSeg task;
    private final Timer timer = new Timer();
    private final Exit exit;
    private boolean hasBeenStarted = false;

    public BackgroundTask(){ this(() -> {});}

    public BackgroundTask(CodeSeg task){
        this.task = task;
        exit = new Exit(() -> false);
    }

    public BackgroundTask(CodeSeg task, double timeActive){
        this.task = task;
        exit = new Exit(() -> timer.seconds() > timeActive);
    }

    public BackgroundTask(CodeSeg task, Exit exit){
        this.task = task;
        this.exit = exit;
    }

    public void run(){
        if(!hasBeenStarted){
            timer.reset();
            hasBeenStarted = true;
        }
        task.run();
    }

    public boolean isDone(){
        return exit.shouldStop();
    }




}

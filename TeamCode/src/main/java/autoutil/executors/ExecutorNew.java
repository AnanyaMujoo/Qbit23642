package autoutil.executors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.checkerframework.checker.units.qual.C;

import autoutil.paths.Path;
import autoutil.reactors.Reactor;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;

public abstract class ExecutorNew {

    protected Path path;
    protected Reactor reactor;
    protected ReturnCodeSeg<Boolean> whileOpModeIsActive;
    protected CodeSeg backgroundTasks = () -> {};
    protected boolean isIndependent = false;

    public ExecutorNew(LinearOpMode opMode){
        whileOpModeIsActive = opMode::opModeIsActive;
    }

    public ExecutorNew(ReturnCodeSeg<Boolean> active) { whileOpModeIsActive = active; }

    public void setPath(Path path){
        this.path = path;
    }

    public void setReactor(Reactor reactor){
        this.reactor = reactor;
    }

    public abstract void followPath();

    public void setBackgroundTasks(CodeSeg backgroundTasks){ this.backgroundTasks = backgroundTasks; }

    public void makeIndependent(){ isIndependent = true; }
}
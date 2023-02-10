package teleutil.independent;

import java.util.ArrayList;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.RobotPart;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.codeseg.ReturnParameterCodeSeg;

import static global.General.bot;

public class Machine {
    /**
     * Runs a series of independents or other code
     */

    /**
     * Stage number machine is on
     */
    public int stageNumber = 0;


    public ReturnCodeSeg<Boolean> pause = () -> false;
    public boolean waiting = false;
    public int skip = 0;
    /**
     * Is the machine running
     */
    public volatile boolean running = false;
    /**
     * List of stages to run
     */
    public final ArrayList<Stage> stages = new ArrayList<>();

    /**
     * Add instructions
     */
    public Machine addInstruction(Stage stage){ stages.add(stage); return this; }
    public Machine addIndependent(Independent independent){
        return addInstruction(new Stage(
            new Initial(() -> {bot.cancelIndependents(); bot.addIndependent(independent);}),
            new Exit(() -> !bot.indHandler.isIndependentRunning())
        ));
    }
    public Machine addIndependent(int n, Independent independent){ return addIndependent(n, i -> independent); }
    public Machine addIndependent(int n, ReturnParameterCodeSeg<Integer, Independent> independent){ for (int i = 0; i < n; i++) { addIndependent(independent.run(i)); } return this; }
    public Machine addInstruction(CodeSeg code, double time){ return addInstruction(new Stage(new Main(code), RobotPart.exitTime(time))); }

    public Machine addPauseCondition(ReturnCodeSeg<Boolean> condition){ pause = condition; return this; }
    /**
     * Update the machine, go through each stage and cancel when done
     */
    public void update(){
        if (running) {
            if(waiting){
                if(!pause.run()){
                    stageNumber++;
                    waiting = false;
                }
            }else if (stageNumber < stages.size()) {
                Stage stage = stages.get(stageNumber);
                if (!stage.hasStarted()) {
                    stage.start();
                }
                stage.loop();
                if (stage.shouldStop()) {
                    stage.runOnStop();
                    waiting = true;
                }
            } else {
                cancel();
            }
        }
    }

    /**
     * Activate the machine
     */
    public void activate(){ running = true; }

    /**
     * Cancel the machine
     */
    public void cancel(){ running = false; stageNumber = 0; }
}

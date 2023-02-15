package teleutil.independent;

import java.util.ArrayList;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import geometry.framework.CoordinatePlane;
import geometry.framework.Point;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.codeseg.ReturnParameterCodeSeg;
import util.template.Iterator;

import static global.General.bot;

public class Machine {
    /**
     * Runs a series of independents or other code
     */

    /**
     * Stage number machine is on
     */
    public volatile int stageNumber = 0;

    public volatile boolean pause = false;
    public volatile boolean waiting = false;
    public volatile boolean skip = false;
    public volatile int skipTo = 0;
    /**
     * Is the machine running
     */
    public volatile boolean running = false;
    /**
     * List of stages to run
     */
    public final ArrayList<Stage> stages = new ArrayList<>();
    public final ArrayList<Independent> independents = new ArrayList<>();

    /**
     * Add instructions
     */
    public Machine addInstruction(Stage stage){ stages.add(stage); return this; }
    public Machine addIndependent(Independent independent){
        independents.add(independent);
        return addInstruction(new Stage(
            new Initial(() -> {bot.cancelIndependents(); bot.addIndependent(independent);}),
            new Exit(() -> !bot.isIndependentRunning())
        ));
    }
    public Machine addIndependentWithPause(Independent independent){ return addIndependent(independent).addInstruction(this::pause); }
    public Machine addIndependent(int n, Independent independent){ return addIndependent(n, i -> independent); }
    public Machine addIndependent(int n, ReturnParameterCodeSeg<Integer, Independent> independent){ for (int i = 0; i < n; i++) { addIndependent(independent.run(i)); } return this; }
    public Machine addInstruction(CodeSeg code, double time){ return addInstruction(new Stage(new Main(code), RobotPart.exitTime(time))); }
    public Machine addInstruction(CodeSeg code){ return addInstruction(code, 0.0); }

    public void pause(){ pause = true; }
    public void play(){ pause = false; }
    public void pauseOrPlay(){ if(pause){ play(); }else{ pause(); } }
    public void skipTo(int n){ skipTo = n; skip = true; }
    public void skipToLast(){ skipTo(stages.size()-1);}

    public boolean isRunning(){ return running; }

    // TODO FIX
    public CoordinatePlane getAutoPlane(Pose startPose){
        CoordinatePlane plane = new CoordinatePlane();
        Iterator.forAll(independents, i -> {i.setStartPose(startPose); i.setup(); plane.addAll(i.getAutoPlane().getAll());});
        plane.removeRedundantPosesEqualTo(startPose.getCopy());
        return plane;
    }
    public Pose getStartPose(){
        return independents.get(0).getStartPose();
    }
    /**
     * Update the machine, go through each stage and cancel when done
     */
    public void update(){
        if (running) {
            if(waiting){
                if(pause){
                    stageNumber++;
                    waiting = false;
                    if(stageNumber < stages.size()){
                        if(skip && (stageNumber-1) != skipTo){ stageNumber = skipTo; }
                    }else{
                        cancel();
                    }
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
    public void cancel(){ pause = false; waiting = false; skip = false; skipTo = 0; running = false; stageNumber = 0; }
}

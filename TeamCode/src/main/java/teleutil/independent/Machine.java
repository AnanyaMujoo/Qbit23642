package teleutil.independent;

import java.util.ArrayList;
import java.util.Objects;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.RobotPart;
import util.codeseg.CodeSeg;
import util.condition.Status;
import util.template.Iterator;

import static global.General.bot;
import static robot.RobotFramework.robotFunctionsThread;

public class Machine {

    private int stageNumber = 0;
    private boolean running = false;
    private final ArrayList<Stage> stages = new ArrayList<>();

    public Machine addInstruction(Stage stage){ stages.add(stage); return this; }
    public Machine addIndependent(Independent independent){
        return addInstruction(new Stage(
            new Initial(() -> {bot.cancelFunctions(); bot.addIndependent(independent);}),
            new Exit(() -> !bot.indHandler.isIndependentRunning())
        ));
    }
    public Machine addIndependent(int n, Independent independent){ for (int i = 0; i < n; i++) { addIndependent(independent); } return this; }
    public Machine addInstruction(CodeSeg code){ return addInstruction(new Stage(new Main(code), RobotPart.exitAlways())); }

    public void update(){
        if (running) {
            if (stageNumber < stages.size()) {
                Stage stage = stages.get(stageNumber);
                if (!stage.hasStarted()) {
                    stage.start();
                }
                stage.loop();
                if (stage.shouldStop()) {
                    stage.runOnStop();
                }
            } else {
                cancel();
            }
        }
    }

    public void activate(){
        running = true; stageNumber = 0;
    }

    public void cancel(){
        running = false;
    }
}

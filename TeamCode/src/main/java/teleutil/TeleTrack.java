package teleutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.RobotPart;
import teleop.Tele;
import util.codeseg.CodeSeg;
import util.template.Iterator;
import util.template.Mode;


public class TeleTrack {

    public final ArrayList<Step> steps;
    public int stepNumber = 0;

    public boolean enabled = false;

    public TeleTrack(Step... steps){ this.steps = new ArrayList<>(Arrays.asList(steps)); }
    public Stage next(){
        return new Stage(new Main(() -> {
            if(enabled) {
                if (stepNumber < steps.size()) {
                    steps.get(stepNumber).run();
                    stepNumber++;
                } else {
                    disable();
                }
            }
        }), RobotPart.exitAlways());
    }

    public void enable(){ enabled = true; }
    public void disable(){ enabled = false; stepNumber = 0; }

    public boolean isEnabled(){ return enabled; }

    public static class Step{
        private final ArrayList<CodeSeg> codes = new ArrayList<>();
        public Step(CodeSeg seg){ codes.add(seg); }
        public void run(){ Iterator.forAllRun(codes); }
        public Step add(CodeSeg seg){ codes.add(seg); return this; }
    }
}

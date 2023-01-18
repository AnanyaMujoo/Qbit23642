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

    private boolean enabled = true;
    private final ArrayList<Step> steps;
    private int stepNumber = 0;

    public TeleTrack(){ disable(); steps = new ArrayList<>(); }

    public TeleTrack(Step... steps){ this.steps = new ArrayList<>(Arrays.asList(steps)); }
    public Stage nextStep(){ return stepNumber < steps.size() ? new Stage(new Main(() -> {steps.get(stepNumber).run(); stepNumber++;}), RobotPart.exitAlways()) : new Stage(new Main(() -> {stepNumber = 0; disable();}), RobotPart.exitAlways()); }

    public void disable(){ enabled = false; }
    public Stage next(){ return enabled ? nextStep() : RobotPart.empty(); }

    public static class Step{
        private final HashMap<Mode, Mode.ModeType> map = new HashMap<>();
        private final ArrayList<CodeSeg> codes = new ArrayList<>();
        public Step(Mode mode, Mode.ModeType value){ map.put(mode, value); }
        public Step(CodeSeg seg){ codes.add(seg); }
        public void run(){ Iterator.forAll(map, Mode::set); Iterator.forAllRun(codes); }
        public Step add(Mode mode, Mode.ModeType value){ map.put(mode, value); return this; }
        public Step add(CodeSeg seg){ codes.add(seg); return this; }
    }
}

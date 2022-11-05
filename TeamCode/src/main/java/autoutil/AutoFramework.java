package autoutil;

import java.util.ArrayList;

import automodules.AutoModule;
import automodules.stage.Main;
import automodules.stage.Stage;
import auton.Auto;
import autoutil.generators.AutoModuleGenerator;
import autoutil.generators.Generator;
import autoutil.generators.PauseGenerator;
import autoutil.reactors.Reactor;
import autoutil.vision.CaseScanner;
import elements.CaseOld;
import elements.FieldSide;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.codeseg.CodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.condition.DecisionList;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Iterator;

import static global.General.fault;
import static global.General.log;

public abstract class AutoFramework extends Auto implements AutoUser {
    // TOD 5 Integrate with coordinate plane
    protected AutoConfig config;

    public void setConfig(AutoConfig config){ this.config = config; }

    protected final ArrayList<Pose> poses = new ArrayList<>();
    protected ArrayList<AutoSegment<?, ?>> segments = new ArrayList<>();

    protected boolean scanning = false;
    protected CaseScanner caseScanner;
    protected CaseOld caseDetected = CaseOld.RIGHT;

    protected boolean isIndependent = false;

    public abstract void define();

    public void makeIndependent(){ isIndependent = true; }
    public boolean isFlipped(){ return fieldSide.equals(FieldSide.RED); }

    public void addDecision(DecisionList decisionList){ decisionList.check(); }
    public void addAutomodule(DecisionList decisionList){ addAutoModule(new AutoModule(new Stage(new Main(decisionList::check), RobotPart.exitAlways()))); }
    public void customSide(FieldSide sideOne, CodeSeg one, FieldSide sideTwo, CodeSeg two){ addDecision(new DecisionList(() -> fieldSide).addOption(sideOne, one).addOption(sideTwo, two)); }
    public void customCase(CaseOld caseOne, CodeSeg one, CaseOld caseTwo, CodeSeg two, CaseOld caseThree, CodeSeg three){ addDecision(new DecisionList(() -> caseDetected).addOption(caseOne, one).addOption(caseTwo, two).addOption(caseThree, three)); }
    public void customNumber(int num, ParameterCodeSeg<Integer> one){ for (int i = 0; i < num; i++) { one.run(i); } }

    public void scan(){
        scanning = true;
        caseScanner = config.getCaseScanner();
        camera.setExternalScanner(caseScanner);
        camera.startExternalCamera();
        while (!isStarted()){
            caseDetected = caseScanner.getCase();
            log.show("Case Detected: ", caseDetected);
            log.showTelemetry();
        }
    }

    @Override
    public void runAuto() {
        define();
        if(scanning) { camera.stopExternalCamera(); }
        Iterator.forAll(segments, segment -> segment.run(this));
    }

    public void addPause(double time){ addSegment(null, new PauseGenerator(time), getLastPose()); }
    public void addSetpoint(double x, double y, double h){ if(isFlipped()){ x = -x; h = -h; } addSegment(config.getSetpointSegment(), new Pose(x, y, h)); }
    public void addWaypoint(double x, double y, double h){ if(isFlipped()){ x = -x; h = -h; } addSegment(config.getWaypointSegment(), new Pose(x, y, h));}
    public void addAutoModule(AutoModule autoModule){ addStationarySegment(new AutoModuleGenerator(autoModule, false)); }
    public void addConcurrentAutoModule(AutoModule autoModule){ addStationarySegment(new AutoModuleGenerator(autoModule, true)); }
    public void addCancelAutoModules(){
        addStationarySegment(new AutoModuleGenerator(true));
    }
    public void addStationarySegment(Generator generator){ addSegment(null, generator, getLastPose()); }

    private void addSegment(AutoSegment<?, ?> segment, Pose target){ addSegment(segment.getReactor(), segment.getGenerator(), target); }
    private <R extends Reactor, G extends Generator> void addSegment(R reactor, G generator, Pose target){
        fault.check("Auto Config Not Set", Expectation.EXPECTED, Magnitude.MODERATE, config == null, false);
        generator.addSegment(getLastPose(), target); segments.add(new AutoSegment<>(reactor, generator));
        poses.add(target);
    }

    private Pose getLastPose(){ return poses.get(poses.size() - 1); }
}

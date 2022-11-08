package autoutil;



import java.util.ArrayList;

import androidx.annotation.NonNull;
import automodules.AutoModule;
import automodules.stage.Main;
import automodules.stage.Stage;
import auton.Auto;
import autoutil.generators.AutoModuleGenerator;
import autoutil.generators.Generator;
import autoutil.generators.PauseGenerator;
import autoutil.reactors.Reactor;
import autoutil.vision.CaseScanner;
import elements.Case;
import elements.CaseOld;
import elements.FieldSide;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.codeseg.CodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.condition.DecisionList;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Iterator;

import static global.General.fault;
import static global.General.log;

public abstract class AutoFramework extends Auto implements AutoUser {
    // TODO 4 Integrate with coordinate plane
    // TODO 4 REMOVE flipped, use coordinate plane
    protected AutoConfig config;

    public void setConfig(AutoConfig config){ this.config = config; }

    protected final ArrayList<Pose> poses = new ArrayList<>();
    protected ArrayList<AutoSegment.Type> segmentTypes = new ArrayList<>();
    protected ArrayList<AutoSegment<?, ?>> segments = new ArrayList<>();

    protected boolean scanning = false;
    protected CaseScanner caseScanner;
    protected Case caseDetected = Case.FIRST;

    protected boolean isIndependent = false;

    {
        poses.add(new Pose());
    }

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
        caseScanner = new CaseScanner();
        camera.setExternalScanner(caseScanner);
        camera.startExternalCamera();
        while (!isStarted()){ caseDetected = caseScanner.getCase(); caseScanner.message(); log.showTelemetry(); }
    }

    @Override
    public void runAuto() {
        define();
        Iterator.forAll(segmentTypes, this::createSegment);
        if(scanning) { camera.stopExternalCamera(); }
        Iterator.forAll(segments, segment -> segment.run(this));
    }


    private void addSegmentType(double time){ AutoSegment.Type.PAUSE.set(time, getLastPose()); segmentTypes.add(AutoSegment.Type.PAUSE); }
    private void addSegmentType(AutoSegment.Type type, AutoModule autoModule){ type.set(autoModule, getLastPose()); segmentTypes.add(type); }
    private void addSegmentType(AutoSegment.Type type, Pose pose){ type.set(pose); segmentTypes.add(type); }

    public void addPause(double time){ addSegmentType(time);}
    public void addSetpoint(double x, double y, double h){ if(isFlipped()){ x = -x; h = -h; } addSegmentType(AutoSegment.Type.SETPOINT, new Pose(x,y,h));}
    public void addWaypoint(double x, double y, double h){ if(isFlipped()){ x = -x; h = -h; } addSegmentType(AutoSegment.Type.WAYPOINT, new Pose(x,y,h));}
    public void addAutoModule(AutoModule autoModule){ addSegmentType(AutoSegment.Type.AUTOMODULE, autoModule);}
    public void addConcurrentAutoModule(AutoModule autoModule){ addSegmentType(AutoSegment.Type.CONCURRENT_AUTOMODULE, autoModule);}
    public void addCancelAutoModules(){ addSegmentType(AutoSegment.Type.CANCEL_AUTOMODULE, getLastPose()); }

    public void addStationarySegment(ReturnCodeSeg<Generator> generator, Pose pose){ addSegment(config.getSetpointSegment().getReactorReference(), generator, pose); }

    private void createSegment(AutoSegment.Type type){
        switch (type){
            case PAUSE:
                addStationarySegment(() -> new PauseGenerator(type.getTime()), type.getPose()); break;
            case SETPOINT:
                addSegment(config.getSetpointSegment(), type.getPose()); break;
            case WAYPOINT:
                addSegment(config.getWaypointSegment(), type.getPose()); break;
            case AUTOMODULE:
                addStationarySegment(() -> new AutoModuleGenerator(type.getAutoModule(), false), type.getPose()); break;
            case CONCURRENT_AUTOMODULE:
                addStationarySegment(() -> new AutoModuleGenerator(type.getAutoModule(), true), type.getPose()); break;
            case CANCEL_AUTOMODULE:
                addStationarySegment(() -> new AutoModuleGenerator(true), type.getPose()); break;
        }
    }

    private void addSegment(AutoSegment<?, ?> segment, Pose target){ addSegment(segment.getReactorReference(), segment.getGeneratorReference(), target); }
    private <R extends Reactor, G extends Generator> void addSegment(@NonNull ReturnCodeSeg<R> reactor, @NonNull ReturnCodeSeg<G> generator, Pose target){
        fault.check("Auto Config Not Set", Expectation.EXPECTED, Magnitude.MODERATE, config == null, false);
        AutoSegment<?,?> autoSegment = new AutoSegment<>(reactor, generator);
        final Pose lastPose = getLastPose();
        autoSegment.setGeneratorFunction(gen -> gen.addSegment(lastPose, target));
        segments.add(autoSegment);
        poses.add(target);
    }

    private Pose getLastPose(){ return poses.get(poses.size() - 1); }
}

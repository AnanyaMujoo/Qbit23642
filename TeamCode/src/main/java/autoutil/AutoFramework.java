package autoutil;



import org.firstinspires.ftc.teamcode.R;

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
import geometry.framework.CoordinatePlane;
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
    protected AutoConfig config;

    public void setConfig(AutoConfig config){ this.config = config; }

    protected final CoordinatePlane autoPlane = new CoordinatePlane();

    protected final ArrayList<Pose> poses = new ArrayList<>();
    protected ArrayList<AutoSegment.Type> segmentTypes = new ArrayList<>();
    protected ArrayList<AutoSegment<?, ?>> segments = new ArrayList<>();

    protected boolean scanning = false;
    protected CaseScanner caseScanner;
    protected Case caseDetected = Case.FIRST;

    protected boolean isIndependent = false;

    private int segmentIndex = 1;

    {
        poses.add(new Pose());
    }

    public void preProcess(){}

    public abstract void define();

    public void postProcess(){}

    public final void setup(){
        preProcess();
        define();
        autoPlane.addAll(poses);
        postProcess();
        if(isFlipped()){ flip(); }
    }

    public void makeIndependent(){ isIndependent = true; }
    public boolean isFlipped(){ return fieldSide.equals(FieldSide.RED); }
    public void flip(){ autoPlane.reflectX(); autoPlane.reflectPoses(); }
    public void flipCases(){ if(caseDetected.equals(Case.FIRST)){ caseDetected = Case.THIRD; }else if(caseDetected.equals(Case.THIRD)){ caseDetected = Case.FIRST; }}

    public void addDecision(DecisionList decisionList){ decisionList.check(); }
    public void addAutomodule(DecisionList decisionList){ addAutoModule(new AutoModule(new Stage(new Main(decisionList::check), RobotPart.exitAlways()))); }
    public void customSide(FieldSide sideOne, CodeSeg one, FieldSide sideTwo, CodeSeg two){ addDecision(new DecisionList(() -> fieldSide).addOption(sideOne, one).addOption(sideTwo, two)); }
    public void customCase(CodeSeg first, CodeSeg second, CodeSeg third){ addDecision(new DecisionList(() -> caseDetected).addOption(Case.FIRST, first).addOption(Case.SECOND, second).addOption(Case.THIRD, third)); }
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
        setup();
        createSegments();
        if(scanning) { camera.stopExternalCamera(); }
        Iterator.forAll(segments, segment -> segment.run(this));
    }


    private void addSegmentType(double time){ AutoSegment.Type.PAUSE.set(time); segmentTypes.add(AutoSegment.Type.PAUSE); addLastPose();  }
    private void addSegmentType(AutoSegment.Type type, AutoModule autoModule){ type.set(autoModule); segmentTypes.add(type); addLastPose();  }
    private void addSegmentType(AutoSegment.Type type){ segmentTypes.add(type); }

    public void addPause(double time){ addSegmentType(time); }
    public void addSetpoint(double x, double y, double h){ addSegmentType(AutoSegment.Type.SETPOINT); poses.add(new Pose(x,y,h)); }
    public void addWaypoint(double x, double y, double h){ addSegmentType(AutoSegment.Type.WAYPOINT); poses.add(new Pose(x,y,h)); }
    public void addAutoModule(AutoModule autoModule){ addSegmentType(AutoSegment.Type.AUTOMODULE, autoModule); }
    public void addConcurrentAutoModule(AutoModule autoModule){ addSegmentType(AutoSegment.Type.CONCURRENT_AUTOMODULE, autoModule);}
    public void addCancelAutoModules(){ addSegmentType(AutoSegment.Type.CANCEL_AUTOMODULE); addLastPose(); }

    public void addStationarySegment(ReturnCodeSeg<Generator> generator){ addSegment(config.getSetpointSegment().getReactorReference(), generator); }

    private void createSegments(){
        Iterator.forAll(segmentTypes, type -> {
            switch (type){
                case PAUSE:
                    addStationarySegment(() -> new PauseGenerator(type.getTime())); break;
                case SETPOINT:
                    addSegment(config.getSetpointSegment()); break;
                case WAYPOINT:
                    addSegment(config.getWaypointSegment()); break;
                case AUTOMODULE:
                    addStationarySegment(() -> new AutoModuleGenerator(type.getAutoModule(), false)); break;
                case CONCURRENT_AUTOMODULE:
                    addStationarySegment(() -> new AutoModuleGenerator(type.getAutoModule(), true)); break;
                case CANCEL_AUTOMODULE:
                    addStationarySegment(() -> new AutoModuleGenerator(true)); break;
            }
            segmentIndex++;
        });
    }

    private void addSegment(AutoSegment<?, ?> segment){ addSegment(segment.getReactorReference(), segment.getGeneratorReference()); }
    private <R extends Reactor, G extends Generator> void addSegment(@NonNull ReturnCodeSeg<R> reactor, @NonNull ReturnCodeSeg<G> generator){
        fault.check("Auto Config Not Set", Expectation.EXPECTED, Magnitude.MODERATE, config == null, false);
        AutoSegment<?,?> autoSegment = new AutoSegment<>(reactor, generator);
        final Pose lastPose = poses.get(segmentIndex-1); final Pose currentPose = poses.get(segmentIndex);
        autoSegment.setGeneratorFunction(gen -> gen.addSegment(lastPose, currentPose));
        segments.add(autoSegment);
    }

    public void addLastPose(){ poses.add(poses.get(poses.size()-1).getCopy()); }

    public ArrayList<Pose> getPoses(){ return poses; }
    public ArrayList<AutoSegment.Type> getSegmentTypes(){ return segmentTypes; }
    public CoordinatePlane getAutoPlane(){ return autoPlane; }
}

package autoutil;



import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import automodules.AutoModule;
import automodules.stage.Main;
import automodules.stage.Stage;
import auton.Auto;
import autoutil.generators.AutoModuleGenerator;
import autoutil.generators.BreakpointGenerator;
import autoutil.generators.Generator;
import autoutil.generators.PauseGenerator;
import autoutil.reactors.Reactor;
import autoutil.vision.CaseScannerRectBottom;
import autoutil.vision.Scanner;
import elements.Case;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.TeamProp;
import geometry.framework.CoordinatePlane;
import geometry.framework.Point;
import geometry.position.Bezier;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.Timer;
import util.codeseg.CodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.codeseg.ReturnCodeSeg;
import util.condition.DecisionList;
import util.condition.Expectation;
import util.condition.Magnitude;
import util.template.Iterator;

import static global.General.fault;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;

public abstract class AutoFramework extends Auto implements AutoUser {
    protected AutoConfig config;

    public void setConfig(AutoConfig config){ this.config = config; }

    protected Pose startPose = new Pose();

    protected CoordinatePlane autoPlane = new CoordinatePlane();

    protected ArrayList<Pose> poses = new ArrayList<>();
    protected ArrayList<AutoSegment.Type> segmentTypes = new ArrayList<>();
    protected ArrayList<AutoSegment<?, ?>> segments = new ArrayList<>();
    protected ArrayList<Double> pauses = new ArrayList<>();
    protected ArrayList<AutoModule> autoModules = new ArrayList<>();
    protected ArrayList<Double> movementScales = new ArrayList<>();
    protected ArrayList<Double> accuracyScales = new ArrayList<>();
    protected ArrayList<Double> times = new ArrayList<>();
    protected ArrayList<AutoSegment<?,?>> customSegments = new ArrayList<>();
    protected ArrayList<CodeSeg> breakpoints = new ArrayList<>();

    protected boolean scanning = false;
    protected boolean haltCameraAfterInit = true;
    protected Scanner caseScannerRect;
    protected Scanner scannerAfterInit;
    protected TeamProp caseDetected = TeamProp.LEFT;

    private int segmentIndex = 1;
    private int pauseIndex, autoModuleIndex, customSegmentIndex, breakpointIndex = 0;

    protected Timer timer = new Timer();
    public boolean skipping = false;

    // TOD5 better breakpoint system


    {
        poses.add(new Pose()); movementScales.addAll(Collections.nCopies(200,1.0)); accuracyScales.addAll(Collections.nCopies(200,1.0)); times.addAll(Collections.nCopies(200,100.0));
    }

    public void preProcess(){}

    public abstract void define();

    public void postProcess(){}

    public final void setup(){
        preProcess();
        if(isFlipped()){ flipCases(); }
        define();
        autoPlane.addAll(poses);
        postProcess();
        if(isFlipped()){ flip(); }
//        autoPlane.rotate(-90);


        // TODO FIX
//        autoPlane.setStart(startPose);



//        autoPlane.toPoses(p -> p.rotateOrientation(90));
    }

    public static boolean isFlipped(){ return fieldSide.equals(FieldSide.RED) ^ fieldPlacement.equals(FieldPlacement.UPPER); }
    public void flip(){ autoPlane.reflectX(); autoPlane.reflectPoses(); }
    public void flipCases(){ if(caseDetected.equals(TeamProp.LEFT)){ caseDetected = TeamProp.RIGHT; }else if(caseDetected.equals(TeamProp.RIGHT)){ caseDetected = TeamProp.LEFT; }}

    public void addDecision(DecisionList decisionList){ decisionList.check(); }
    public void addAutomodule(DecisionList decisionList){ addAutoModule(new AutoModule(new Stage(new Main(decisionList::check), RobotPart.exitAlways()))); }
    public void customSide(CodeSeg one, CodeSeg two){ addDecision(new DecisionList(() -> fieldSide).addOption(FieldSide.BLUE, one).addOption(FieldSide.RED, two)); }
    public void customFlipped(CodeSeg one, CodeSeg two){ if(!isFlipped()){ one.run();}else{two.run();}}
    public void customPlacement(CodeSeg one, CodeSeg two){ addDecision(new DecisionList(() -> fieldPlacement).addOption(FieldPlacement.LOWER, one).addOption(FieldPlacement.UPPER, two)); }
    public void customSidePlacement(CodeSeg one, CodeSeg two, CodeSeg three, CodeSeg four){customSide(() -> customPlacement(one, two), () -> customPlacement(three, four));}
    public void customCase(CodeSeg first, CodeSeg second, CodeSeg third){ addDecision(new DecisionList(() -> caseDetected).addOption(Case.FIRST, first).addOption(Case.SECOND, second).addOption(Case.THIRD, third)); }
    public void customNumber(int num, ParameterCodeSeg<Integer> one){ for (int i = 0; i < num; i++) { one.run(i); } }
    public void customIf(boolean value, CodeSeg ifTrue, CodeSeg ifFalse){ if(value){ifTrue.run();}else{ifFalse.run();} }

    public void setScannerAfterInit(Scanner scanner){
        haltCameraAfterInit = false;
        scannerAfterInit = scanner;
    }

    public void scan(Scanner scanner, boolean view, String color, String side){
        scanning = true;
        caseScannerRect = scanner;
        caseScannerRect.setColor(color);
        caseScannerRect.setSide(side);
        camera.setScanner(caseScannerRect);
        camera.start(view);
    }
    public void scanRect(boolean view, String color, String side){
        caseScannerRect = new CaseScannerRectBottom();
        camera.start(true);
        camera.setScanner(caseScannerRect);
        caseScannerRect.setColor(color);
        caseScannerRect.setSide(side);
        caseScannerRect.start();
    }


    @Override
    public final void initAuto() {
        initialize();
        if(scanning){
            while (!isStarted() && !isStopRequested()){ caseDetected = caseScannerRect.getCase(); caseScannerRect.log(); log.showTelemetry(); }
            if(haltCameraAfterInit) { camera.halt(); }else{ camera.setScanner(scannerAfterInit); }
        }
        setup();
        createSegments();
    }

    public abstract void initialize();


    protected void resetBeforeRun(){
        odometry.reset();
//        odometry.reset(startPose);
    }

    @Override
    public void runAuto() {
        resetBeforeRun();
        pause(0.05);
        timer.reset();
        Iterator.forAll(segments, segment -> segment.run(this));
    }


    private void addSegmentType(double time){ pauses.add(time); segmentTypes.add(AutoSegment.Type.PAUSE); addLastPose();  }
    private void addSegmentType(AutoSegment.Type type, AutoModule autoModule){ autoModules.add(autoModule); segmentTypes.add(type); addLastPose();  }
    private void addSegmentType(AutoSegment.Type type){ segmentTypes.add(type); }

    public void addPause(double time){ addSegmentType(time); }
    public void addSetpoint(double x, double y, double h){ addSegmentType(AutoSegment.Type.SETPOINT); poses.add(new Pose(x,y,h)); }
    public void addWaypoint(double x, double y, double h){ addSegmentType(AutoSegment.Type.WAYPOINT); poses.add(new Pose(x,y,h)); }
    public void addAutoModule(AutoModule autoModule){ addSegmentType(AutoSegment.Type.AUTOMODULE, autoModule); }
    public void addConcurrentAutoModule(AutoModule autoModule){ addSegmentType(AutoSegment.Type.CONCURRENT_AUTOMODULE, autoModule);}
    public void addConcurrentAutoModuleWithCancel(AutoModule autoModule, double pauseAfter){ addCancelAutoModules(); addConcurrentAutoModule(autoModule); addPause(pauseAfter);}
    public void addConcurrentAutoModuleWithCancel(AutoModule autoModule){ addCancelAutoModules(); addConcurrentAutoModule(autoModule);}
    public void addCancelAutoModules(){ addSegmentType(AutoSegment.Type.CANCEL_AUTOMODULE); addLastPose(); }
    public void addSegment(double time, double scale, AutoSegment<?,?> segment, double x, double y, double h){addTime(time); addSegment(scale, segment, x, y, h); }
    public void addSegment(double scale, AutoSegment<?,?> segment, double x, double y, double h){addScale(scale); addSegment(segment, x, y, h); }
    public void addSegment(AutoSegment<?,?> segment, double x, double y, double h){ customSegments.add(segment); segmentTypes.add(AutoSegment.Type.CUSTOM); poses.add(new Pose(x, y, h)); }


    private void addStationarySegment(ReturnCodeSeg<Generator> generator){ addSegment(config.getSetpointSegment().getReactorReference(), generator); }

    public void addScale(double scale){ movementScales.set(poses.size()-1, scale); }
    public void addAccuracy(double scale){ accuracyScales.set(poses.size()-1, scale); }
    public void addTime(double time){ times.set(poses.size()-1, time);}

    public void addSetpoint(double scale, double x, double y, double h){ addScale(scale); addSetpoint(x, y, h);}
    public void addWaypoint(double scale, double x, double y, double h){ addScale(scale); addWaypoint(x, y, h);}

    public void addSetpoint(double acc, double scale, double x, double y, double h){ addAccuracy(acc); addSetpoint(scale, x, y, h);}
    public void addTimedSetpoint(double acc, double scale, double time, double x, double y, double h){ addTime(time); addSetpoint(acc, scale, x, y, h); }
    public void addTimedWaypoint(double scale, double time, double x, double y, double h){ addTime(time); addWaypoint(scale, x, y, h);}

    public void addBezierWaypoints(double scale, double time, Point start, Point control1, Point control2, Point end, double h, int indices){
        Bezier newBezier = new Bezier(start, control1, control2, end);
        for (int t=0; t<1; t+=1/indices) {
            Point currentPoint= newBezier.getAt(t,time);
            double x=currentPoint.getX();
            double y= currentPoint.getY();
            addWaypoint(scale, x,y,h/indices);
        }
        Point currentPoint= newBezier.getAt(time);
        double x=currentPoint.getX();
        double y= currentPoint.getY();
        addTimedSetpoint(1, scale, time, x, y, h/indices);
    }

    public void addBezierSegments(double scale, double time, Point start, Point control1, Point control2, Point end, double h, int indices){
        Bezier newBezier = new Bezier(start, control1, control2, end);
        for (int t=0; t<1; t+=1/indices) {
            Point currentPoint= newBezier.getAt(t,time);
            double x=currentPoint.getX();
            double y= currentPoint.getY();
            AutoSegment<?,?> segment= new AutoSegment<>(mecanumPurePursuitReactor, lineGenerator);
            addSegment(segment, x, y, h/indices);
        }
        Point currentPoint= newBezier.getAt(time);
        double x=currentPoint.getX();
        double y= currentPoint.getY();
        addTimedSetpoint(1, scale, time/indices, x, y, h/indices);
    }

    private void addCustomCodeInternal(CodeSeg code){ addSegmentType(AutoSegment.Type.BREAKPOINT);  breakpoints.add(code); addLastPose(); }
    public void addCustomCode(CodeSeg code){ addCustomCodeInternal(() -> {
        if(!skipping){
            code.run();
        }
    });}
    public void addCustomCode(CodeSeg code, double time){ addCustomCode(code); addPause(time); }
    public void addSynchronisedDecision(DecisionList decisionList){ addCustomCode(decisionList::check); }
    public void addBreakpoint(ReturnCodeSeg<Boolean> exit){ addCustomCodeInternal(() -> {
        if(exit.run()){
            skipping = true;
            Iterator.forAll(segments, AutoSegment::skip);
        }
    });}
    public void addBreakpointReturn(){ addCustomCodeInternal(() -> Iterator.forAll(segments, AutoSegment::reset));}

    private void createSegments(){
        Iterator.forAll(segmentTypes, type -> {
            switch (type){
                case PAUSE:
                    final double time = getCurrentPause(); addStationarySegment(() -> new PauseGenerator(time)); break;
                case SETPOINT:
                    addSegment(config.getSetpointSegment()); break;
                case WAYPOINT:
                    addSegment(config.getWaypointSegment()); break;
                case AUTOMODULE:
                    final AutoModule autoModule = getCurrentAutoModule(); addStationarySegment(() -> new AutoModuleGenerator(autoModule, false)); break;
                case CONCURRENT_AUTOMODULE:
                    final AutoModule autoModule2 = getCurrentAutoModule(); addStationarySegment(() -> new AutoModuleGenerator(autoModule2, true));  break;
                case CANCEL_AUTOMODULE:
                    addStationarySegment(() -> new AutoModuleGenerator(true)); break;
                case CUSTOM:
                    addSegment(getCurrentCustomSegment()); break;
                case BREAKPOINT:
                    final CodeSeg code = getCurrentBreakpoint(); addStationarySegment(() -> new BreakpointGenerator(code)); break;
            }
            segmentIndex++;
        });
    }

    private AutoModule getCurrentAutoModule(){ AutoModule autoModule = autoModules.get(autoModuleIndex); autoModuleIndex++; return autoModule; }
    private double getCurrentPause(){ double time = pauses.get(pauseIndex); pauseIndex++; return time; }
    private AutoSegment<?,?> getCurrentCustomSegment(){AutoSegment<?,?> segment = customSegments.get(customSegmentIndex); customSegmentIndex++; return segment; }
    private CodeSeg getCurrentBreakpoint(){ CodeSeg breakpoint = breakpoints.get(breakpointIndex); breakpointIndex++; return breakpoint; }

    private void addSegment(AutoSegment<?, ?> segment){ addSegment(segment.getReactorReference(), segment.getGeneratorReference()); }
    private <R extends Reactor, G extends Generator> void addSegment(@NonNull ReturnCodeSeg<R> reactor, @NonNull ReturnCodeSeg<G> generator){
        fault.check("Auto Config Not Set", Expectation.EXPECTED, Magnitude.MODERATE, config == null, false);
        AutoSegment<?,?> autoSegment = new AutoSegment<>(reactor, generator);
        final Pose lastPose = poses.get(segmentIndex-1); final Pose currentPose = poses.get(segmentIndex);
        autoSegment.setGeneratorFunction(gen -> gen.addSegment(lastPose, currentPose));
        final double scale = movementScales.get(segmentIndex-1);
        final double accuracy = accuracyScales.get(segmentIndex-1);
        final double time = times.get(segmentIndex - 1);
        autoSegment.setReactorFunction(rea -> {rea.scale(scale); rea.scaleAccuracy(accuracy); rea.setTime(time);});
        segments.add(autoSegment);
    }

    public void addLastPose(){ poses.add(poses.get(poses.size()-1).getCopy()); }

    public ArrayList<Pose> getPoses(){ return poses; }
    public ArrayList<AutoSegment.Type> getSegmentTypes(){ return segmentTypes; }
    public CoordinatePlane getAutoPlane(){ return autoPlane; }

    public Pose getStartPose(){
        return startPose;
    }

    public Pose getEndPose(){ return poses.get(poses.size() - 1); }

    public void setStartPose(Pose start){ startPose = start.getCopy(); }

    @Override
    public void stopAuto() {
        if(scanning && !haltCameraAfterInit){ camera.halt(); }
    }


    public static void wait(double time){
        Timer timer = new Timer(); timer.reset();
        while(timer.seconds() < time){}
    }

    public void reset(){
        config = null;
        autoPlane = new CoordinatePlane();
        poses = new ArrayList<>();
        segmentTypes = new ArrayList<>();
        segments = new ArrayList<>();
        pauses = new ArrayList<>();
        autoModules = new ArrayList<>();
        movementScales = new ArrayList<>();
        accuracyScales = new ArrayList<>();
        times = new ArrayList<>();
        customSegments = new ArrayList<>();
        breakpoints = new ArrayList<>();
        scanning = false;
        haltCameraAfterInit = true;
        caseScannerRect = null;
        scannerAfterInit = null;
        caseDetected = TeamProp.LEFT;
        segmentIndex = 1;
        pauseIndex = 0;
        autoModuleIndex = 0;
        customSegmentIndex = 0;
        breakpointIndex = 0;
        skipping = false;
        startPose = new Pose();
        poses.add(new Pose()); movementScales.addAll(Collections.nCopies(200,1.0)); accuracyScales.addAll(Collections.nCopies(200,1.0)); times.addAll(Collections.nCopies(200,100.0));
    }
}

package auton;

import java.util.ArrayList;

import automodules.StageList;
import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.executors.ExecutorNew;
import autoutil.generators.Generator;
import autoutil.generators.PoseGenerator;
import autoutil.reactors.Reactor;
import autoutil.reactors.mecanum.MecanumPIDReactor;
import autoutil.vision.CaseScanner;
import elements.Case;
import elements.FieldSide;
import geometry.position.Point;
import geometry.position.Pose;
import robot.RobotFramework;
import robotparts.RobotPart;
import util.Timer;
import util.User;
import util.codeseg.CodeSeg;
import util.codeseg.ParameterCodeSeg;
import util.condition.DecisionList;

import static global.General.bot;
import static global.General.log;
import static global.General.mainUser;

public abstract class AutoFramework extends Auto{

    protected FieldSide fieldSide = FieldSide.UNKNOWN;

    public abstract ExecutorNew getExecutor();

    protected ExecutorNew executor;

    protected Pose lastPose = new Pose(new Point(0,0),0);

    protected ArrayList<AutoSegment<? extends Reactor, ? extends Generator>> segments = new ArrayList<>();

    protected boolean scanning = false;
    protected CaseScanner caseScanner;
    protected Case caseDetected = Case.RIGHT;

    protected boolean isIndependent = false;

    public final double scale = 0.95;
//    public boolean canceled = false;

    public abstract void define();
    public abstract Reactor getSetpointReactor();
    public abstract Reactor getWaypointReactor();
    public abstract Generator getSetpointGenerator();
    public abstract Generator getWaypointGenerator();
    public abstract CaseScanner getCaseScanner();

    public void makeIndependent(){
        isIndependent = true;
    }

    public void setBackgroundTasks(CodeSeg backgroundTasks){
        RobotFramework.backgroundThread.setExecutionCode(() -> {
            if(!isStopRequested()) {
                bot.checkAccess(mainUser);
                backgroundTasks.run();
                update(false);
            }else{
                RobotFramework.backgroundThread.stopUpdating();
            }
        });
//        executor.setBackgroundTasks(() -> {
//            backgroundTasks.run();
//            update(false);
//        });
    }

    public boolean isFlipped(){
        return fieldSide.equals(FieldSide.RED);
    }

    public void addDecision(DecisionList decisionList){
        decisionList.check();
    }

    public void addAutomodule(DecisionList decisionList){
        addAutoModule(new StageList(new Stage(new Main(decisionList::check), RobotPart.exitAlways())));
    }

    public void customSide(FieldSide sideOne, CodeSeg one, FieldSide sideTwo, CodeSeg two){
        addDecision(new DecisionList(() -> fieldSide).addOption(sideOne, one).addOption(sideTwo, two));
    }

    public void customCase(Case caseOne, CodeSeg one, Case caseTwo, CodeSeg two, Case caseThree, CodeSeg three){
        addDecision(new DecisionList(() -> caseDetected).addOption(caseOne, one).addOption(caseTwo, two).addOption(caseThree, three));
    }


    public void customNumber(int num, ParameterCodeSeg<Integer> one){
        for (int i = 0; i < num; i++) { one.run(i); }
    }

    public void scan(){
        scanning = true;
        caseScanner = getCaseScanner();
        bot.camera.setExternalScanner(caseScanner);
        bot.camera.startExternalCamera();
        while (!isStarted()){
            caseDetected = caseScanner.getCase();
            log.show("Case Detected: ", caseDetected);
            log.showTelemetry();
        }
    }

    @Override
    public void runAuto() {
        define();
        if(scanning) {
            bot.camera.stopExternalCamera();
        }
        for(AutoSegment<? extends Reactor, ? extends Generator> autoSegment: segments){
            executor = getExecutor();
            if(isIndependent){
                executor.makeIndependent();
            }
            executor.setReactor(autoSegment.getReactor());
            executor.setPath(autoSegment.getGenerator().getPath());
            executor.followPath();
        }
    }

    public void addPause(double time){
        getLastSegment().getGenerator().addPause(time);
    }

    public void addSetpoint(double x, double y, double h){
        x *= scale;
        y *= scale;
        if(isFlipped()){
            x = -x;
            h = -h;
        }
        addSegment(getSetpointReactor(), getSetpointGenerator(), x, y, h);
    }

    public void addWaypoint(double x, double y, double h){
        x *= scale;
        y *= scale;
        if(isFlipped()){
            x = -x;
            h = -h;
        }
        addSegment(getWaypointReactor(), getWaypointGenerator(), x, y, h);
    }

    public void addAutoModule(StageList autoModule){
        getLastSegment().getGenerator().addAutoModule(autoModule);
    }

    public void addConcurrentAutoModule(StageList autoModule){
        getLastSegment().getGenerator().addConcurrentAutoModule(autoModule);
    }

    public void addCancelAutoModules(){
        getLastSegment().getGenerator().addCancelAutoModule();
    }



    private <R extends Reactor, G extends Generator> void addSegment(R reactor, G generator, double x, double y, double h){
        if(isFirstSegment()){
            generator.add(x,y,h);
            segments.add(new AutoSegment<>(reactor,generator));
        }else {
            if ((getLastSegment().getGenerator().getClass() == generator.getClass())) {
                getLastSegment().getGenerator().add(x,y,h);
            } else {
                generator.add(x,y,h);
                segments.add(new AutoSegment<>(reactor, generator));
            }
        }
        lastPose = new Pose(new Point(x,y),h);
    }

    private AutoSegment<? extends Reactor, ? extends Generator> getLastSegment(){
        if(isFirstSegment()){
            segments.add(new AutoSegment<>(new MecanumPIDReactor(), new PoseGenerator()));
        }
        return segments.get(segments.size() - 1);
    }

    public boolean isFirstSegment(){
        return segments.isEmpty();
    }
}

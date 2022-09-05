package robot;

import java.util.ArrayList;

import automodules.StageList;
import geometry.CoordinatePlane;
import robotparts.RobotPart;
import teleutil.independent.Independent;
import teleutil.independent.IndependentRunner;
import util.TerraThread;
import util.User;
import util.codeseg.ParameterCodeSeg;

import static global.General.*;

public class RobotFramework {
    /**
     * The allRobotParts arraylist contains all of the robotParts in the robot,
     * The RobotPart constructor automatically adds itself to this static arraylist
     */
    public static ArrayList<RobotPart> allRobotParts;
    /**
     * The localPlane Coordinate plane represents the robots local perspective
     */
    public CoordinatePlane localPlane;
    /**
     * The terrathread robotFunctionsThread is used for running robotfunction related tasks,
     * it is usually not necessary to access this directly
     */
    public static TerraThread robotFunctionsThread;
    /**
     * The odometry thread is used to update odometry
     */
    public static TerraThread odometryThread;


    public static TerraThread backgroundThread;
    /**
     * rfsHandler is used for running rfs code. Stages can be added to the queue
     */
    public RobotFunctions rfsHandler;


    public IndependentRunner independentRunner;

    /**
     * Constructor for creating a robotFramework, this should be overridden by terraBot by extending this class
     * Objects are instantiated here, and rfsHandler is also initialized, which sets the update code
     */
    protected RobotFramework(){
        allRobotParts = new ArrayList<>();
        TerraThread.init();
        localPlane = new CoordinatePlane();
        rfsHandler = new RobotFunctions();
        robotFunctionsThread = new TerraThread("RobotFunctionsThread");
        odometryThread = new TerraThread("OdometryThread");
        backgroundThread = new TerraThread("BackgroundThread");
        independentRunner = new IndependentRunner();
        rfsHandler.init();
    }

    /**
     * This method is run from TerraBot and this initializes all of the robotparts, sets the user to main user
     * It then starts the robotfunctionsthread, odometry thread, and resets the game timer
     * NOTE: Threads are started in init to prevent lag in start
     */
    public void init(){
        setUser(mainUser);
        forAllParts(RobotPart::init);
        robotFunctionsThread.start();
        odometryThread.start();
        backgroundThread.start();
        gameTime.reset();
    }

    /**
     * Start, starts the robotfunctions by "resuming" (resume and start are the same thing)
     */
    public void start() {
        rfsHandler.resume();
    }

    public void update(){
        checkAccess(mainUser);
        TerraThread.checkAllThreadsForExceptions();
    }
    /**
     * the stop method stops updating threads, and halts the robot
     * @link halt
     */
    public void stop(){
        cancelAutoModules();
        RobotFramework.backgroundThread.stopUpdating();
        cancelIndependent();
        independentRunner.disableIndependent();
        TerraThread.stopUpdatingAllThreads();
        halt();
    }

    /**
     * Stops all of the robotparts by setting all of the cmotors and cservos to 0 power
     * NOTE: This will only work if the current user is main user, do NOT use this in a thread
     */
    public void halt(){ forAllParts(RobotPart::halt); }

    /**
     * Adds an automodule (or list of stages) to the robotfunctions to add it to the queue
     * @param autoModule
     */
    public void addAutoModule(StageList autoModule){
        rfsHandler.addAutoModule(autoModule);
    }

    /**
     * Sets the user to the new user specified
     * NOTE: This does not change the access of the user which must be updated explicity with checkAccess
     * @param newUser
     */
    public synchronized void setUser(User newUser){ forAllParts(part -> part.switchUser(newUser)); }

    /**
     * Checks the access of the potential user to all of the robot parts
     * This should be called every time a user wants to use the robot, to check if the current user privileges are updated
     * @param potentialUser
     */
    public synchronized void checkAccess(User potentialUser){ forAllParts(part -> part.checkAccess(potentialUser)); }

    /**
     * Cancel all of the automodules by emptying the robot functions queue
     */
    public void cancelAutoModules(){
        setUserMainAndHalt();
        rfsHandler.emptyQueue();
    }

    /**
     * Set the user to main and halt all of the robot parts that aren't the main user
     */
    public void setUserMainAndHalt() {
        forAllParts(part -> {
            if (!part.getUser().equals(mainUser)){
                part.switchUser(mainUser);
                part.checkAccess(mainUser);
                part.halt();
            }
        });
    }

    /**
     * Resume the automodules
     */
    public void resumeAutoModules(){ rfsHandler.resume(); }

    /**
     * Pause the automodules
     */
    public void pauseAutoModules() { rfsHandler.pauseNow(); }

    /**
     * This runs the specified code for all of the robot parts.
     * The type parameter code segment will accept the current robotpart
     * @param run
     */
    private void forAllParts(ParameterCodeSeg<RobotPart> run){
        for(RobotPart part: allRobotParts){
            run.run(part);
        }
    }


    public void addIndependent(Independent independent){
        independentRunner.addIndependent(independent);
    }

    public void cancelIndependent(){
        independentRunner.disableIndependent();
        independentRunner.cancelIndependent();
    }
}

package teleutil.independent;

import robot.RobotFramework;
import util.ExceptionCatcher;
import util.User;

import static global.General.bot;
import static global.General.mainUser;
import static robot.RobotUser.drive;

public class IndependentFunctions {
    /**
     * Class to run independents
     */


    /**
     * Is the independent running?
     */
    private volatile boolean isIndependentRunning = false;

    /**
     * Current independent object
     */
    private Independent currentIndependent;

    /**
     * Initialize the thread, run the auton and stop the auton, throttling to avoid burning excess CPU cycles
     */
    public void init(){
        currentIndependent = null;
        RobotFramework.independentThread.setExecutionCode(() -> {
           if(isIndependentRunning && currentIndependent != null){
               drive.switchUser(User.INDP);
               drive.checkAccess(User.INDP);
               currentIndependent.initAuto();
               currentIndependent.runAuto();
               currentIndependent.stopAuto();
               drive.switchUser(mainUser);
               isIndependentRunning = false;
           }else{
               ExceptionCatcher.catchInterrupted(() -> Thread.sleep(50));
           }
        });
    }

    /**
     * Run the independent by setting the current independent to this object
     * @param independent
     */
    public void runIndependent(Independent independent){ currentIndependent = independent; currentIndependent.reset(); isIndependentRunning = true; }

    /**
     * Stop the current independent
     */
    public void stopCurrentIndependent(){ isIndependentRunning = false; if(currentIndependent != null){ currentIndependent.exit();} }

    /**
     * Is an independent running?
     * @return isIndependentRunning
     */
    public boolean isIndependentRunning(){ return isIndependentRunning; }

    public Independent getCurrentIndependent(){ return currentIndependent; }

}

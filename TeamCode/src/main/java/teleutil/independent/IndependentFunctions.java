package teleutil.independent;

import robot.RobotFramework;
import util.User;

import static global.General.bot;
import static global.General.mainUser;
import static robot.RobotUser.drive;

public class IndependentFunctions {
    private volatile boolean isIndependentRunning = false;
    private Independent currentIndependent;

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
           }
        });
    }

    public void runIndependent(Independent independent){ currentIndependent = independent; currentIndependent.reset(); isIndependentRunning = true; }
    public void stopCurrentIndependent(){ isIndependentRunning = false; if(currentIndependent != null){ currentIndependent.exit();} }

}

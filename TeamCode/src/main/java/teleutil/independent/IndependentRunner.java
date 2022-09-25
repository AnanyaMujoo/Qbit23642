package teleutil.independent;

import robot.RobotFramework;
import global.Modes;
import util.User;

import static global.General.bot;
import static global.General.mainUser;

public class IndependentRunner {
    public volatile boolean isIndependentRunning = false;
    public volatile boolean disabled = false;

    public synchronized void addIndependent(Independent independent){
        isIndependentRunning = true;
        bot.drive.setDriveMode(Modes.DriveMode.FAST);
        bot.drive.switchUser(User.BACK);
        RobotFramework.backgroundThread.setExecutionCode(() -> {
            bot.drive.checkAccess(User.BACK);
            independent.initAuto();
            independent.runAuto();
            bot.drive.switchUser(mainUser);
            isIndependentRunning = false;
            cancelIndependent();
        });
    }

    public synchronized void cancelIndependent(){
        isIndependentRunning = false;
        RobotFramework.backgroundThread.setExecutionCode(() -> {});
    }

    public synchronized void disableIndependent(){
        disabled = true;
    }

    public synchronized void enableIndependent(){
        disabled = false;
    }
}

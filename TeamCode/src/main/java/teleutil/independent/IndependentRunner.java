package teleutil.independent;

import elements.Ball;
import robot.RobotFramework;
import teleutil.Modes;
import util.User;

import static global.General.bot;
import static global.General.log;
import static global.General.mainUser;

public class IndependentRunner {
    // TOD4 Why do we need this?
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

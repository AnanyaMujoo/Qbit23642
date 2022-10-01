package unused.mecanumold.oldauto;

import static java.lang.Math.PI;
import static global.General.*;

import java.util.LinkedList;
import java.util.TreeMap;

import automodules.AutoModule;
import geometry.circles.AngleType;
import robotparts.RobotPart;
import util.codeseg.CodeSeg;

/**
 * An executor for movement and RobotFunctions, combined
 */
@Deprecated
public abstract class Executor extends MovementExecutor {

    /**
     * Stores whether there are synced RFs to do during each movement
     */
    private final boolean[] syncedSegsExist = new boolean[1000];
    /**
     * Stores whether there are unsynced RFs to do during each movement
     */
    private final boolean[] unSyncedSegsExist = new boolean[1000];
    /**
     * Stores the synced RFs to do during each movement
     */
    private final TreeMap<Integer, LinkedList<CodeSeg>> syncedSegs = new TreeMap<>();
    /**
     * Stores the unsynced RFs to do during each movement
     */
    private final TreeMap<Integer, LinkedList<CodeSeg>> unSyncedSegs = new TreeMap<>();

    /**
     * Stores whether a codeSeg is currently running or not
     */
    private boolean runningCodeSeg = false;

    //region CONSTRUCTORS
    public Executor() {
        super(0, 0, PI/2, AngleType.RADIANS);
        fillBoolArrs();
    }

    /**
     * Initially populates the boolean arrays
     */
    private void fillBoolArrs() {
        for (int i = 0; i < 1000; i++) {
            syncedSegsExist[i] = false;
            unSyncedSegsExist[i] = false;
        }
    }
    //endregion

    /**
     * Adds a synchronized RF for the current movement
     * Put before the movement you want it synchronized with
     * @param rf SyncedRF to add
     */
    public void addSynchronizedRF(AutoModule rf) {
        if (!syncedSegsExist[curPath]) {
            syncedSegsExist[curPath] = true;
            syncedSegs.put(curPath, new LinkedList<>());
        }
        syncedSegs.get(curPath).add(() -> bot.addAutoModule(rf));
    }

    /**
     * Adds an unsynced RF for the break between these two movements
     * Put it between the two movements you want to put it between
     * @param rf RobotFunction to execute
     */
    public void addUnsynchronizedRF(AutoModule rf) {
        if (!unSyncedSegsExist[curPath]) {
            unSyncedSegsExist[curPath] = true;
            unSyncedSegs.put(curPath, new LinkedList<>());
        }
        unSyncedSegs.get(curPath).add(() -> bot.addAutoModule(rf));
    }

    /**
     * Adds a pause between two movements/RFs
     * @param time duration
     */
    public void addPause(double time) {
        addUnsynchronizedRF(new AutoModule(RobotPart.pause(time)));
    }

    /**
     * Is the executor completely finished (movement + RFs)
     * @return
     */
    public boolean finished() {
        return finishedMove() && !unSyncedSegsExist[curPath] && !syncedSegsExist[curPath]
                && bot.rfsHandler.rfsQueue.isEmpty();
    }

    /**
     * Updates RFs and Movement
     */
    public void update() {
        if (!runningCodeSeg) {
            // Not doing an unsynced RF
            if (!moveRunning) {
                // Updating unsynced RFs
                if (unSyncedSegsExist[curPath]) {
                    runningCodeSeg = true;
                    unSyncedSegs.get(curPath).poll().run();
                    unSyncedSegsExist[curPath] = !unSyncedSegs.get(curPath).isEmpty();
                } else if (!finishedMove()) {
                    resumeMove();
                }
            } else {
                // Updating synced RFs and movement
                if (syncedSegsExist[curPath]) {
                    syncedSegs.get(curPath).poll().run();
                    syncedSegsExist[curPath] = !syncedSegs.get(curPath).isEmpty();
                }
                updateMovement();
            }
        } else {
            // Waiting for unsynced RF to finish
            runningCodeSeg = !bot.rfsHandler.rfsQueue.isEmpty();
        }
    }

}

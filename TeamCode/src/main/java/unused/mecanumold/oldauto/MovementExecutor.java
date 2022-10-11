package unused.mecanumold.oldauto;

import java.util.*;

import geometry.position.Pose;
import unused.tankold.TankReactor;

import static global.General.*;
import static java.lang.Math.*;

/**
 * Handles all arc-movement
 */
@Deprecated
public abstract class MovementExecutor {
//
//    // Uses the desired motion predictor and reactor to move the robot along the desired path
//    // There should be one static executor
//    // The start heading is counterclockwise (PI/2 is upward/forward)
//    // every other heading is clockwise (relative to start angle)
//
//    /**
//     * Stores the paths for the robot
//     */
//    public ArrayList<ArrayList<Pose>> paths = new ArrayList<>();
//
//    /**
//     * Stores the arc generators to be used (one per setpoint)
//     */
//    protected ArrayList<ArcGenerator> arcGenerators = new ArrayList<>();
//    /**
//     * Stores the reactor to be used – tank because arcs are tank (forward & turn)
//     */
//    private final TankReactor reactor = new TankReactor();
////
////    /**
////     * Start heading to be used
////     */
//    private double startH;
//
//    /**
//     * Returns whether the movement is complete or not
//     */
//    protected boolean moveRunning = false;
//
//    /**
//     * Indexes for paths arraylist
//     */
//    public int curPath = 0;
//    public int curPose = 0;
//
//    //region PUBLIC FUNCTIONS
//
//    /**
//     * Constructor with starting position of robot
//     * @param startX starting x position
//     * @param startY starting y position
//     * @param startH starting heading
//     */
//    public MovementExecutor(double startX, double startY, double startH) {
//        arcGenerators.add(new ArcGenerator());
////        this.startH = startH * (angleType == AngleType.DEGREES ? PI/180 : 1);
////        addWaypoint(startX, startY, 0, AngleType.RADIANS);
//    }
//
//    /**
//     * Adds a waypoint to the path
//     * @param x absolute x position
//     * @param y absolute y position
//     * @param h absolute heading
//     */
//    public void addWaypoint(double x, double y, double h) {
////        h *= -1;
//////        h *= angleType == AngleType.DEGREES ? (PI/180) : 1;
////        h += startH;
////        while (h > 2 * PI) h -= 2 * PI;
////        while (h < 0) h += 2 * PI;
////        if ((h > PI || h < 0) && h != 3 * PI/2) { h += PI; }
////        arcGenerators.get(arcGenerators.size() - 1).moveTo(x, y, h);
//    }
//
//    /**
//     * Adds a setpoint
//     * @param x absolute x position
//     * @param y absolute y position
//     * @param h absolute heading
//     */
//    public void addSetpoint(double x, double y, double h) {
////        addWaypoint(x, y, h, angleType);
//        arcGenerators.add(new ArcGenerator());
//        curPath++;
////        addWaypoint(x, y, h, angleType);
//    }
//
//    /**
//     * Mark the path-adding complete
//     * Generates all of the arcs and marks the executor ready
//     */
//    public void complete() {
//        for (ArcGenerator g : arcGenerators) {
//            ArrayList<Pose> poses = new ArrayList<>();
//            ArrayList<PathSegment> pss = g.done().segments;
//            for (PathSegment ps : pss) {
//                poses.addAll(ps.points);
//            }
//            paths.add(poses);
//        }
//        if (arcGenerators.get(arcGenerators.size() - 1).empty()) {
//            paths.remove(paths.size() - 1);
//        }
//        curPath = 0;
//    }
//
//    /**
//     * Marks that the executor can start now
//     */
//    public void resumeMove() { moveRunning = true; }
//
//    /**
//     * Marks that the executor should stop now
//     */
//    public void pauseMove() { moveRunning = false; }
//
//    /**
//     * Has the executor finished its movement?
//     * @return finished movement?
//     */
//    public boolean finishedMove() { return curPath >= paths.size(); }
//
//    //endregion
//
//    //region BACKGROUND FUNCTIONS
//
//    /**
//     * Updates the executor's movement
//     * Runs movement appropriately, by whether it finished and if it should be running
//     */
//    public void updateMovement() {
//        if (moveRunning && !finishedMove()) {
//            updateCurPoint();
//            // Moves to next setpoint if the current is done
//            if (curPose > paths.get(curPath).size()) {
//                curPath++;
//                curPose = 0;
//                pauseMove();
//                move(0, 0);
//                return;
//            }
//            if (paths.get(curPath).size() == curPose) {
//                // Running for a setpoint
////                Pose nextPose = paths.get(curPath).get(curPose - 1);
////                double forwardPow = reactor.forwardPowSetpoint(nextPose.p.x, nextPose.p.y);
////                double dis = sqrt(pow(bot.odometry.getPose()[0] - nextPose.p.x, 2)
////                        + pow(bot.odometry.getPose()[1] - nextPose.p.y, 2));
////                if (dis > 10) {
////                    // if too far away, move with waypoint algorithm
////                    move(
////                        forwardPow,
////                        reactor.turnPowWay(nextPose.p.x, nextPose.p.y, startH)
////                    );
////                } else {
////                    // run the custom setpoint algorithm
////                    moveSetpoint(nextPose);
////                }
//            } else {
//                // Move for a waypoint
//                Pose nextPose = paths.get(curPath).get(curPose);
////                move(
//////                    reactor.forwardPowWaypoint(nextPose.p.x, nextPose.p.y),
//////                    reactor.turnPowWay(nextPose.p.x, nextPose.p.y, startH)
////                );
//            }
//        } else {
//            // Stop movement
//            move(0, 0);
//        }
//    }
//
//    /**
//     * Moves the robot for a setpoint
//     * Can be overwritten for different behavior (currently tank)
//     * @param nextPose point to move to
//     */
//    public void moveSetpoint(Pose nextPose) {
//        double forwardPow = reactor.forwardPowSetpoint(nextPose.p.x, nextPose.p.y);
//        move(
//                forwardPow,
//                reactor.turnPow(nextPose.getAngle(), startH, true)
//        );
//    }
//
//    /**
//     * Updates the current point
//     * If completed a point, continues
//     * If skipped points, doesn't care
//     */
//    private void updateCurPoint() {
//        for (int i = curPose; i < paths.get(curPath).size(); i++) {
//            if (doneWithPoint(i)) curPose = i + 1;
//            else break;
//        }
//        if (curPose == paths.get(curPath).size() && doneWithSetpoint()) {
//            curPose++;
//        }
//    }
//
//    /**
//     * Is the executor finished with its current setpoint?
//     * @return Finished
//     */
//    private boolean doneWithSetpoint() {
//        Pose nextPose = paths.get(curPath).get(paths.get(curPath).size() - 1);
//        return abs(reactor.turnPow(nextPose.getAngle(), startH, true)) < 0.4
//                && sqrt(pow(bot.odometry.getPose()[0] - nextPose.getX(), 2)
//                + pow(bot.odometry.getPose()[1] - nextPose.getY(), 2)) < 4;
//    }
//
//    /**
//     * Returns if the executor is done with the specified point
//     * @param i current index
//     * @return Done?
//     */
//    private boolean doneWithPoint(int i) {
//        Pose nextPose = paths.get(curPath).get(i);
//        double dis = sqrt(pow(bot.odometry.getPose()[0] - nextPose.getX(), 2)
//                + pow(bot.odometry.getPose()[1] - nextPose.getY(), 2));
//        return dis < 10;
//    }
//
//    //endregion
//
//    //region METHODS TO OVERRIDE
//
//    /**
//     * Moves the robot based off forward and turn powers
//     * @param f forward power
//     * @param t turn power
//     */
//    public abstract void move(double f, double t);
//
//    //endregion

}

package auton.old;

import static global.General.fault;

import androidx.annotation.NonNull;

import automodules.AutoModule;
import auton.Auto;
import autoutil.executors.Executor;
import elements.FieldSide;
import geometry.circles.AngleType;
import util.codeseg.CodeSeg;

/**
 * Runs Autonomous with an Executor
 */
public abstract class CompleteAuto extends Auto {
    /**
     * Executor to be used
     */
    protected Executor executor;

    /**
     * Should the autonomous update with telemetry?
     */
    boolean haveTelemetry = true;

    /**
     * Can be overwritten
     * If not overwritten, tells not to update with telemetry
     */
    public void addTelemetry() { haveTelemetry = false; }

    /**
     * To be overwritten
     * Runs with the loop where executor is running and updated
     */
    public void duringLoop() {}

    /**
     * To be overwritten
     * Runs when the start button is clicked (once)
     */
    public void onStart() {}

    /**
     * To be overwritten
     * Defines the executor and adds the functions to the executor
     */
    public abstract void defineExecutorAndAddPoints();

    /**
     * To be overwritten
     * Runs when the program ends (executor finishes or stop pressed)
     */
    public abstract void onEnd();

    /**
     * Checks and throws error if the heading the user used is incorrect
     * @param h Heading
     */
    private void checkHeadingWrong(double h) {
        fault.check("Using degrees or wrong range of heading ( correct is (-PI, PI] ) in default point?",
                Math.abs(h) > Math.PI || h == -Math.PI, false);
    }

    /**
     * Adds a waypoint
     * @param x Absolute x position
     * @param y Absolute y position
     * @param h Absolute heading
     * @return CodeSeg to be inputted into addExecutorFuncs
     */
    public CodeSeg wayPoint(double x, double y, double h) {
        checkHeadingWrong(h);
        return () -> executor.addWaypoint(x, y, h, AngleType.RADIANS);
    }

    /**
     * Adds a setpoint
     * @param x Absolute x position
     * @param y Absolute y position
     * @param h Absolute heading
     * @return CodeSeg to be inputted into addExecutorFuncs
     */
    public CodeSeg setPoint(double x, double y, double h) {
        checkHeadingWrong(h);
        return () -> executor.addSetpoint(x, y, h, AngleType.RADIANS);
    }

    /**
     * Adds an unsynchronized robot function
     * @param rf RobotFunction to be added
     * @return CodeSeg to be inputted into addExecutorFuncs
     */
    public CodeSeg unsyncedRF(AutoModule rf) {
        return () -> executor.addUnsynchronizedRF(rf);
    }

    /**
     * Adds a synchronized robot function
     * @param rf RobotFunction to be added
     * @return CodeSeg to be inputted into addExecutorFuncs
     */
    public CodeSeg syncedRF(AutoModule rf) {
        return () -> executor.addSynchronizedRF(rf);
    }

    /**
     * Does a custom task
     * @param in CodeSeg that executes custom task
     * @return CodeSeg to be inputted into addExecutorFuncs
     */
    public CodeSeg custom(CodeSeg in) {
        return in;
    }

    /**
     * Handles the executor functions (setpoint, waypoint, robotFunctions, custom, etc.)
     * @param funcs CodeSegs to be executed that will add executor functions
     */
    public void addExecutorFuncs(@NonNull CodeSeg... funcs) {
        for (CodeSeg func : funcs) {
            func.run();
        }
    }

    /**
     * To be overwritten
     * Provides the current field side
     * @return current field side
     */
    public abstract FieldSide getSide();

    /**
     * To be overwritten
     * Runs when the init button is pressed (once)
     */
    public void onInit() {}

    /**
     * Runs the initialization
     * Should not be overwritten
     */
    @Override
    public void initAuto() {
        defineExecutorAndAddPoints();
        executor.complete();
        onInit();
        activate(getSide());
    }

    /**
     * Runs the autonomous
     * Should not be overwritten
     */
    @Override
    public void runAuto() {
        onStart();

        executor.resumeMove();

        while (opModeIsActive() && !executor.finished()) {
            executor.update();
            addTelemetry();
            duringLoop();
            update(haveTelemetry);
        }

        onEnd();
    }
}

package robotparts.unused.mecanumold;

import automodules.AutoModule;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import autoutil.executors.Executor;
import autoutil.executors.MecanumExecutorArcsPID;
import geometry.circles.AngleType;
import geometry.position.Pose;
import global.Modes.DriveMode;
import global.Modes.IndependentMode;
import math.misc.Logistic;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;

import static global.General.bot;

/**
 * NOTE: Uncommented
 */

/**
 * Create Motors
 */
public class MecanumDrive extends RobotPart {
    private Executor executor;
    private CMotor fr, br, fl, bl;
    private DriveMode driveMode = DriveMode.FAST;
    private IndependentMode independentMode = IndependentMode.MANUAL;

    @Override
    public void init() {
        fr = create("fr", ElectronicType.CMOTOR_REVERSE);
        br = create("br", ElectronicType.CMOTOR_REVERSE);
        fl = create("fl", ElectronicType.CMOTOR_FORWARD);
        bl = create("bl", ElectronicType.CMOTOR_FORWARD);
    }

    /**
     * Raw movement
     *
     * @param f Forward Power
     * @param s Strafe Power
     * @param t Turn Power
     */
    public void move(double f, double s, double t) {
        fr.setPower(f - s - t);
        br.setPower(f + s - t);
        fl.setPower(f + s + t);
        bl.setPower(f - s + t);
    }

    /**
     * Move the robot smoothly
     *
     * @param f Forward Power
     * @param s Strafe Power
     * @param t Turn Power
     */
    public void moveSmooth(double f, double s, double t) {
        Logistic movementCurveForward = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 10.0, 5.0);
        Logistic movementCurveStrafe = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0, 6.0);
        Logistic movementCurveTurn = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0, 6.0);
        move(movementCurveForward.fodd(f), movementCurveStrafe.fodd(s), movementCurveTurn.fodd(t));
    }

    public void moveSmoothTele(double f, double s, double t) {
        if(!bot.independentRunner.isIndependentRunning){
            Logistic movementCurveForward = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 10.0, 5.0);
            Logistic movementCurveStrafe = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0, 6.0);
            Logistic movementCurveTurn = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0, 6.0);
            double scale = 1;
            if (driveMode.equals(DriveMode.MEDIUM)) {
                scale = 0.75;
            } else if (driveMode.equals(DriveMode.SLOW)) {
                scale = 0.5;
            }
            move(
                    movementCurveForward.fodd(f * scale),
                    movementCurveStrafe.fodd(s * scale),
                    movementCurveTurn.fodd(t * scale));
        }
    }

    public Main mainMoveForward(double pow) {
        return new Main(() -> move(pow, 0, 0));
    }

    public Main mainMove(double f, double s, double t) {
        return new Main(() -> move(f, s, t));
    }

    /**
     * Stop the robot
     *
     * @return
     */

    public Stop stopMove() {
        return new Stop(() -> move(0, 0, 0));
    }

    /**
     * Initial to set path for executor
     */
    public Initial setPath(Pose[][] poses) {
        return new Initial(() -> {
            executor = new MecanumExecutorArcsPID(() -> true);
            executor.addSetpoint(bot.odometry.getCurX(), bot.odometry.getCurY(),
                    bot.odometry.getCurThetaRad(), AngleType.RADIANS);
            for (Pose[] p : poses) {
                for (int i = 0; i < p.length - 1; i++) {
                    executor.addWaypoint(p[i].p.x, p[i].p.y, p[i].ang, AngleType.RADIANS);
                }
                executor.addSetpoint(p[p.length - 1].p.x, p[p.length - 1].p.y, p[p.length - 1].ang, AngleType.RADIANS);
            }
        });
    }

    /**
     * Main to execute a path
     *
     * @return Main path executor
     */
    private Main executePath() {
        return new Main(executor::update);
    }

    /**
     * Exit to execute a path
     *
     * @return Path finished?
     */
    private Exit exitFinishedPath() {
        return new Exit(executor::finished);
    }

    /**
     * Stage to move to a set of points
     * Autonomous movement in TeleOp
     *
     * @param points List of arrays of points – each array of point is waypoints & 1 setpoint at the end
     * @return Stage to move
     */
    public Stage moveToPoint(Pose[]... points) {
        return new Stage(
                usePart(),
                setPath(points),
                executePath(),
                exitFinishedPath(),
                returnPart()
        );
    }

    public AutoModule mainChangeDrive(DriveMode driveMode) {
        return new AutoModule(new Stage(
                usePart(),
                mainCycleDrive(driveMode),
                exitAlways(),
                returnPart()
        ));
    }


    public Main mainCycleDrive(DriveMode drive) {
        return new Main(() -> driveMode = drive);
    }

    private DriveMode nextDrive() {
        switch (driveMode) {
            case FAST:
                return DriveMode.SLOW;
            case MEDIUM:
                return DriveMode.FAST;
            case SLOW:
                return DriveMode.MEDIUM;
        }
        return null;
    }

    // TOD4
    // Make cycle class or smt for enums

    public void cycleDriveUp() {
        driveMode = nextDrive();
    }

    public void cycleDriveDown() {
        cycleDriveUp();
        cycleDriveUp();
    }

    public DriveMode getDriveMode() {
        return driveMode;
    }

    public void setDriveMode(DriveMode driveMode) {
        this.driveMode = driveMode;
    }

    public IndependentMode getIndependentMode() {
        return independentMode;
    }

    public void setIndependentMode(IndependentMode independentMode) {
        this.independentMode = independentMode;
    }

    public void cycleIndependentMode(){
        if(independentMode.equals(IndependentMode.USING)){
            setIndependentMode(IndependentMode.MANUAL);
        }else{
            setIndependentMode(IndependentMode.USING);
        }
    }
}

package robotparts.hardware;

import com.qualcomm.robotcore.hardware.Servo;

import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PServo;
import global.Modes;

public class Outtake extends RobotPart {
    /**
     * orel is Outtake Release Controller
     * over is Outtake Vertical Controller
     * ohor is Outtake Horizontal Controller
     */
    private PServo od, or, ol, ot, cap;

    private Modes.OuttakeMode outtakeMode = Modes.OuttakeMode.ALLIANCE;
    private Modes.SharedMode sharedMode = Modes.SharedMode.NORMAL;


    @Override
    public void init() {
        od = create("od", ElectronicType.PSERVO_FORWARD);
        or = create("or", ElectronicType.PSERVO_FORWARD);
        ol = create("ol", ElectronicType.PSERVO_REVERSE);
        ot = create("ot", ElectronicType.PSERVO_FORWARD);
        cap = create("cap", ElectronicType.PSERVO_REVERSE);

        cap.addPosition("start", 0.0);
        cap.addPosition("hold", 0.5);
        cap.addPosition("down", 0.7);
        cap.addPosition("end", 1.0);

        cap.addPosition("mid", 0.2);

        od.addPosition("releaseLeft", 0.0);
        od.addPosition("lock", 0.53);
        od.addPosition("releaseRight", 0.9);

        or.addPosition("start", 0.1);
        or.addPosition("horizontal", 1);
        ol.addPosition("start", 0.05);
        ol.addPosition("horizontal", 0.92);

        ot.addPosition("sharedLeft", 0.3); //0.18
        ot.addPosition("center", 0.525);
        ot.addPosition("sharedRight", 0.78); //0.9

        lock();
        turretCenter();
        startCap();

    }


    public void startCap(){cap.setPosition("start");}

    public void collectCap(){
        cap.setPosition("end");
    }

    public void readyCap(){
        cap.setPosition("hold");
    }

    public void dropCap(){
        cap.setPosition("down");
    }

    public void midCap(){cap.setPosition("mid");}

    /**
     * Releases the release servo
     */
    public void drop() { od.setPosition("releaseRight"); }

    /**
     * Locks the release servo
     */
    public void lock() { od.setPosition("lock"); }

    /**
     * Moves the horizontal servo for shared shipping hub
     */
    public void sharedTurretRight() { ot.setPosition("sharedRight"); }

    /**
     * Moves the horizontal servo for shared shipping hub
     */
    public void sharedTurretLeft() { ot.setPosition("sharedLeft"); }

    /**
     * Moves the horizontal servo for everything except shared shipping hub
     */
    public void turretCenter() { ot.setPosition("center"); }

    /**
     * Moves the vertical servo for locking
     */
    public void turnToStart() {
        or.setPosition("start");
        ol.setPosition("start");
    }

    /**
     * Moves the vertical servo for releasing
     */
    public void turnToHorizontal() {
        or.setPosition("horizontal");
        ol.setPosition("horizontal");
    }

    /**
     * Mains for all of the above functions
     * @return Main for related function
     */
    private Main mainDrop() { return new Main(this::drop); }
    private Main mainLock() { return new Main(this::lock); }
    private Main mainCenterTurret() { return new Main(this::turretCenter); }
    private Main mainSharedTurretRight() { return new Main(this::sharedTurretRight); }
    private Main mainSharedTurretLeft() { return new Main(this::sharedTurretLeft); }
    private Main mainTurnToStart() { return new Main(this::turnToStart); }
    private Main mainTurnToHorizontal() { return new Main(this::turnToHorizontal); }

    /**
     * Stages for all the above functions
     * @param main
     * @param t
     * @return
     */
    public Stage buildStageTime(Main main, double t){ return new Stage(usePart(), main, exitTime(t), returnPart()); }
    public Stage stageDrop(double t) { return buildStageTime(mainDrop(), t); } //0.25
    public Stage stageLock(double t) { return buildStageTime(mainLock(), t); } //0.25
    public Stage stageCenterTurret(double t) { return buildStageTime(mainCenterTurret(), t); }
    public Stage stageSharedTurretRight(double t) { return buildStageTime(mainSharedTurretRight(), t); } // 0.5
    public Stage stageSharedTurretLeft(double t) { return buildStageTime(mainSharedTurretLeft(), t); } //0.5
    public Stage stageTurnToStart(double t) { return buildStageTime(mainTurnToStart(), t); } //0.05
    public Stage stageTurnToHorizontal(double t) { return buildStageTime(mainTurnToHorizontal(), t); } //0.05

    public void setOuttakeMode(Modes.OuttakeMode outtakeMode){ this.outtakeMode = outtakeMode; }
    public Modes.OuttakeMode getOuttakeMode(){ return outtakeMode; }


    public void cycleOuttakeMode(){
        if(outtakeMode.equals(Modes.OuttakeMode.SHARED)){
            setOuttakeMode(Modes.OuttakeMode.ALLIANCE);
        }else{
            setOuttakeMode(Modes.OuttakeMode.SHARED);
        }
    }

    public void cycleSharedMode(){
        if(sharedMode.equals(Modes.SharedMode.CENTER)){
            setSharedMode(Modes.SharedMode.NORMAL);
        }else{
            setSharedMode(Modes.SharedMode.CENTER);
        }
    }

    public Modes.SharedMode getSharedMode() {
        return sharedMode;
    }

    public void setSharedMode(Modes.SharedMode sharedMode) {
        this.sharedMode = sharedMode;
    }
}

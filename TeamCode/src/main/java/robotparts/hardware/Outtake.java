package robotparts.hardware;

import com.qualcomm.robotcore.hardware.Servo;

import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PServo;
import global.Modes;

public class Outtake extends RobotPart {

    private PServo od, or, ol, ot;


    @Override
    public void init() {
        od = create("od", ElectronicType.PSERVO_FORWARD);
        or = create("or", ElectronicType.PSERVO_FORWARD);
        ol = create("ol", ElectronicType.PSERVO_REVERSE);
        ot = create("ot", ElectronicType.PSERVO_FORWARD);


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

    }

    public void drop() { od.setPosition("releaseRight"); }
    public void lock() { od.setPosition("lock"); }
    public void sharedTurretRight() { ot.setPosition("sharedRight"); }
    public void sharedTurretLeft() { ot.setPosition("sharedLeft"); }
    public void turretCenter() { ot.setPosition("center"); }
    public void turnToStart() { or.setPosition("start"); ol.setPosition("start"); }
    public void turnToHorizontal() { or.setPosition("horizontal"); ol.setPosition("horizontal"); }

    public Stage stageDrop(double t) { return super.customTime(this::drop, t); } //0.25
    public Stage stageLock(double t) { return super.customTime(this::lock, t); } //0.25
    public Stage stageCenterTurret(double t) { return super.customTime(this::turretCenter, t); }
    public Stage stageSharedTurretRight(double t) { return super.customTime(this::sharedTurretRight, t); } // 0.5
    public Stage stageSharedTurretLeft(double t) { return super.customTime(this::sharedTurretLeft, t); } //0.5
    public Stage stageTurnToStart(double t) {return super.customTime(this::turnToStart, t); } //0.05
    public Stage stageTurnToHorizontal(double t) { return super.customTime(this::turnToHorizontal, t); } //0.05


}

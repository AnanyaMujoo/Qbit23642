package robotparts.hardware;

import com.qualcomm.robotcore.hardware.Servo;

import automodules.AutoModule;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PServo;
import global.Modes;
import util.Timer;

public class Outtake extends RobotPart {

    private PServo armr, arml, turn, claw;

    @Override
    public void init() {
        armr = create("armr", ElectronicType.PSERVO_REVERSE);
        arml = create("arml", ElectronicType.PSERVO_FORWARD);

        arml.changePosition("start", 0.15);
        armr.changePosition("start", 0.15);

        arml.addPosition("startHalf", 0.45);
        armr.addPosition("startHalf", 0.45);

        arml.addPosition("endHalf", 0.75);
        armr.addPosition("endHalf", 0.75);

        arml.changePosition("end", 0.94);
        armr.changePosition("end", 0.94);

        turn = create("turn", ElectronicType.PSERVO_FORWARD);
        claw = create("claw", ElectronicType.PSERVO_FORWARD);

        turn.changePosition("start", 0.2);
        turn.addPosition("flipped", 1.0);

        claw.addPosition("open", 0.75);
        claw.addPosition("close", 1.0);

        readyEnd();
        openClaw();
        unFlip();
    }

    public void moveStart(){ armr.setPosition("start"); arml.setPosition("start"); turn.setPosition("start"); }
    public void moveEnd(){ armr.setPosition("end"); arml.setPosition("end"); turn.setPosition("flipped"); }
    public void openClaw(){ claw.setPosition("open"); }
    public void closeClaw(){ claw.setPosition("close"); }

    public void flip(){ turn.setPosition("flipped"); }
    public void unFlip(){ turn.setPosition("start"); }

    public void readyEnd(){ armr.setPosition("startHalf"); arml.setPosition("startHalf"); }
    public void readyStart(){ armr.setPosition("endHalf"); arml.setPosition("endHalf"); }

    public Stage stageStart(double t){ return super.customTime(this::moveStart, t); }
    public Stage stageEnd(double t){ return super.customTime(this::moveEnd, t); }
    public Stage stageOpen(double t){ return super.customTime(this::openClaw, t); }
    public Stage stageClose(double t){ return super.customTime(this::closeClaw, t); }
    public Stage stageFlip(double t){return super.customTime(this::flip, t); }
    public Stage stageUnFlip(double t){return super.customTime(this::unFlip, t); }


    public Stage stageEnd(){
        return super.customTime(time -> {
            if(time < 1.0){ closeClaw(); }else if(time < 2.0){ readyEnd(); flip(); }else if(time < 3.0){ moveEnd(); }
        }, 4.0);
    }

    public Stage stageStart(){
        return super.customTime(time -> {
            if(time < 0.1){ openClaw(); }else if(time < 0.2){ readyStart();}else if(time < 0.6){ closeClaw(); unFlip(); }else if(time < 2.1){ moveStart(); }
        }, 2.2);
    }

}

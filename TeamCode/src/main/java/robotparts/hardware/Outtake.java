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

        arml.changePosition("start", 0.18);
        armr.changePosition("start", 0.18);

        arml.addPosition("startHalf", 0.3);
        armr.addPosition("startHalf", 0.3);

        arml.addPosition("endHalf", 0.75);
        armr.addPosition("endHalf", 0.75);

        turn = create("turn", ElectronicType.PSERVO_FORWARD);
        claw = create("claw", ElectronicType.PSERVO_FORWARD);

        turn.changePosition("start", 0.2);
        turn.addPosition("flipped", 1.0);

        claw.addPosition("open", 0.7);
        claw.addPosition("close", 1.0);

        moveStart();
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
        final Timer timer = new Timer();
        return new Stage(
                new Initial(timer::reset), new Main(() -> {
                    double time = timer.seconds();
                    if(time < 0.1){ closeClaw(); }else if(time < 0.2){ readyEnd(); flip(); }else if(time < 0.5){ moveEnd(); }
                }
        ));
    }

    public Stage stageStart(){
        final Timer timer = new Timer();
        return new Stage(
                new Initial(timer::reset), new Main(() -> {
                    double time = timer.seconds();
                    if(time < 0.1){ openClaw(); }else if(time < 0.2){ readyStart();}else if(time < 0.3){ closeClaw(); unFlip(); }else if(time < 0.6){ moveStart(); }
                }
        ));
    }

}

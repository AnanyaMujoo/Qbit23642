package robotparts.hardware;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PServo;

public class Outtake extends RobotPart {

    private PServo armr, arml, turn, claw;

    private final double startPos = 0.14;
    private final double endPos = 0.79;

    @Override
    public void init() {
        armr = create("armr", ElectronicType.PSERVO_REVERSE);
        arml = create("arml", ElectronicType.PSERVO_FORWARD);

        arml.changePosition("start", startPos);
        armr.changePosition("start", startPos);

        arml.addPosition("startHalf", 0.45);
        armr.addPosition("startHalf", 0.45);

        arml.addPosition("endHalf", 0.75);
        armr.addPosition("endHalf", 0.75);

        arml.changePosition("end", endPos);
        armr.changePosition("end", endPos);

        turn = create("turn", ElectronicType.PSERVO_REVERSE);
        claw = create("claw", ElectronicType.PSERVO_REVERSE);

        turn.changePosition("start", 0.0);
        turn.addPosition("flipped", 0.7);

        claw.addPosition("open", 0.0); // 0.0
        claw.addPosition("close", 0.32);

        readyStart();
        openClaw();
        unFlip();
    }

    public void moveStart(){ armr.setPosition("start"); arml.setPosition("start"); }
    public void moveEnd(){ armr.setPosition("end"); arml.setPosition("end");  }
    public void openClaw(){ claw.setPosition("open"); }
    public void closeClaw(){ claw.setPosition("close"); }

    public void flip(){ turn.setPosition("flipped"); }
    public void unFlip(){ turn.setPosition("start"); }

    public void readyStart(){ armr.setPosition("startHalf"); arml.setPosition("startHalf"); }
    public void readyEnd(){ armr.setPosition("endHalf"); arml.setPosition("endHalf"); }


    public Stage stageReadyStart(double t){return super.customTime(this::readyStart, t);}

    public Stage stageStart(double t){ return super.customTime(this::moveStart, t); }
    public Stage stageEnd(double t){ return super.customTime(this::moveEnd, t); }
    public Stage stageOpen(double t){ return super.customTime(this::openClaw, t); }
    public Stage stageClose(double t){ return super.customTime(this::closeClaw, t); }
    public Stage stageFlip(double t){return super.customTime(this::flip, t); }
    public Stage stageUnFlip(double t){return super.customTime(this::unFlip, t); }


    public Stage stageEnd(){
        return super.customTime(time -> {
            if(time < 0.1){ closeClaw(); readyStart(); }else if(time < 0.4){
                flip();
//                flipContinuous(0.2, 1.0, time, 0.25);
            } else if(time < 0.9){
                moveContinuous(0.45, endPos, time - 0.4, 0.5);
            }
        }, 1.0);
    }

    public Stage stageStart(){
        return super.customTime(time -> {
            if(time < 0.1){ openClaw();  readyEnd(); }else if(time < 0.4){ closeClaw();
//                flipContinuous(1.0, 0.0, time, 0.25);
                unFlip();
            }else if(time < 0.9){
                moveContinuous(0.75, startPos, time - 0.4, 0.5);
            }
        }, 1.0);
    }


    public void moveContinuous(double start, double end, double time, double totalTime){
        double pos = start + ((end-start) * time/totalTime);
        armr.setPositionRaw(pos); arml.setPositionRaw(pos);
    }

    public void flipContinuous(double start, double end, double time, double totalTime){
        double pos = start + ((end-start) * time/totalTime);
        turn.setPositionRaw(pos);
    }

}

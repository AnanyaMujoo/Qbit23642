package robotparts.hardware;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PServo;

public class Claw extends RobotPart {
    public PServo outtaker;
    public PServo outtakel;

    @Override
    public void init() {
        outtaker = create("rc", ElectronicType.PSERVO_FORWARD);
        outtakel = create("lc", ElectronicType.PSERVO_REVERSE);
        //outtake 2 not configured yet
        outtaker.setPosition("hold", 0.10);
        outtaker.setPosition("hold2", 0.23);
        outtaker.setPosition("release", 0.92);
        outtaker.setPosition("ready",0.47);
        outtaker.setPosition("squeeze",0.24);

        outtakel.setPosition("hold", 0.05);
        outtakel.setPosition("hold2", 0.16);
        outtakel.setPosition("release", 0.87);
        outtakel.setPosition("ready",0.42);
        outtakel.setPosition("squeeze",0.19);

    }
    private void move(String positionName){ outtakel.moveToPosition(positionName); outtaker.moveToPosition(positionName);  }//outtake2.moveToPosition(positionName); }
    public void disable(){
        outtaker.disable();
        outtakel.disable();
    }
    public void hold(){ move("hold"); }
    public void release(){ move("release"); }
    public void ready(){ move("ready");}
    public void squeeze(){move("squeeze");}
    public void hold2(){ move("hold2");}

//    public void ram(){
//        ready();
//        disable();
//    }

    public Stage stageHold(double t){ return super.customTime(this::hold, t); }
    public Stage stageHold2(double t){ return super.customTime(this::hold2, t);}
    public Stage stageRelease(double t){ return super.customTime(this::release, t); }
    public Stage stageReady(double t){ return super.customTime(this::ready, t); }
    public Stage stageSqueeze(double t){ return super.customTime(this::squeeze, t); }
    public Stage stageDisable(double t){ return super.customTime(this::disable, t); }

}

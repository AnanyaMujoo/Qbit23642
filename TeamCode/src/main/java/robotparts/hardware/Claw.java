package robotparts.hardware;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PServo;

public class Claw extends RobotPart {
    public PServo outtake1;
    public PServo outtake2;

    @Override
    public void init() {
        outtake1 = create("rc", ElectronicType.PSERVO_REVERSE);
        outtake2 = create("lc", ElectronicType.PSERVO_FORWARD);
        //outtake 2 not configured yet
        outtake1.setPosition("hold", 0.2);
        outtake2.setPosition("hold", 0.2);
        outtake1.setPosition("release", 0.2);
        outtake2.setPosition("release", 0.2);

    }
    private void move(String positionName){ outtake1.moveToPosition(positionName); outtake2.moveToPosition(positionName); }

    public void hold(){ move("start"); }
    public void release(){ move("middle"); }

    public Stage stageHold(double t){ return super.customTime(this::hold, t); }
    public Stage stageRelease(double t){ return super.customTime(this::release, t); }

}

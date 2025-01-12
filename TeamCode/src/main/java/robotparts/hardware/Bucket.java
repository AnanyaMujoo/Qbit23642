package robotparts.hardware;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PServo;

public class Bucket extends RobotPart {
    public PServo rbucket;
    public PServo lbucket;
    @Override
    public void init() {
        rbucket = create("rs", ElectronicType.PSERVO_REVERSE);
        lbucket = create("ls", ElectronicType.PSERVO_FORWARD);
        rbucket.setPosition("bottom", 0);
        lbucket.setPosition("bottom", 0);
        rbucket.setPosition("top", 0.66);
        lbucket.setPosition("top", 0.66);
        lbucket.setPosition("hold", 0.33);
        rbucket.setPosition("hold", 0.33);
        lbucket.setPosition("specimen", 0.5);
        rbucket.setPosition("specimen", 0.5);


    }

    private void move(String positionName){ rbucket.moveToPosition(positionName); lbucket.moveToPosition(positionName);}

    public void bottom(){ move("bottom"); }
    public void top(){ move("top"); }
    public void hold(){ move("hold"); }

    public void specimen(){ move("specimen"); }

    public Stage stageBottom(double t){ return super.customTime(this::bottom, t); }
    public Stage stageTop(double t){ return super.customTime(this::top, t); }

    public Stage stageHold(double t){ return super.customTime(this::hold, t); }

    public Stage stageSpecimen(double t){ return super.customTime(this::specimen, t); }


}

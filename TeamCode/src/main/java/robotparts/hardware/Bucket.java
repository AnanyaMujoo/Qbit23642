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
        rbucket = create("rs", ElectronicType.PSERVO_FORWARD);
        lbucket = create("ls", ElectronicType.PSERVO_REVERSE);
        rbucket.setPosition("bottom", 0);
        lbucket.setPosition("bottom", 0);
        rbucket.setPosition("top", 0.66);
        lbucket.setPosition("top", 0.66);
        lbucket.setPosition("hold", 0.3);
        rbucket.setPosition("hold", 0.3);
        lbucket.setPosition("specimen", 0.55);
        rbucket.setPosition("specimen", 0.55);
        lbucket.setPosition("emergency", 0.45);
        rbucket.setPosition("emergency", 0.45);


    }

    private void move(String positionName){ rbucket.moveToPosition(positionName); lbucket.moveToPosition(positionName);}

    public void bottom(){ move("bottom"); }
    public void top(){ move("top"); }
    public void hold(){ move("hold"); }
    public void emergency(){ move("emergency"); }

    public void specimen(){ move("specimen"); }

    public Stage stageBottom(double t){ return super.customTime(this::bottom, t); }
    public Stage stageTop(double t){ return super.customTime(this::top, t); }

    public Stage stageHold(double t){ return super.customTime(this::hold, t); }

    public Stage stageEmergency(double t){ return super.customTime(this::emergency, t); }

    public Stage stageSpecimen(double t){ return super.customTime(this::specimen, t); }


}

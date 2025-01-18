package robotparts.hardware.old;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.positional.PServo;

public class Intaketest extends RobotPart {
    public PServo intake1;
    public PServo intake2;
    public PServo intake3;
    public PServo intake4;
    @Override
    public void init() {
        intake1 = create("rs", ElectronicType.PSERVO_REVERSE);
        intake2 = create("ls", ElectronicType.PSERVO_FORWARD);
        intake1.setPosition("bottom", 0);
        intake2.setPosition("bottom", 0);
        intake1.setPosition("top", 0.5);
        intake2.setPosition("top", 0.5);


    }

    private void move(String positionName){ intake1.moveToPosition(positionName); intake2.moveToPosition(positionName); }

    public void bottom(){ move("bottom"); }
    public void top(){ move("top"); }

    public Stage stageBottom(double t){ return super.customTime(this::bottom, t); }
    public Stage stageTop(double t){ return super.customTime(this::top, t); }



}

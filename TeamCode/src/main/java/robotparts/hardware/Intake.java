package robotparts.hardware;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CServo;
import robotparts.electronics.positional.PServo;

public class Intake extends RobotPart {
    public CServo intake1;
    public CServo intake2;
    public PServo intake3;
    public PServo intake4;
    @Override
    public void init() {
        intake1 = create("irs", ElectronicType.CSERVO_REVERSE);
        intake2 = create("ils", ElectronicType.CSERVO_FORWARD);
        intake3 = create("idr", ElectronicType.PSERVO_REVERSE);
        intake4 = create("idl", ElectronicType.PSERVO_FORWARD);

        //not configured
        intake3.setPosition("open", 0.2);
        intake4.setPosition("open", 0.2);
        intake3.setPosition("close", 0.2);
        intake4.setPosition("close", 0.2);

    }



    @Override
    public void move(double intakePower) {
        intake1.setPower(intakePower);
        intake2.setPower(intakePower);
    }
    private void moveDoor(String positionName){ intake3.moveToPosition(positionName); intake4.moveToPosition(positionName); }
    private void open(){moveDoor("open");}
    private void close(){moveDoor("open");}

    public Stage stageOpen(double t){ return super.customTime(this::open, t); }
    public Stage stageClose(double t){ return super.customTime(this::close, t); }

    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }

}

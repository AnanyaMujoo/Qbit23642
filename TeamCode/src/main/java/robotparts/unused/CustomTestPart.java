package robotparts.unused;

import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PServo;

public class CustomTestPart extends RobotPart {
    private PServo arm1, arm2, turn, claw;

    @Override
    public void init() {
        arm1 = create("arm1", ElectronicType.PSERVO_FORWARD);
        arm2 = create("arm2", ElectronicType.PSERVO_FORWARD);
        turn = create("turn", ElectronicType.PSERVO_FORWARD);
        claw = create("claw", ElectronicType.PSERVO_FORWARD);

        arm1.setPosition("start", 0.0);
        arm1.setPosition("end", 0.3);

        arm2.setPosition("start", 0.0);
        arm2.setPosition("end", 0.4);


        turn.setPosition("start", 0.0);
        turn.setPosition("flipped", 1.0);


        claw.setPosition("open", 0.0);
        claw.setPosition("close", 0.5);


        moveToStart();


    }

    public void moveToStart(){
        arm1.moveToPosition("start");
        arm2.moveToPosition("start");
        turn.moveToPosition("start");
        claw.moveToPosition("open");
    }

    public void openClaw(){
        claw.moveToPosition("open");
    }

    public void closeClaw(){
        claw.moveToPosition("close");
    }


    public void moveToEnd(){
        arm1.moveToPosition("end");
        arm2.moveToPosition("end");
        turn.moveToPosition("flipped");
        claw.moveToPosition("close");
    }
}

package robotparts.unused;

import com.qualcomm.robotcore.hardware.Servo;

import robotparts.RobotPart;
import robotparts.electronics.positional.PMotor;
import robotparts.electronics.positional.PServo;

public class CustomTestPart extends RobotPart {
    private PServo arm1, arm2, turn, claw;

    @Override
    public void init() {
        arm1 = createPServo("arm1", Servo.Direction.FORWARD, 0, 1);
        arm2 = createPServo("arm2", Servo.Direction.FORWARD, 0, 1);
        turn = createPServo("turn", Servo.Direction.FORWARD, 0, 1);
        claw = createPServo("claw", Servo.Direction.FORWARD, 0, 1);

        arm1.addPosition("start", 0.0);
        arm1.addPosition("end", 1.0);

        arm2.addPosition("start", 0.0);
        arm2.addPosition("end", 1.0);


        turn.addPosition("start", 0.0);
        turn.addPosition("flipped", 1.0);


        claw.addPosition("open", 0.0);
        claw.addPosition("close", 1.0);





    }

    public void moveToStart(){
        arm1.setPosition("start");
        arm2.setPosition("start");
        turn.setPosition("start");
        claw.setPosition("open");
    }

    public void openClaw(){
        claw.setPosition("open");
    }

    public void closeClaw(){
        claw.setPosition("close");
    }


    public void moveToEnd(){
        arm1.setPosition("end");
        arm2.setPosition("end");
        turn.setPosition("flipped");
    }
}

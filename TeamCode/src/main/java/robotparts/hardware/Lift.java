package robotparts.hardware;

import automodules.stage.Main;
import automodules.stage.Stage;
import global.Constants;
import robot.BackgroundTask;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;
import robotparts.electronics.positional.PServo;
import util.User;

public class Lift extends RobotPart {

    public PMotor motorRight;
    public PMotor motorLeft;
    public PServo testLeft, testRight;

    public double restPowUp = 0.02;
    public static final double maxPosition = 65; // 67

    @Override
    public void init() {
        motorRight = create("lil", ElectronicType.PMOTOR_REVERSE);
        motorLeft = create("lir", ElectronicType.PMOTOR_FORWARD);
        testLeft = create("tl", ElectronicType.PSERVO_REVERSE);
        testRight = create("tr", ElectronicType.PSERVO_FORWARD);
        motorRight.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.79, 0.25, 5);
        motorLeft.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.79, 0.25, 5);
        motorRight.usePositionHolder(restPowUp);
        motorRight.setPIDCoefficients(12, 3, 0);
        motorLeft.setPIDCoefficients(12, 3, 0);

        testLeft.addPosition("start", 0.0);
        testLeft.addPosition("end", 1.0);
        testRight.addPosition("start", 0.0);
        testRight.addPosition("end", 1.0);
        moveTestStart();
    }


    @Override
    public void move(double p) {
        if (p != 0) {
            motorRight.releasePosition();
            motorRight.move(p + restPowUp);
            motorLeft.move(p + restPowUp);
        } else {
            if(motorRight.getPosition() > 2) {
                motorRight.holdPosition();
                motorLeft.move(restPowUp);
            }else{
                motorRight.move(-0.05);
                motorLeft.move(-0.05);
            }
        }
    }

    public void moveTestStart(){
        testLeft.setPosition("start");
        testRight.setPosition("start");
    }

    public void moveTestEnd(){
        testLeft.setPosition("end");
        testRight.setPosition("end");
    }

    @Override
    protected Stage moveTime(double p, double t) {
        return super.moveTime(p, t);
    }

    @Override
    protected Stage moveNow(double p) {
        return super.moveNow(p);
    }

    public Stage stageLift(double power, double target) { return moveTarget(() -> motorRight, () -> motorLeft, power, power, target); }


    public BackgroundTask holdPosition(){ return new BackgroundTask(() -> {checkAccess(User.AUTO); move(0);}); }
}


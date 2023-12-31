package robotparts.hardware;

import automodules.AutoModule;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import util.codeseg.ReturnCodeSeg;

public class Drive extends RobotPart {

    public CMotor frontRight, backRight, frontLeft, backLeft;


    @Override
    public void init() {

        frontRight = create("fr", ElectronicType.CMOTOR_REVERSE_FLOAT);
        backRight = create("br", ElectronicType.CMOTOR_REVERSE_FLOAT);
        frontLeft = create("fl", ElectronicType.CMOTOR_FORWARD_FLOAT);
        backLeft = create("bl", ElectronicType.CMOTOR_FORWARD_FLOAT);

    }


    @Override
    public void move(double forwardPower, double strafePower, double turnPower) {
        frontRight.setPower(forwardPower - strafePower - turnPower);
        backRight.setPower(forwardPower + strafePower - turnPower);
        frontLeft.setPower(forwardPower + strafePower + turnPower);
        backLeft.setPower(forwardPower - strafePower + turnPower);
    }



















    @Override
    public Stage moveTime(double fp, double sp, double tp, double t) {
        return super.moveTime(fp, sp, tp, t);
    }

    @Override
    public Stage moveTime(double fp, double sp, double tp, ReturnCodeSeg<Double> t) {
        return super.moveTime(fp, sp, tp, t);
    }

    @Override
    public AutoModule MoveTime(double fp, double sp, double tp, double t) {
        return super.MoveTime(fp, sp, tp, t);
    }


}

package robotparts.hardware;

import automodules.AutoModule;
import automodules.stage.Stage;
import global.Modes;
import math.misc.Logistic;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;

import static global.Modes.DriveMode.Drive.MEDIUM;
import static global.Modes.DriveMode.Drive.SLOW;

public class Drive extends RobotPart {

    private CMotor fr, br, fl, bl;
    private final Logistic movementCurveForward = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 60.0, 2.0);
    private final Logistic movementCurveStrafe = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0, 6.0);
    private final Logistic movementCurveTurn = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0, 6.0);

    @Override
    public void init() {
        fr = create("fr", ElectronicType.CMOTOR_REVERSE);
        br = create("br", ElectronicType.CMOTOR_REVERSE);
        fl = create("fl", ElectronicType.CMOTOR_FORWARD);
        bl = create("bl", ElectronicType.CMOTOR_FORWARD);
        Modes.driveMode.set(MEDIUM);
//        throw new RuntimeException("HA HA YOU NOOB VIRUS VIRUS VIRUS");
    }

    @Override
    public void move(double f, double s, double t) { // TODO ANTI TIP?
        fr.setPower(f - s - t);
        br.setPower(f + s - t);
        fl.setPower(f + s + t);
        bl.setPower(f - s + t);
    }


    public void moveSmooth(double f, double s, double t) {
        double scale = Modes.driveMode.get().getValue(); move(movementCurveForward.fodd(f*scale), movementCurveStrafe.fodd(s*1.2*scale), movementCurveTurn.fodd(t*1.2*scale));
    }

    @Override
    public Stage moveTime(double fp, double sp, double tp, double t) {
        return super.moveTime(fp, sp, tp, t);
    }

    @Override
    public AutoModule MoveTime(double fp, double sp, double tp, double t) {
        return super.MoveTime(fp, sp, tp, t);
    }
}

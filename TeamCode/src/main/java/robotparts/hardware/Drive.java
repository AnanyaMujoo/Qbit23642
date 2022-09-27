package robotparts.hardware;

import automodules.AutoModule;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;

/**
 * NOTE: Uncommented
 */

/**
 * Create Motors
 */
public class Drive extends RobotPart {

    private CMotor fr, br, fl, bl;

    @Override
    public void init() {
        fr = create("fr", ElectronicType.CMOTOR_REVERSE);
        br = create("br", ElectronicType.CMOTOR_REVERSE);
        fl = create("fl", ElectronicType.CMOTOR_FORWARD);
        bl = create("bl", ElectronicType.CMOTOR_FORWARD);
    }

    /**
     * Raw movement
     *
     * @param f Forward Power
     * @param s Strafe Power
     * @param t Turn Power
     */
    @Override
    public void move(double f, double s, double t) {
        fr.setPower(f - s - t);
        br.setPower(f + s - t);
        fl.setPower(f + s + t);
        bl.setPower(f - s + t);
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

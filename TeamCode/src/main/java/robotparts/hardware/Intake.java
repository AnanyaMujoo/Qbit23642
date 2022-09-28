package robotparts.hardware;

import static global.General.bot;

import automodules.AutoModule;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;

public class Intake extends RobotPart {

    private CMotor in;
    /**
     * Create Intake Motor
     */
    @Override
    public void init() {
        in = create("in", ElectronicType.CMOTOR_FORWARD);
    }

    @Override
    public void move(double pow) {
        in.setPower(pow);
    }

    @Override
    public Stage moveTime(double p, double t) {
        return super.moveTime(p, t);
    }

    @Override
    public AutoModule MoveTime(double p, double t) {
        return super.MoveTime(p, t);
    }

    public Stage intakeUntilFreight(double p) {
        return super.customExit(p, color.exitFreight());
    }

}

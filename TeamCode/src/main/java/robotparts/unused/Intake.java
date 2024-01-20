package robotparts.unused;

import automodules.AutoModule;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;

public class Intake extends RobotPart {

    private CMotor in;

    @Override
    public void init() {
        in = create("in", ElectronicType.CMOTOR_FORWARD);
    }

    @Override
    public void move(double pow) {
        in.setPower(pow);
    }

    @Override
    public Stage moveTime(double power, double time) {
        return super.moveTime(power, time);
    }

    public Stage stageMoveUntilPixelsAreLoaded(double power) {
        return new Stage(
                usePart(),
                new Main(()->move(1)),
               new Exit(colorSensors::arePixelsLoaded),
                new Stop(()->move(0.0)),
                returnPart()

                );
    }



    @Override
    public AutoModule MoveTime(double p, double t) {
        return super.MoveTime(p, t);
    }


}

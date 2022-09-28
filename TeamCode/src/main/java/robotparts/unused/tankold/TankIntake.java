package robotparts.unused.tankold;

import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;

import static global.General.bot;

public class TankIntake extends RobotPart {
    /**
     * Intake motor
     */
    private CMotor in;

    /**
     * Init method creates the intake motor
     */
    @Override
    public void init() {
        in = create("in", ElectronicType.CMOTOR_FORWARD_FLOAT);
    }

    /**
     * Moves the intake motor at the given power
     * @param power
     */
    public void move(double power) {
        in.setPower(power);
    }

    /**
     * Moves the intake in a main (for automodules)
     * @param power
     * @return main
     */
    public Main main(double power) {
        return new Main(() -> move(power));
    }

    /**
     * Turns on the intake until a freight is detected
     * NOTE: Uses the colorsensor to detect this
     * @param power
     * @return stage
     */
    public Stage intakeUntilFreight(double power) { return new Stage(
        usePart(),
        main(power),
        bot.color.exitFreight(),
        stop(),
        returnPart()
    );}

    public Stage intakeUntilFreightLiftDown(double power) { return new Stage(
            usePart(),
            bot.tankLift.usePart(),
            bot.tankLift.main(-0.2),
            main(power),
            bot.color.exitFreight(),
            bot.tankLift.returnPart(),
            returnPart()
    );}


    public Stage intakeTime(double power, double time) { return new Stage(
            usePart(),
            main(power),
            exitTime(time),
            stop(),
            returnPart()
    );}
}

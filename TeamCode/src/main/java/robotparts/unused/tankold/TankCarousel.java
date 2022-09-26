package robotparts.unused.tankold;

import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CServo;

public class TankCarousel extends RobotPart {
    /**
     * Carousel left and right (cl and cr respectively)
     */
    public CServo cl;
    public CServo cr;

    /**
     * Init method creates the cservos (or continuous servos)
     */
    @Override
    public void init() {
        cl = create("cl", ElectronicType.CSERVO_REVERSE);
        cr = create("cr", ElectronicType.CMOTOR_FORWARD);
    }

    /**
     * Move the carousels at the given power
     * NOTE: Positive should be so that the right carousel spins clockwise
     * @param p
     */
    public void move(double p){
        cl.setPower(p);
        cr.setPower(p);
    }

    private Main mainSpin() {
        return new Main(() -> move(1));
    }

    private Stop mainStop() {
        return new Stop(() -> move(0));
    }

    public Stage spin(double time) {
        return new Stage(
            usePart(),
            mainSpin(),
            exitTime(time),
            mainStop()
        );
    }
}

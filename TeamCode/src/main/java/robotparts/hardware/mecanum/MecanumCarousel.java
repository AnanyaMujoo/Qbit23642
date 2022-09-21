package robotparts.hardware.mecanum;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

import static global.General.*;

import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import elements.FieldSide;
import math.polynomial.Linear;
import robotparts.RobotPart;
import robotparts.electronics.continuous.CMotor;
import util.Timer;

public class MecanumCarousel extends RobotPart {
    // TODO 4 Please make automodules easier to make
    private CMotor carousel;

    private final Timer timerA = new Timer();

    @Override
    public void init() {
        carousel = createCMotor("car", DcMotorSimple.Direction.REVERSE);
    }

    public void move(double power) {
        carousel.setPower(power * (fieldSide == FieldSide.BLUE ? -1 : 1));
    }

    private Main mainSpin() { return new Main(() -> move(1)); }

    private Stop stopSpin() { return new Stop(() -> move(0)); }

    public Stage spin(double time) {
        return new Stage(
            usePart(),
            mainSpin(),
            exitTime(time),
            stopSpin(),
            returnPart()
        );
    }

    private Stop stop() {
        return new Stop(() -> move(0));
    }

    public Stage spinOneDuck(double time, double minPow, double maxPow) {
        return new Stage(
                usePart(),
                new Initial(timerA::reset),
                new Main(() ->{
                    double secs = timerA.seconds();
                    double halfTime = time/2;
                    double slope = (maxPow-minPow)/halfTime;
                    Linear l1 = new Linear(slope, minPow);
                    Linear l2 = new Linear(-slope,l1.f(halfTime));
                    if(secs < halfTime) {
                        move(-l1.f(secs));
                    }else{
                        move(-l2.f(secs-halfTime));
                    }
                }),
                exitTime(time),
                stop(),
                returnPart()
        );
    }


    public Stage spinOneDuckMoving(double time, double minPow, double maxPow,double k,  double f, double s, double t) {
        return new Stage(
                usePart(),
                drive.usePart(),
                new Initial(timerA::reset),
                drive.mainMove(f,s,t),
                new Main(() ->{
                    double secs = timerA.seconds();
                    double halfTime = time/2;
                    double slope = (maxPow-minPow)/halfTime;
                    Linear l1 = new Linear(slope, minPow);
                    Linear l2 = new Linear(-slope,l1.f(halfTime));
                    if(secs < halfTime) {
                        move(k*l1.f(secs));
                    }else{
                        move(k*l2.f(secs-halfTime));
                    }
                }),
                exitTime(time),
                stop(),
                drive.stopMove(),
                drive.returnPart(),
                returnPart()
        );
    }

}

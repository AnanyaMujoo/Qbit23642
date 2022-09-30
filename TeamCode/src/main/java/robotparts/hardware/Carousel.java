package robotparts.hardware;

import java.sql.Time;

import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import elements.FieldSide;
import math.polynomial.Linear;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import util.Timer;

import static global.General.fieldSide;

public class Carousel extends RobotPart {

    public CMotor car;
    private final Timer timer = new Timer();

    @Override
    public void init() {
        car = create("car", ElectronicType.CMOTOR_REVERSE);
    }

    @Override
    public void move(double power) {
        car.setPower(power * (fieldSide == FieldSide.BLUE ? -1 : 1));
    }


    public Stage spinOneDuck(double time, double minPow, double maxPow) {
        return customTime(new Main(() ->{
            double secs = timer.seconds();
            double halfTime = time/2;
            double slope = (maxPow-minPow)/halfTime;
            Linear l1 = new Linear(slope, minPow);
            Linear l2 = new Linear(-slope,l1.f(halfTime));
            if(secs < halfTime) {
                move(-l1.f(secs));
            }else{
                move(-l2.f(secs-halfTime));
            }
        }), time).combine(timer.initialReset());
    }

}

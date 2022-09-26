package robotparts.unused.tankold;

import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import math.misc.Logistic;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.positional.PServo;
import util.User;

import static global.General.*;

public class TankDrive extends RobotPart {
    /**
     * Contunous motors for drivetrain
     */
    private CMotor fr,br,fl,bl;
    /**
     * Positional Servo for retracting odometer
     */
    private PServo re;

    /**
     * Init method to create all of the motors and the servo
     */
    @Override
    public void init(){
        fr = create("fr", ElectronicType.CMOTOR_REVERSE);
        br = create("br", ElectronicType.CMOTOR_REVERSE);
        fl = create("fl", ElectronicType.CMOTOR_FORWARD);
        bl = create("bl", ElectronicType.CMOTOR_FORWARD);
        re = create("re", ElectronicType.PSERVO_REVERSE);
        re.addPosition("up", 1);
        re.addPosition("down", 0.2);
        if(mainUser.equals(User.AUTO)){
            re.setPosition("down");
        }else if(mainUser.equals(User.TELE)){
            re.setPosition("up");
        }
    }

    /**
     * Move the robot forward at power f and turn at power t
     * @param f
     * @param t
     */
    public void move(double f, double t){
        fr.setPower(f-t);
        br.setPower(f-t);
        fl.setPower(f+t);
        bl.setPower(f+t);
    }

    public void moveOdometry(String m){ re.setPosition(m);}
    public void raise(){ moveOdometry("up");}
    public void lower(){ moveOdometry("down");}

    /**
     * Move the robot using logistic curves (to make it easier to use in teleop)
     * @param f
     * @param t
     */
    public void moveSmooth(double f, double t){
        Logistic movementCurveForward = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 10.0,5.0);
        Logistic movementCurveTurn = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0,6.0);
        move(movementCurveForward.fodd(f), movementCurveTurn.fodd(t));
    }

    public Main liftOdoMain() {
        return new Main(this::raise);
    }

    public Main main(double forward, double turn){
        return new Main(() -> move(forward, turn));
    }

    public Stop stop(){
        return new Stop(() -> move(0,0));
    }

    public Stage liftOdo() {
        return new Stage(
            liftOdoMain(),
            exitAlways()
        );
    }

    public Stage moveTime(double forward, double turn, double time) {
        return new Stage(
            usePart(),
            main(forward, turn),
            exitTime(time),
            stop()
        );
    }
}

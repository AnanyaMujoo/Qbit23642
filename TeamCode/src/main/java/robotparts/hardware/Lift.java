package robotparts.hardware;

import static global.General.log;

import automodules.stage.Main;
import automodules.stage.Stage;
import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;
import util.codeseg.ReturnCodeSeg;

public class Lift extends RobotPart {

    public PMotor liftRight;
    public PMotor liftLeft;
    public final static double max_height = 60; //cm, not exact
    public final static double min_height = 0;
    @Override
    public void init() {

        //
        liftRight = create("lil", ElectronicType.PMOTOR_FORWARD);
        liftLeft = create("lir", ElectronicType.PMOTOR_REVERSE);

        liftRight.setToLinear(Constants.ORBITAL_ENCODER_TICKS_PER_REVOLUTION, 2.4, 1.0, 30);
        liftLeft.setToLinear(Constants.ORBITAL_ENCODER_TICKS_PER_REVOLUTION, 2.4, 1.0, 30);

        liftRight.usePositionHolder(0.05, 0.05);
        liftLeft.usePositionHolder(0.05, 0.05);
    reset();
    }



    @Override

    public void move(double liftPower) {
        // dpad
        //if current height of lift is greater than max lift height, don't move up any further.
        if(max_height-liftRight.getPosition()>5 && liftRight.getPosition()-min_height>5){
            liftRight.moveWithPositionHolder(liftPower,  0.05);
            liftLeft.moveWithPositionHolder(liftPower,  0.05);
            //log.show(lift.liftLeft.getPosition());
            //log.show(lift.liftRight.getPosition());
        }
        else{
            //log.show(lift.liftLeft.getPosition());
            //log.show(lift.liftRight.getPosition());
        }
    }

    @Override
    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }

    @Override
    public Stage moveTime(double p, ReturnCodeSeg<Double> t) { return super.moveTime(p, t); }



    public Stage stageLift(double power, double target) { return moveTarget(() -> liftRight, () -> liftLeft, power, power, target); }

    @Override
    public void maintain() { super.maintain(); }

    public void reset(){ liftRight.softReset(); liftLeft.softReset(); }

    public Stage resetLift(){ return new Stage(usePart(), new Main(this::reset), exitTime(0.1), stop(), returnPart()); }




//
//   TODO FIX BOT This was in TELEOP
//    @Override
//    public void startTele() {
//        lift.reset();
//    }
}


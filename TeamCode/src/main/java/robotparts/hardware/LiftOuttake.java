package robotparts.hardware;

import automodules.AutoModuleUser;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.positional.PMotor;
import util.codeseg.ReturnParameterCodeSeg;

public class LiftOuttake extends RobotPart implements AutoModuleUser {

    public PMotor liftRight;
    public PMotor liftLeft;

//    public double target = 0;
//    public final double MAXHEIGHT = 40;
    public final ReturnParameterCodeSeg<Double, Double> restPowerFunction = height -> {
        return (height/40)*0.01;
    };


    @Override
    public void init() {
//        liftRight = create("rlh", ElectronicType.PMOTOR_FORWARD);
//        liftLeft = create("llh", ElectronicType.PMOTOR_REVERSE);
        liftLeft = create("llift", ElectronicType.PMOTOR_FORWARD);
        liftRight = create("rlift", ElectronicType.PMOTOR_REVERSE);

//        liftLeft = create("llv", ElectronicType.CMOTOR_FORWARD);
//        liftRight = create("rlv", ElectronicType.CMOTOR_REVERSE);

        liftLeft.setToLinearOrbitalMotorVertical(2.7);
        liftRight.setToLinearOrbitalMotorVertical(2.7);

        liftLeft.usePositionHolder(restPowerFunction, 0.05);
        liftRight.usePositionHolder(restPowerFunction, 0.05);

        liftLeft.useSnapToZero(2, -0.05);
        liftRight.useSnapToZero(2, -0.05);

//
//        target = 0;
//TODO Test life and stages (max height and intervals)

    }
//    public ReturnCodeSeg<AutoModule> lifttarget(double inc){
//        return ()->{
//            if ((target+inc>=0)&&(target+inc<=MAXHEIGHT)){
//                target+=inc;
//                return Lift(target);
//
//            }
//            else if(target+inc<=0){
//                target=0;
//                return Lift(target);
//            }
//          return new AutoModule();
//        };
//    }


    @Override
    public void move(double liftPower) {
        liftRight.moveWithPositionHolder(liftPower);
        liftLeft.moveWithPositionHolder(liftPower);
//        liftLeft.setPower(liftPower);
//        liftRight.setPower(liftPower);
    }









//
//    @Override
//    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }
//
//    @Override
//    public Stage moveTime(double p, ReturnCodeSeg<Double> t) { return super.moveTime(p, t); }
//
//
//
//    public Stage stageLift(double power, double target) { return moveTarget(() -> liftRight, () -> liftLeft, power, power, target); }
//
//    @Override
//    public void maintain() { super.maintain(); }
//
//    public void reset(){ liftRight.softReset(); liftLeft.softReset(); }
//    public void hardReset(){ liftRight.resetPosition(); liftLeft.resetPosition(); }
//
//    public Stage resetLift(){ return new Stage(usePart(), new Main(this::reset), exitTime(0.1), stop(), returnPart()); }



//
//   TODO FIX BOT This was in TELEOP
//    @Override
//    public void startTele() {
//        lift.reset();
//    }
}


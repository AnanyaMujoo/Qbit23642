package robotparts.hardware;

import automodules.AutoModuleUser;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.positional.PMotor;
import util.codeseg.ReturnParameterCodeSeg;

public class LiftOuttake extends RobotPart implements AutoModuleUser {

    public PMotor liftRight;
    public PMotor liftLeft;

//    public double target = 0;
    public final double MAX_HEIGHT = 94.3;


    public final ReturnParameterCodeSeg<Double, Double> restPowerFunction = height -> {
        if (height > 30) {
            return Math.pow(height / 40, 2) * 0.02;
        }else {
            return  0.0;
        }
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

    public void setToAuto(){
        liftLeft.useSnapToZero(0.01, -0.01);
        liftRight.useSnapToZero(0.01, -0.01);
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
        if ((liftRight.getPosition() < 0 || liftLeft.getPosition() < 0) && liftPower < 0) {
            liftRight.getPositionHolder().deactivate();
            liftRight.move(0.0);
            liftLeft.getPositionHolder().deactivate();
            liftLeft.move(0.0);
        }else if((liftRight.getPosition() > MAX_HEIGHT || liftLeft.getPosition() > MAX_HEIGHT) && liftPower > 0){
            liftRight.getPositionHolder().deactivate();
            liftRight.move(restPowerFunction.run(liftRight.getPosition()));
            liftLeft.getPositionHolder().deactivate();
            liftLeft.move(restPowerFunction.run(liftLeft.getPosition()));
        }else {
            liftRight.moveWithPositionHolder(liftPower);
            liftLeft.moveWithPositionHolder(liftPower);
        }

    }







    public void softReset(){
        liftRight.resetPosition();
        liftLeft.resetPosition();
    }


    @Override
    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }

    public Stage moveTimeSus(double p, double t){
        return new Stage(usePart(), new Main(() -> {
            liftRight.setPowerRaw(p);
            liftLeft.setPowerRaw(p);
        }), exitTime(t), stop(), returnPart());
    }
//
//
    public Stage stageLift(double power, double target) { return moveTarget(() -> liftRight, () -> liftLeft, power, power, target); }

    public Stage stageDown(double power, double target){
        return new Stage(
                usePart(),
                new Main(() -> {
                    liftRight.move(-Math.abs(power));
                    liftLeft.move(-Math.abs(power));
                }),
                new Exit(() -> liftLeft.getPosition() < target || liftRight.getPosition() < target),
                stop(),
                returnPart()
        );
    }

    @Override
    public void maintain() {
        super.maintain();
    }
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


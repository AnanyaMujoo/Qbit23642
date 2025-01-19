package robotparts.hardware;

import automodules.AutoModule;
import automodules.AutoModuleUser;
import automodules.stage.Exit;
import automodules.stage.Main;
import automodules.stage.Stage;
import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.positional.PMotor;
import util.codeseg.ReturnCodeSeg;

public class LiftIntake extends RobotPart implements AutoModuleUser {

    public PMotor liftRight;
    public PMotor liftLeft;
//    public double target = 0;
    public final double MAX_HEIGHT = 45;


    @Override
    public void init() {
//        liftRight = create("rlh", ElectronicType.PMOTOR_FORWARD);
//        liftLeft = create("llh", ElectronicType.PMOTOR_REVERSE);
        liftLeft = create("llh", ElectronicType.PMOTOR_REVERSE);
        liftRight = create("rlh", ElectronicType.PMOTOR_FORWARD);


        liftRight.setToLinearOrbitalMotorHorizontal(2.3);
        liftLeft.setToLinearOrbitalMotorHorizontal(2.3);

        liftRight.usePositionHolder(0.0, 0.05);
        liftLeft.usePositionHolder(0.0, 0.05);
        liftLeft.useSnapToZero(2, -0.05);
        liftRight.useSnapToZero(2, -0.05);
//
//        target = 0;
//TODO Test life and stages (max height and intervals)

    }
    public Stage stageLift(double power, double target) { return moveTarget(() -> liftRight, () -> liftLeft, power, power, target); }

    public Stage stageDelta(double power, double delta){ return moveTarget(() -> liftRight, () -> liftLeft, power, power, () -> {
            double newTarget = liftRight.getTarget() + delta;
            if(newTarget < 0){
                return 0.0;
            }else if(newTarget > MAX_HEIGHT){
                return MAX_HEIGHT;
            }else{
                return newTarget;
            }
        }
    );}


    public void softReset(){
        liftRight.resetPosition();
        liftLeft.resetPosition();
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


    public Stage moveTimeSus(double p, double t){
        return new Stage(usePart(), new Main(() -> {
            liftRight.setPowerRaw(p);
            liftLeft.setPowerRaw(p);
        }), exitTime(t), stop(), returnPart());
    }


    @Override
    public void move(double liftPower) {
        liftRight.moveWithPositionHolder(liftPower);
        liftLeft.moveWithPositionHolder(liftPower);
//        liftLeft.setPower(liftPower);
//        liftRight.setPower(liftPower);
    }



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


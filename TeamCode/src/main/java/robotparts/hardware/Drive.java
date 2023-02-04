package robotparts.hardware;

import automodules.AutoModule;
import automodules.stage.Stage;
import autoutil.reactors.MecanumJunctionReactor2;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Modes;
import math.misc.Logistic;
import math.polynomial.Linear;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import robotparts.electronics.positional.PServo;
import teleutil.TeleTrack;
import util.codeseg.ReturnCodeSeg;
import util.template.Precision;

import static global.General.bot;
//import static global.Modes.driveMode;

public class Drive extends RobotPart {

    private CMotor fr, br, fl, bl;

    public boolean slow = false;
//    private PServo retract;


    @Override
    public void init() {
        fr = create("fr", ElectronicType.CMOTOR_REVERSE);
        br = create("br", ElectronicType.CMOTOR_REVERSE);
        fl = create("fl", ElectronicType.CMOTOR_FORWARD);
        bl = create("bl", ElectronicType.CMOTOR_FORWARD);
//
//        retract = create("ret", ElectronicType.PSERVO_FORWARD);
//
//        retract.changePosition("start", 0.03);
//        retract.changePosition("end", 1.0);

//        engage();


        slow = false;
//        throw new RuntimeException("HA HA YOU NOOB VIRUS VIRUS VIRUS");
    }

//    public void retract(){ retract.setPosition("end"); }
//    public void engage(){ retract.setPosition("start"); }

//    public Stage stageRetract(){ return customTime(this::retract, 0.0); }
//    public Stage stageEngage(){ return customTime(this::engage, 0.0); }

    @Override
    public void move(double f, double s, double t) {
        Vector power = new Vector(Precision.clip(s, 1), Precision.clip(f, 1));
        power.scaleX(1.2);
        power.limitLength(1);
        f = power.getY(); s = power.getX(); t = Precision.clip(t, 1);
        fr.setPower(f - s - t);
        br.setPower(f + s - t);
        fl.setPower(f + s + t);
        bl.setPower(f - s + t);
    }



    public void moveSmooth(double f, double s, double t) {
        if(!bot.indHandler.isIndependentRunning()) {
            Logistic rt = new Logistic(Logistic.LogisticParameterType.RP_K, 0.12, 1.0);
            Logistic rm = new Logistic(Logistic.LogisticParameterType.RP_K, 0.05, 5.0);
            Linear rx = new Linear(1.0, 0.7, 1.0);

            f = rm.fodd(f) * (t != 0 ? rx.feven(t) : 1.0);
            s = !Precision.range(s, 0.7) || slow ? rm.fodd(s) * 0.6 : 0.0;
            t = rt.fodd(t) * 0.7;

            if(slow) {
                drive.move(f*0.5, s*0.5, t*0.5);
            }else{
                drive.move(f, s, t);
            }
        }
    }



    @Override
    public Stage moveTime(double fp, double sp, double tp, double t) {
        return super.moveTime(fp, sp, tp, t);
    }

    @Override
    public Stage moveTime(double fp, double sp, double tp, ReturnCodeSeg<Double> t) {
        return super.moveTime(fp, sp, tp, t);
    }

    @Override
    public AutoModule MoveTime(double fp, double sp, double tp, double t) {
        return super.MoveTime(fp, sp, tp, t);
    }


    public double getAntiTippingPower(){
//        double pitch = gyro.getPitch();
//        double pitchDerivative = Math.abs(gyro.getPitchDerivative());
//        if(pitch > -1.5){
//            return 0;
//        }else{
//            return pitch*0.15/(pitchDerivative > 0.7 ? Math.pow(Math.abs(pitchDerivative), 0.5) : 1.0);
//        }
        return 0;
    }

    public Stage changeSlow(boolean slow){
        return customTime(() -> this.slow = slow, 0.0);
    }

}

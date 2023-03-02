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
import static global.Modes.Drive.FAST;
import static global.Modes.Drive.MEDIUM;
import static global.Modes.Drive.SLOW;
import static global.Modes.driveMode;
//import static global.Modes.driveMode;

public class Drive extends RobotPart {

    private CMotor fr, br, fl, bl;

    private final Precision precision = new Precision();
    private final Precision precision2 = new Precision();

    public boolean noStrafeLock = false;

//    public boolean slow = false;
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

        noStrafeLock = false;

        driveMode.set(MEDIUM);
        precision.reset();
        precision2.reset();
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

    public void moveWithoutVS(double f, double s, double t) {
        Vector power = new Vector(Precision.clip(s, 1), Precision.clip(f, 1));
        power.scaleX(1.2);
        power.limitLength(1);
        f = power.getY(); s = power.getX(); t = Precision.clip(t, 1);
        fr.setPowerRaw(f - s - t);
        br.setPowerRaw(f + s - t);
        fl.setPowerRaw(f + s + t);
        bl.setPowerRaw(f - s + t);
    }

    public void moveSmooth(double f, double s, double t) {
        if(!bot.indHandler.isIndependentRunning()) {
            Logistic rt = new Logistic(Logistic.LogisticParameterType.RP_K, 0.12, 1.0);
            Logistic rm = new Logistic(Logistic.LogisticParameterType.RP_K, 0.05, 5.0);
            Linear rx = new Linear(1.0, 0.4, 1.0);

            if(!driveMode.modeIs(SLOW)) {
                if (precision.outputTrueForTime(precision.isInputTrueForTime(Math.abs(f) > 0.9, 0.5), 0.2) && Math.abs(f) > 0.9) {
                    driveMode.set(FAST);
                } else {
                    driveMode.set(MEDIUM);
                }
            }

            if(driveMode.modeIs(SLOW)) {
                drive.move(rm.fodd(f*0.4),  noStrafeLock || !Precision.range(s, 0.7) ? rm.fodd(s)*0.3 : 0.0, rt.fodd(t*0.6));
            }else if(driveMode.modeIs(MEDIUM)){
                if(precision2.outputTrueForTime(precision2.isInputTrueForTime(Math.abs(t) > 0.9, 0.5), 0.2) && Math.abs(t) > 0.9){
                    drive.move(rm.fodd(f*0.7) * (t != 0 ? rx.feven(t) : 1.0), !Precision.range(s, 0.7) ? rm.fodd(s*0.7) : 0.0, t);
                }else{
                    drive.move(rm.fodd(f*0.7) * (t != 0 ? rx.feven(t) : 1.0), !Precision.range(s, 0.7) ? rm.fodd(s*0.7) : 0.0, rt.fodd(t*0.85));
                }
            }else{
                drive.move(rm.fodd(f) * (t != 0 ? rx.feven(t) : 1.0), 0.0, rt.fodd(t*0.8));
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

}

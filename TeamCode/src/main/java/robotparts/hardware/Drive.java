package robotparts.hardware;

import automodules.AutoModule;
import automodules.stage.Stage;
import autoutil.reactors.MecanumJunctionReactor2;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Modes;
import math.linearalgebra.Vector3D;
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

    public double[] currentPower = new double[3];
    public double[] deltaPower = new double[3];

    public double fast1 = 0;
    public double fast2 = 0;

//    public boolean slow = false;
//    private PServo retract;


    @Override
    public void init() {
//        fr = create("fr", ElectronicType.CMOTOR_REVERSE);
//        br = create("br", ElectronicType.CMOTOR_REVERSE);
//        fl = create("fl", ElectronicType.CMOTOR_FORWARD);
//        bl = create("bl", ElectronicType.CMOTOR_FORWARD);


        fr = create("fr", ElectronicType.CMOTOR_REVERSE_FLOAT);
        br = create("br", ElectronicType.CMOTOR_REVERSE_FLOAT);
        fl = create("fl", ElectronicType.CMOTOR_FORWARD_FLOAT);
        bl = create("bl", ElectronicType.CMOTOR_FORWARD_FLOAT);
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

        currentPower = new double[3];
        deltaPower = new double[3];

        fast1 = 0;
        fast2 = 0;
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

    public void help(double[] power, int i, double cutoff, double accel, double decel){
        if(Math.abs(power[i]) > cutoff){
//            deltaPower[i] += Math.abs(accel*power[i]);
//            currentPower[i] = Math.signum(power[i]) * (deltaPower[i] + cutoff);
            currentPower[i] = Math.signum(power[i])*cutoff;
        }else{
            currentPower[i] = power[i];
//            deltaPower[i] = Math.max(0, deltaPower[i] - decel);
        }
    }

    public void moveSmooth(double f, double s, double t) {
        if(!bot.indHandler.isIndependentRunning()) {


            Logistic rt = new Logistic(Logistic.LogisticParameterType.RP_K, 0.12, 1.0);
            Logistic rm = new Logistic(Logistic.LogisticParameterType.RP_K, 0.05, 5.0);
            Linear rx = Linear.one(1.0, 0.35);

            boolean forward = precision.isInputTrueForTime(Math.abs(f) > 0.9, 0.2) && Math.abs(f) > 0.9;
            boolean turn = precision2.isInputTrueForTime(Math.abs(t) > 0.9, 0.2) && Math.abs(t) > 0.9;

            double old = rm.fodd(f*0.6) * rx.fevenb(t, 1.0);
            double real = rm.fodd(f) * rx.fevenb(Precision.clip(t*2.0, 1), 1.0);
            Linear linear1 = Linear.one(old, real);

            double old2 = rm.fodd(s*0.6) * rx.fevenb(Precision.clip(f*2.0, 1), 1.0);

            double old3 = rt.fodd(t*0.7);
            Linear linear3 = Linear.one(old3, t);



            if(driveMode.modeIs(SLOW)) {
                drive.move(rm.fodd(f*0.4),  rm.fodd(s*0.9)*0.3, rt.fodd(t*0.6));
                fast1 = 0; fast2 = 0;
            }else {
                double xraw = s*0.8;
                double traw = t*0.8;
                double xnew = Math.abs(f) > 0.5 ? Precision.attract(xraw, 0.0, 0.4) : xraw;
                double tnew = Math.abs(f) > 0.5 ? traw : traw*0.5;
                drive.move(f, xnew, tnew);

//                if(forward){
//                    fast1+=0.02;
//                    if(turn){
//                        fast2+=0.03;
//                        drive.move(0.8*linear1.f(fast1), 0.0, linear3.f(fast2));
//                    }else{
//                        drive.move(linear1.f(fast1), 0.0, rt.fodd(t*0.7));
//                        fast2 = Math.max(0, fast2 - 0.02);
//                        if(fast2 != 0.0){
//                            precision2.reset();
//                        }
//                    }
//                }else{
//                    if(turn){
//                        fast2+=0.03;
//                        drive.move(old, old2, linear3.f(fast2));
//                    }else{
//                        drive.move(old, old2, old3);
//                        fast2 = Math.max(0, fast2 - 0.02);
//                        if(fast2 != 0.0){
//                            precision2.reset();
//                        }
//                    }
//                    if(fast1 != 0.0){
//                        precision.reset();
//                    }
//                    fast1 = 0;
//                }
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

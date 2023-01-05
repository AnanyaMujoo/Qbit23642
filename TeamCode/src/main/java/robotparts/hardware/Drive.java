package robotparts.hardware;

import automodules.AutoModule;
import automodules.stage.Stage;
import autoutil.AutoFramework;
import autoutil.vision.JunctionScannerAll;
import elements.FieldSide;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Modes;
import math.linearalgebra.Vector3D;
import math.misc.Logistic;
import math.polynomial.Linear;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import util.template.Precision;

import static global.General.fieldSide;
import static global.General.gph1;
import static global.Modes.AttackMode.STICKY;

public class Drive extends RobotPart {

    private CMotor fr, br, fl, bl;
    private final Logistic movementCurveForward = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 60.0, 2.0);
    private final Logistic movementCurveStrafe = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0, 6.0);
    private final Logistic movementCurveTurn = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0, 6.0);
//
//    private final Logistic movementCurveForward = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 1000.0, 6.0);
//    private final Logistic movementCurveStrafe = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 1000.0, 6.0);
//    private final Logistic movementCurveTurn = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 1000.0, 6.0);


    @Override
    public void init() {
        fr = create("fr", ElectronicType.CMOTOR_REVERSE);
        br = create("br", ElectronicType.CMOTOR_REVERSE);
        fl = create("fl", ElectronicType.CMOTOR_FORWARD);
        bl = create("bl", ElectronicType.CMOTOR_FORWARD);
        Modes.driveMode.set(Modes.Drive.SLOW);
        Modes.attackMode.set(Modes.AttackMode.NORMAL);
//        throw new RuntimeException("HA HA YOU NOOB VIRUS VIRUS VIRUS");
    }

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
        Vector3D attackPow = new Vector3D();
        if(Modes.attackMode.modeIs(STICKY)) {
            Linear yCurve = new Linear(0.02, 0.06);
            Linear hCurve = new Linear(0.008, 0.04);
            Pose error = JunctionScannerAll.getError();
            Vector junctionPow = new Vector(0, yCurve.fodd(error.getY()));
            junctionPow.rotate(-error.getAngle()); junctionPow.limitLength(Math.abs(JunctionScannerAll.rollOfJunction) > 1 ? 0.1 : 0.4);
            attackPow = new Vector3D(junctionPow, Precision.clip(hCurve.fodd(error.getAngle()), Math.abs(JunctionScannerAll.rollOfJunction) > 1 ? 0.1 : 0.4));
        }
        Pose power = drive.getMoveSmoothPower(f, s, t);
        drive.move(attackPow.getY() + power.getX(), power.getY(), attackPow.getZ() + power.getAngle());
    }

    public Pose getMoveSmoothPower(double f, double s, double t){
        double scale = Modes.driveMode.getValue();
        return new Pose(movementCurveForward.fodd(f*scale),movementCurveStrafe.fodd(s*scale), movementCurveTurn.fodd(Modes.driveMode.modeIs(Modes.Drive.MEDIUM) ? 0.7*t*scale : t*1.2*scale));
    }

    @Override
    public Stage moveTime(double fp, double sp, double tp, double t) {
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

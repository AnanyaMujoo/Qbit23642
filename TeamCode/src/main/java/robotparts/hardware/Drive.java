package robotparts.hardware;

import automodules.AutoModule;
import automodules.stage.Stage;
import autoutil.reactors.MecanumJunctionReactor2;
import geometry.position.Pose;
import geometry.position.Vector;
import global.Modes;
import math.misc.Logistic;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;
import util.codeseg.ReturnCodeSeg;
import util.template.Precision;

import static global.General.bot;
import static global.Modes.AttackStatus.ATTACK;
import static global.Modes.Drive.MEDIUM;
import static global.Modes.Drive.SLOW;
import static global.Modes.attackStatus;

public class Drive extends RobotPart {

    private CMotor fr, br, fl, bl;
    private final Logistic movementCurveForward = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 60.0, 2.0);
    private final Logistic movementCurveStrafe = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0, 6.0);
    private final Logistic movementCurveTurn = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 30.0, 6.0);

    private final MecanumJunctionReactor2 mecanumJunctionReactor2 = new MecanumJunctionReactor2();
    //
//    private final Logistic movementCurveForward = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 1000.0, 6.0);
//    private final Logistic movementCurveStrafe = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 1000.0, 6.0);
//    private final Logistic movementCurveTurn = new Logistic(Logistic.LogisticParameterType.ONE_ONE, 1000.0, 6.0);



    private final double[][] powers = {new double[]{0.35, 0.35, 0.4}, new double[]{0.75, 0.6, 0.5}, new double[]{1.0, 1.0, 1.0}};


    @Override
    public void init() {
        fr = create("fr", ElectronicType.CMOTOR_REVERSE);
        br = create("br", ElectronicType.CMOTOR_REVERSE);
        fl = create("fl", ElectronicType.CMOTOR_FORWARD);
        bl = create("bl", ElectronicType.CMOTOR_FORWARD);
        Modes.driveMode.set(SLOW);
        attackStatus.set(Modes.AttackStatus.REST);
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
        if(!bot.indHandler.isIndependentRunning()) {
            Pose power = getMoveSmoothPower(f, s, t);
            drive.move(power.getX(), power.getY(), power.getAngle());
//            mecanumJunctionReactor2.move(attackStatus.modeIs(ATTACK), drive.getMoveSmoothPower(f, s, t));
        }
    }

    public Pose getMoveSmoothPower(double f, double s, double t){
        double[] scales = powers[Modes.driveMode.modeIs(SLOW) ? 0 : Modes.driveMode.modeIs(MEDIUM) ? 1 : 2];
        return new Pose(movementCurveForward.fodd(f*scales[0]),movementCurveStrafe.fodd(s*scales[1]), movementCurveTurn.fodd(t*scales[2]));
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

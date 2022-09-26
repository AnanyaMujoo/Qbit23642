package robotparts.unused.tank;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

import global.Constants;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;
import robotparts.hardware.Lift;
import util.codeseg.ReturnParameterCodeSeg;

public class TankLift extends Lift {


    @Override
    public void init() {
        super.init();
        motors[0].useStallDetector(0.2, Constants.LIFT_REST_POW,200,0.03, 2);
    }

    @Override
    protected double[] getRestPows() {
        return new double[]{Constants.LIFT_REST_POW};
    }

    @Override
    protected double getAngle() {
        return Math.PI/2;
    }

    @Override
    public PMotor[] getMotors() {
        return new PMotor[] {
            create("li", ElectronicType.PMOTOR_FORWARD)
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public ReturnParameterCodeSeg<Double, Double>[] getTargetConvertors() {
        return new ReturnParameterCodeSeg[] {
                h -> (Double)h * Constants.LIFT_CM_TO_TICKS
        };
    }

    @Override
    public void move(double pow) {
        for(PMotor p: motors){
            p.setPower(pow);
        }
    }

}

package robotparts.unused.mecanumold;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Arrays;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stop;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;
import util.codeseg.ReturnParameterCodeSeg;

@Deprecated
public abstract class TwoLift extends RobotPart {

    public PMotor motorUp;
    public PMotor motorDown;

    protected abstract double[] getRestPows();

    protected abstract double getAngle();

    public abstract double CM_PER_TICK();

    public abstract void move(double pow);
    public abstract void moveDown(double pow);

    @Override
    public void init() {
        motorUp = create("lil", ElectronicType.PMOTOR_REVERSE);
        motorDown = create("lir", ElectronicType.PMOTOR_REVERSE_FLOAT);
        resetEncoder();
    }

    public void resetEncoder(){
        motorUp.resetPosition();
        motorDown.resetPosition();
    }

    public Initial initialSetTargetUp(double height){
        return new Initial(() -> motorUp.setPosition(height/ CM_PER_TICK()));
    }

    public Initial initialSetTargetDown(double height){
        return new Initial(() -> motorDown.setPosition(height/ CM_PER_TICK()));
    }

    public void stopAndResetMode() {
        motorUp.stopAndReset();
        motorDown.stopAndReset();
    }

    public boolean hasReachedTargetUp() {
        return motorUp.hasReachedPosition();
    }

    public boolean hasReachedTargetDown() {
        return motorDown.hasReachedPosition();
    }

    public Main mainDown(double power){
        return new Main(() -> moveDown(power));
    }
    public Main main(double power){
        return new Main(() -> move(power));
    }

    public Exit exitReachedTargetUp(){return new Exit(this::hasReachedTargetUp);}
    public Exit exitReachedTargetDown(){return new Exit(this::hasReachedTargetDown);}

    public Stop stop(){return new Stop(() -> move(0));}

    public Stop stopEncoder(){return new Stop(this::stopAndResetMode);}
}

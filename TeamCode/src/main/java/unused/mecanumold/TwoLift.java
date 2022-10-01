package unused.mecanumold;


import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stop;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;

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
        return new Initial(() -> motorUp.setTarget(height/ CM_PER_TICK()));
    }

    public Initial initialSetTargetDown(double height){
        return new Initial(() -> motorDown.setTarget(height/ CM_PER_TICK()));
    }

    public void stopAndResetMode() {
        motorUp.stopTarget();
        motorDown.stopTarget();
    }

    public boolean hasReachedTargetUp() {
        return motorUp.exitTarget();
    }

    public boolean hasReachedTargetDown() {
        return motorDown.exitTarget();
    }

    public Main mainDown(double power){
        return new Main(() -> moveDown(power));
    }
    public Main main(double power){
        return new Main(() -> move(power));
    }

    public Exit exitReachedTargetUp(){return new Exit(this::hasReachedTargetUp);}
    public Exit exitReachedTargetDown(){return new Exit(this::hasReachedTargetDown);}

    public Stop stopEncoder(){return new Stop(this::stopAndResetMode);}
}

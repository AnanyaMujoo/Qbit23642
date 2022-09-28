package robotparts.unused.tankold;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import elements.FieldSide;
import global.Constants;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;
import util.User;
import util.condition.Expectation;
import util.condition.Magnitude;

import static global.General.bot;
import static global.General.fault;
import static global.General.fieldSide;
import static global.General.mainUser;

public class TankTurret extends RobotPart {
    /**
     * Turret positional motor
     */
    private PMotor tr;

    private double targetBlueTele = Constants.BLUE_TURRET_TARGET_TELE_1;
    private double targetRedTele = Constants.RED_TURRET_TARGET_TELE_1;

    public int freightPlaced = 0;

    /**
     * Create the turret motor and reset it (done internally)
     */
    @Override
    public void init() {
        tr = create("tr", ElectronicType.PMOTOR_FORWARD);
    }

    /**
     * Move the motor at a power
     * @param power
     */
    public void move(double power) {
        tr.setPower(power);
    }

    /**
     * Get the turret position
     * @return position
     */
    public double getTurretPos(){
        return tr.getPosition();
    }

    /**
     * Reset the turret encoder (motor encoder)
     */
    public void resetEncoder(){tr.resetPosition();}

    /**
     * Set the target angle for the turret
     * @param angle
     */
    public void setTarget2(double angle){
        tr.setPosition(angle*Constants.TURRET_ANGLE_DEG_TO_TICKS);
    }

    /**
     * Has the turret reached the target position
     * @return hasReached
     */
    public boolean hasReachedTarget(){
        return tr.hasReachedPosition();
    }

    /**
     * Get the target position based on the field side
     * @return target pos
     */
    // TOD4 TEST VARINI
    // Test this
    public double getTargetPos(){
        if(fieldSide != null) {
            if(mainUser.equals(User.TELE)) {
                if (fieldSide.equals(FieldSide.BLUE)) {
                    return targetBlueTele;
                } else if (fieldSide.equals(FieldSide.RED)) {
                    return targetRedTele;
                }
            }else if(mainUser.equals(User.AUTO)){
                if (fieldSide.equals(FieldSide.BLUE)) {
                    return Constants.BLUE_TURRET_TARGET_AUTO;
                } else if (fieldSide.equals(FieldSide.RED)) {
                    return Constants.RED_TURRET_TARGET_AUTO;
                }
            }
        }else{
            fault.warn("Fieldside was not defined so target pos is not defined", Expectation.SURPRISING, Magnitude.MAJOR);
        }
        return 0;
    }

    public void swapTargetsTeleIfReady() {
        freightPlaced++;
        if (freightPlaced == 1) {
            swapTargetsTele();
        }
    }

    public void swapTargetsTele() {
        if (targetBlueTele == Constants.BLUE_TURRET_TARGET_TELE_1) {
            targetBlueTele = Constants.BLUE_TURRET_TARGET_TELE_2;
        } else {
            targetBlueTele = Constants.BLUE_TURRET_TARGET_TELE_1;
        }
        if (targetRedTele == Constants.RED_TURRET_TARGET_TELE_1) {
            targetRedTele = Constants.RED_TURRET_TARGET_TELE_2;
        } else {
            targetRedTele = Constants.RED_TURRET_TARGET_TELE_1;
        }
    }

    /**
     * Stop and reset the mode
     */
    public void stopAndResetMode() {
        tr.stopAndReset();
    }

    /**
     * Automobile methods
     */
    public Initial initialSetTarget(double angle){return new Initial(() -> setTarget(angle));}
    public Initial initialSetFieldSideTarget(){return new Initial(() -> setTarget(getTargetPos()));}

    public Main main(double power){return new Main(()-> move(power));}
    public Exit exitReachedTarget(){return new Exit(this::hasReachedTarget);}

    public Stop swapTurretTargetsTele() { return new Stop(this::swapTargetsTeleIfReady); }

    public Stop stopEncoder(){return new Stop(this::stopAndResetMode);}

    public Stage turretEncoder(double power, double angle){return new Stage(
            usePart(),
            initialSetTarget(angle),
            main(power),
            exitReachedTarget(),
            stopEncoder(),
            returnPart()
    );}

    public Stage turretEncoderTarget(double power){return new Stage(
            usePart(),
            initialSetFieldSideTarget(),
            main(power),
            exitReachedTarget(),
            stopEncoder(),
            swapTurretTargetsTele(),
            returnPart()
    );}
}

package robotparts.hardware;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import global.Constants;
import global.Modes;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.positional.PMotor;
import util.codeseg.ReturnCodeSeg;
import util.template.Precision;

import static global.General.bot;
import static global.Modes.Height.GROUND;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;
import static global.Modes.Height.MIDDLE;
import static global.Modes.OuttakeStatus.PLACING;
import static global.Modes.heightMode;
import static global.Modes.outtakeStatus;

public class Lift extends RobotPart {

    public PMotor motorRight;
    public PMotor motorLeft;

    public static final double maxPosition = 61;
    public final double defaultCutoffPosition = 6;
    public volatile double currentCutoffPosition = defaultCutoffPosition;
    public int stackedMode = 0;
    public boolean circuitMode = false;
    public boolean ground = false;
    public boolean stacked = false;
    public boolean upright = false;
    public boolean skipping = false;
    public boolean cap = false;
    public int adjust = 0;
    public boolean adjusting = false;
    public double globalOffset = 0;

    @Override
    public void init() {
        motorRight = create("lil", ElectronicType.PMOTOR_FORWARD);
        motorLeft = create("lir", ElectronicType.PMOTOR_REVERSE);
        // 0.25
        motorRight.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.79, 0.66, 5);
        motorLeft.setToLinear(Constants.ORBITAL_TICKS_PER_REV, 1.79, 0.66, 5);
        motorRight.usePositionHolder(0.2, 0.1);
        motorLeft.usePositionHolder(0.2, 0.1);
        heightMode.set(Modes.Height.HIGH);
        circuitMode = false;
        stacked = false;
        ground = false;
        upright = false;
        skipping = false;
        cap = false;
        adjusting = false;
        adjust = 0;
        stackedMode = 0;
        globalOffset = 0;
    }

    public void setGround(boolean ground){ this.ground = ground; }

    @Override
    public void move(double p) {
        motorRight.moveWithPositionHolder(p, currentCutoffPosition, 0.2);
        motorLeft.moveWithPositionHolder(p, currentCutoffPosition, 0.2);
    }

    public void adjustHolderTarget(double delta){
        if(outtakeStatus.modeIs(PLACING) && !heightMode.modeIs(GROUND)) {
            globalOffset += delta;
        }
        currentCutoffPosition = -10;
        motorRight.holdPositionExact();
        motorLeft.holdPositionExact();
        double target = Precision.clip(motorRight.getPositionHolder().getTarget()+delta, 0, maxPosition);
        motorRight.setPositionHolderTarget(target);
        motorLeft.setPositionHolderTarget(target);
    }

    @Override
    public Stage moveTime(double p, double t) {
        return super.moveTime(p, t).combine(new Initial(() -> currentCutoffPosition = p > 0 ? 0 : defaultCutoffPosition));
    }

    @Override
    public Stage moveTime(double p, ReturnCodeSeg<Double> t) { return super.moveTime(p, t); }

    public Stage moveTimeBack(double fp, double p, ReturnCodeSeg<Double> t){
        final Double[] val = {0.0};
        return new Stage(drive.usePart(), this.usePart(), new Initial(() -> val[0] = t.run()),
        new Main(() -> {drive.move(fp, 0,0); move(p);}),
        new Exit(() -> { synchronized (val){ return bot.rfsHandler.getTimer().seconds() > val[0]; }}), this.stop(), drive.stop(), this.returnPart(), drive.returnPart());
    }

    public Stage moveTimeBackOverride(double fp, double p, double t){
        final Double[] val = {0.0};
        return new Stage(drive.usePart(), this.usePart(), new Initial(() -> val[0] = t),
                new Main(() -> {drive.move(fp, 0,0); motorLeft.setPowerRaw(p); motorRight.setPowerRaw(p);}),
                new Exit(() -> { synchronized (val){ return bot.rfsHandler.getTimer().seconds() > val[0]; }}), stop(), drive.stop(), this.returnPart(), drive.returnPart());
    }

    public Stage stageLift(double power, double target) {
        return moveTarget(() -> motorRight, () -> motorLeft, power, power, () -> {
        if(target == heightMode.getValue(LOW)+2 || target == heightMode.getValue(MIDDLE)+2 || target == heightMode.getValue(HIGH)+2){
            return target+globalOffset;
        }else{
            return target;
        }
    }).combine(new Initial(() -> currentCutoffPosition = target < 1 ? defaultCutoffPosition : 0)); }

    @Override
    public void maintain() { super.maintain(); }

    public void reset(){ motorRight.softReset(); motorLeft.softReset(); }

    public Stage resetLift(){ return new Stage(usePart(), new Main(this::reset), exitTime(0.1), stop(), returnPart()); }
}


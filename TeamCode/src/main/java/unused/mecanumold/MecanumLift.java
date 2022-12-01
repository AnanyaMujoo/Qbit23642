package unused.mecanumold;

import automodules.stage.Stage;
import autoutil.controllers.control1D.PositionHolder;
import elements.Level;
import global.Constants;

@Deprecated
public class MecanumLift extends TwoLift {

    public PositionHolder positionHolder;
    private final double UP_SCALE_CONST = 0.96;
    private Level levelMode = Level.TOP;

    @Override
    public void move(double p){
        if(p > 0){
            motorUp.move(p + getRestPows()[0]);
            motorDown.move(0);
        }else if (p < 0){
            motorUp.move(0);
            motorDown.move(p + getRestPows()[1]);
        }else{
            motorUp.move(getRestPows()[0]);
            motorDown.move(getRestPows()[1]);
        }
    }

    // TOD4 FIX
    // Why do we need this?

    @Override
    public void moveDown(double p){
        if(p > 0){
            motorUp.move(p + getRestPows()[0]);
            motorDown.move(0);
        }else if (p < 0){
            motorUp.move(-0.005);
            motorDown.move(p + getRestPows()[1]);
        }else{
            motorUp.move(getRestPows()[0]);
            motorDown.move(getRestPows()[1]);
        }
    }


    @Override
    public void init() {
        super.init();

//        positionHolder = new PositionHolder(0.0, 0.007,0.003, 0.1);
        positionHolder.setProcessVariable(this::getPositionUp);


        // TOD4 FIX
        // Problem with stall detection
//        motorUp.useStallDetector(0.2, getRestPows()[0], 200,0.03, 2);
//        motorDown.useStallDetector(0.2, getRestPows()[1],200,0.03, 2);
    }

    @Override
    protected double[] getRestPows() {
        return new double[]{0.12, -0.05};
    }

    @Override
    protected double getAngle() {
        return Math.PI/4;
    }

    @Override
    public double CM_PER_TICK() {
        return 1 / Constants.LIFT_CM_TO_TICKS;
    }

    public Stage liftTime(double power, double time){return new Stage(
            usePart(),
            main(power),
            exitTime(time),
            stop(),
            returnPart()
    );}


    public Stage liftPow(double power){return new Stage(
            usePart(),
            main(power),
            exitAlways(),
            returnPart()
    );}
    /**
     * Lift to a certain position
     * @param power
     * @param height
     * @return
     */
    public Stage liftEncoderUp(double power, double height){return new Stage(
            usePart(),
            initialSetTargetUp(height*UP_SCALE_CONST),
            main(power),
            exitReachedTargetUp(),
            stopEncoder(),
            returnPart()
    );}


    public Stage liftEncoderDown(double power, double height){return new Stage(
            usePart(),
            initialSetTargetDown(height),
            mainDown(power),
            exitReachedTargetDown(),
            stopEncoder(),
            returnPart()
    );}

    public double getPositionUp(){
        return motorUp.getPosition()*CM_PER_TICK();
    }

    public double getPositionDown(){
        return motorDown.getPosition()*CM_PER_TICK();
    }


    public void holdPosition(){
        if(getPositionUp() > 10) {
            if (motorUp.isAllowed() && motorDown.isAllowed()) {
                positionHolder.update();
                motorUp.move(positionHolder.getOutput() + getRestPows()[0]);
                motorDown.move(getRestPows()[1]);
            }
        }else{
            motorUp.move(0.08);
            motorDown.move(-0.1);
        }
//        motorUp.setPower(getRestPows()[0]);
    }


    public void cycleLevelUp(){
        levelMode = nextLevel();
    }

    public void cycleLevelDown(){
        cycleLevelUp();
        cycleLevelUp();
    }

    public Level getLevelMode(){
        return levelMode;
    }

    public void setLevelMode(Level level){
        levelMode = level;
    }

    private Level nextLevel(){
        switch (levelMode){
            case TOP:
                return Level.BOTTOM;
            case MIDDLE:
                return Level.TOP;
            case BOTTOM:
                return Level.MIDDLE;
        }
        return null;
    }
}

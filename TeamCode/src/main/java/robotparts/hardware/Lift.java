package robotparts.hardware;

import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import global.Constants;
import robotparts.electronics.positional.PMotor;
import util.codeseg.CodeSeg;
import util.codeseg.ReturnParameterCodeSeg;

import static global.General.*;

public abstract class Lift extends PMotorRobotPart {

    @Override
    public double getOverallTarget(double in) {
        return in/Math.sin(getAngle());
    }

    @Override
    public double CM_PER_TICK() {
        return 1 / Constants.LIFT_CM_TO_TICKS;
    }

    /**
     * Set the power of the lift in a main
     * @param power
     * @return
     */
    public Main main(double power){
        return new Main(() -> move(power));
    }

//    /**
//     * Exit when the lift is down
//     * NOTE: Uses the touch sensor
//     * @return
//     */
//    public Exit exitDown(){return new Exit(() -> bot.touchSensors.isOuttakePressingTouchSensor());}

    /**
     * Lift for a certain time
     * @param power
     * @param time
     * @return
     */
    public Stage liftTime(double power, double time){return new Stage(
            usePart(),
            main(power),
            exitTime(time),
            stop(),
            returnPart()
    );}

    /**
     * Lift to a certain position
     * @param power
     * @param height
     * @return
     */
    public Stage liftEncoder(double power, double height){return new Stage(
            usePart(),
            initialSetTarget(height),
            main(power),
            exitReachedTarget(),
            stopEncoder(),
            returnPart()
    );}


    public Stage liftEncoderCustom(double power, double height, CodeSeg custom){return new Stage(
            usePart(),
            initialSetTarget(height),
            new Initial(custom),
            main(power),
            exitReachedTarget(),
            stopEncoder(),
            returnPart()
    );}

}

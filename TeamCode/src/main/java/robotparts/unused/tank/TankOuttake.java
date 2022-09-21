package robotparts.unused.tank;

import com.qualcomm.robotcore.hardware.Servo;

import automodules.stage.Main;
import automodules.stage.Stage;
import elements.GameElement;
import robotparts.RobotPart;
import robotparts.electronics.positional.PServo;

import static global.General.bot;

public class TankOuttake extends RobotPart {
    /**
     * Positional servo for outtake
     */
    private PServo lo;

    /**
     * Initialize the servo and common positions
     */
    @Override
    public void init(){
        lo = createPServo("lo", Servo.Direction.FORWARD, 0.25, 0.5);
        lo.addPosition("cubeLock", 0.0);
        lo.addPosition("ballLock", 0.25);
        lo.addPosition("aligned", 0.35);
        lo.addPosition("start", 0.41);
        lo.addPosition("open", 1.0);
    }

    /**
     * Move to a certain predefined position
     * @param s
     */
    public void move(String s){
        lo.setPosition(s);
    }

    /**
     * Methods to move to a specific position
     */
    public void lockCube(){ move("cubeLock"); }
    public void lockBall(){ move("ballLock"); }
    public void align(){ move("aligned"); }
    public void start(){ move("start"); }
    public void open(){ move("open"); }

    /**
     * Mains to move the outtake
     * @return
     */
    public Main mainLockIfBall(){return new Main(() -> {if (bot.color.isBall()) {lockBall();}});}
    public Main mainLockIfCube(){return new Main(() -> {if (bot.color.isCube()) {lockCube();}});}

    public Main mainDrop(){return new Main(this::open);}
    public Main mainReset(){return new Main(this::start);}

    /**
     * Lock the outtake based on the type of freight
     * @param freightType
     * @param time
     * @return
     */
    public Stage outtakeLock(GameElement freightType, double time){
        Main lock = null;
        if(freightType.equals(GameElement.CUBE)){lock = mainLockIfCube();}
        else if(freightType.equals(GameElement.BALL)){lock = mainLockIfBall();}
        return new Stage(
                usePart(),
                lock,
                exitTime(time),
                returnPart()
        );
    }

    /**
     * Lock the outtake based on the type of freight from the color sensor
     * @param time
     * @return
     */
    public Stage outtakeLock(double time){ return new Stage(
                usePart(),
                mainLockIfBall(),
                mainLockIfCube(),
                exitTime(time),
                returnPart()
        );
    }

    public Stage outtakeLockAndIntake(double time){ return new Stage(
            usePart(),
            bot.tankIntake.usePart(),
            bot.tankIntake.main(1),
            mainLockIfBall(),
            mainLockIfCube(),
            exitTime(time),
            bot.tankIntake.stop(),
            bot.tankIntake.returnPart(),
            returnPart()
        );
    }

    /**
     * Drop the freight
     * @param time
     * @return
     */
    public Stage outtakeDrop(double time){ return new Stage(
            usePart(),
            mainDrop(),
            exitTime(time),
            returnPart()
    );}

    /**
     * Reset the outtake
     * @param time
     * @return
     */
    public Stage outtakeReset(double time){ return new Stage(
            usePart(),
            mainReset(),
            exitTime(time),
            returnPart()
    );}
}

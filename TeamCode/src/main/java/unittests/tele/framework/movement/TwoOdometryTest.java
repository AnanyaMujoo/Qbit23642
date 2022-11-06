package unittests.tele.framework.movement;

import automodules.AutoModule;
import automodules.stage.Exit;
import automodules.stage.Initial;
import automodules.stage.Main;
import automodules.stage.Stage;
import automodules.stage.Stop;
import geometry.framework.Point;
import geometry.position.Pose;
import geometry.position.Vector;
import robotparts.RobotPart;
import teleutil.button.Button;
import teleutil.button.OnTurnOffEventHandler;
import teleutil.button.OnTurnOnEventHandler;
import unittests.tele.TeleUnitTest;

import static global.General.bot;
import static global.General.gph1;
import static global.General.log;

public class TwoOdometryTest extends TeleUnitTest {

    private boolean customMove = false;

    private boolean inAutoModule = false;

    private volatile Pose powerA = new Pose();

    @Override
    protected void start() {
        gph1.link(Button.RIGHT_BUMPER, moveHeading(180));
        gph1.link(Button.LEFT_BUMPER, moveHeading(0));
        gph1.link(Button.RIGHT_TRIGGER, moveHeading(-180));
        gph1.link(Button.LEFT_TRIGGER, testModule());
        gph1.link(Button.B, () -> bot.cancelAutoModules());
        gph1.link(Button.Y, OnTurnOnEventHandler.class, () -> customMove = true);
        gph1.link(Button.Y, OnTurnOffEventHandler.class, () -> {
            odometry.reset(); customMove = false;});
    }

    @Override
    protected void loop() {
        Pose power = movePower(new Pose(new Point(), 0));
        if(!inAutoModule) {
            if (customMove) {
                drive.move(gph1.ry / 2.0, gph1.rx / 2.0, gph1.lx / 2.0);
            } else {
                drive.move(power.getY() + gph1.ry / 2.0, power.getX() + gph1.rx / 2.0, gph1.lx);
            }
        }else{
            drive.move(powerA.getY(), powerA.getX(), powerA.getAngle());
        }
        log.show("Odometry Pose", odometry);
    }

    private AutoModule testModule(){
        return new AutoModule(new Stage(
                usePart(),
                new Main(() -> {
                    Pose test = movePower(new Pose(new Point(), 180));
                    powerA = new Pose(0,0,-1.0);
                }),
                new Exit(() -> Math.abs(odometry.getHeading()-180) < 2),
                returnPart()
        ));
    }

    private AutoModule moveHeading(double target){
        return new AutoModule(new Stage(
                usePart(),
                new Main(() -> {
                    powerA = movePower(new Pose(new Point(), target));
                }),
                new Exit(() -> Math.abs(odometry.getHeading()-target) < 2),
               returnPart()
        ));
    }

    private Pose movePower(Pose target){
        double xPow = 0;
        double yPow = 0;
        double hPow = 0;
        double head = odometry.getHeading();
        if(inAutoModule) {
            hPow = getPower(head - target.getAngle(), 0.008, 0.06); //0.008, 0.06
        }else{
            Pose error = target.getAdded(odometry.getPose().getInverted());
            xPow = getPower(error.getX(), 0.05, 0.02);
            yPow = getPower(error.getY(), 0.05, 0.02);
            hPow = getPower(-error.getAngle(), 0.008, 0.06); //0.008, 0.06
        }
        Vector powerVector = new Vector(xPow, yPow).getRotated(-head).getScaled(1);

        return new Pose(powerVector, hPow);
    }

    private double getPower(double error, double k, double rp){
        return error*k + Math.signum(error)*rp;
    }


    private Initial usePart(){ return new Initial(() -> inAutoModule = true); }
    private Stop returnPart(){ return new Stop(() -> inAutoModule = false); }
}

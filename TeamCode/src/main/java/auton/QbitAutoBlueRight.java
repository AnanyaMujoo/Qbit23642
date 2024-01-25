package auton;

import static automodules.StageBuilder.pause;
import static global.General.bot;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.vision.CaseScannerRect;
import elements.TeamProp;
import math.polynomial.Linear;
import util.Timer;
import util.iter.FinalDouble;

@Autonomous(name = "QbitAutoBlueRight", group = "Autonomous")
public class QbitAutoBlueRight extends Auto {
    public CaseScannerRect caseScanner;
    protected TeamProp propCaseDetected = TeamProp.LEFT;


    @Override
    public final void initAuto() {
        log.show("yeet");
        scan(true,"blue","left");

        while (!isStarted() && !isStopRequested()){ propCaseDetected = caseScanner.getCase(); caseScanner.log(); log.showTelemetry(); }
        camera.halt();
        log.show(propCaseDetected);

        gyro.reset();
    }

    @Override
    public void runAuto() {
        pause(1);





        if (propCaseDetected.equals(TeamProp.LEFT)) {
            //bot.addAutoModule(DropPurpleL(1));
//            moveTurnGyro(0.2, 90);
//            moveTurnGyro(0.2, -90);
            outtake.moveClawClose();
            moveTurnGyroMoveSmooth(-0.55, 1.1, 0.3, -70);
            pause(0.5);
            moveTurnGyroMoveSmooth(0.45, 1.1, 0.4, 0);
            pause(0.5);
            moveForward(0.4,0.34);
            drive.halt();
            moveTurnGyro(0.4,-90);
            drive.halt();
            moveForward(-0.6,3);
            drive.halt();
            bot.addAutoModule(AutoYellow());
            AutoYellow();
            pause(0.5);
            moveTime(0,-0.4, 0.0,1.05);
            pause(0.5);
            moveForward(-0.4,0.7);
            pause(0.5);
            outtake.moveClawOpen();
            drive.halt();



            //moveForward(-0.6, 0.0, -0.28, 0.76)

//            log.show("left");
//            DropPurpleL(0);
        }
        else if (propCaseDetected.equals(TeamProp.CENTER)){
            outtake.moveClawClose();
            moveTime(-0.3, 0.1, 0, 0.94*2);
            pause(1);
            moveTime(0.53/2, -0.1, 0, 0.80*2);
            moveTurnGyro(0.4,-90);
            drive.halt();
            moveForward(-0.6/2,3*2);
            drive.halt();
            bot.addAutoModule(AutoYellow());
            AutoYellow();
            pause(0.5);
            moveTime(0,-0.4, 0.0,1.45);
            pause(0.5);
            moveForward(-0.4/2,0.7*2);
            pause(0.5);
            outtake.moveClawOpen();
            drive.halt();
            log.show("right");
            DropPurpleR(0);
        }
        else if(propCaseDetected.equals(TeamProp.RIGHT)){
            outtake.moveClawClose();
            drive.moveTime(-0.6, 0.1, 0, 0.94);
            pause(1);
            drive.moveTime(0.53, -0.1, 0, 0.8);
            moveTurnGyro(0.4,-90);
            drive.halt();
            moveForward(-0.6,3);
            drive.halt();
            bot.addAutoModule(AutoYellow());
            AutoYellow();
            pause(0.5);
            moveTime(0,-0.4, 0.0,1.05);
            pause(0.5);
            moveForward(-0.4,0.7);
            pause(0.5);
            outtake.moveClawOpen();
            drive.halt();
            log.show("right");
            DropPurpleR(0);
        }

        pause(10);

    }

    public void moveTime(double f, double s, double t, double time) {
        drive.move(f, s, t);
        pause(time);
        drive.halt();
    }
    public void moveForward(double forwardPower, double time) {
        drive.move(forwardPower, 0, 0);
        pause(time);
        drive.halt();
    }
    public void scan(boolean view, String color, String side){
        caseScanner = new CaseScannerRect();
        camera.start(true);
        camera.setScanner(caseScanner);
        caseScanner.setColor(color);
        caseScanner.setSide(side);
        caseScanner.start();
    }

    // https://www.desmos.com/calculator/gfycx0gxgw
    // Initial Power is p
    // Minimum Power is m
    // Turning Distance (Degrees) is d
    // X axis is the current error
    // Y axis is the current turning power to output
    public void moveTurnGyro(double initialPower, double target){
        double start = gyro.getHeading();
        final double minPower = 0.1;
        whileActive(() -> Math.abs(target - gyro.getHeading()) > 2,() -> {
            double error = target - gyro.getHeading();
            Linear linear = new Linear(minPower, Math.abs(initialPower), Math.abs(start-target));
            drive.move(0,0,linear.fodd(error));
        });
        drive.halt();
    }



    public void moveTurnGyroMoveSmooth(double forwardPower, double forwardTime, double initialTurningPower, double targetDegrees){
        double start = gyro.getHeading();
        final double minimumTurningPower = 0.1;
        Timer timer = new Timer();
        timer.reset();
        Linear linearTurnTarget = new Linear(start, targetDegrees, forwardTime);
        Linear linearTurnPower = new Linear(minimumTurningPower, initialTurningPower, Math.abs(start-targetDegrees));
        whileActive(() -> Math.abs(targetDegrees - gyro.getHeading()) > 2 || timer.seconds() < forwardTime,() -> {
            double currentTarget = linearTurnTarget.f(timer.seconds());
            double error = currentTarget - gyro.getHeading();
            if(timer.seconds() > forwardTime){
                drive.move(0,0, linearTurnPower.fodd(error));
            }else{
                drive.move(forwardPower,0, linearTurnPower.fodd(error));
            }
        });
        drive.halt();
    }

}


package auton;

import static global.General.bot;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.vision.CaseScannerRect;
import elements.TeamProp;
import math.polynomial.Linear;

@Autonomous(name = "QbitAutoRedRight", group = "Autonomous")
public class QbitAutoRedRight extends Auto {
    public CaseScannerRect caseScanner;
    protected TeamProp propCaseDetected = TeamProp.LEFT;


    @Override
    public final void initAuto() {
            log.show("yeet");
            scan(true,"red","left");

            while (!isStarted() && !isStopRequested()){ propCaseDetected = caseScanner.getCase(); caseScanner.log(); log.showTelemetry(); }
            camera.halt();
        log.show(propCaseDetected);


    }

    @Override
    public void runAuto() {
        pause(1);






        if (propCaseDetected.equals(TeamProp.LEFT)) {
            bot.addAutoModule(DropPurpleL(1));
            log.show("left");
           DropPurpleL(0);
        }
        else if (propCaseDetected.equals(TeamProp.CENTER)){
            bot.addAutoModule(DropPurpleC(1));
            log.show("center");
            DropPurpleC(0);
        }
        else if(propCaseDetected.equals(TeamProp.RIGHT)){
            bot.addAutoModule(DropPurpleR(1));
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
            Linear linear = new Linear(minPower, initialPower, Math.abs(start-target));
            drive.move(0,0,linear.fodd(error));
        });
        drive.halt();
    }

}


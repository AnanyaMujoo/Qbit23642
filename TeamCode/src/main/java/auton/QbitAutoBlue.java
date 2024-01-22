package auton;

import static global.General.bot;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.vision.CaseScannerRect;
import elements.TeamProp;
@Autonomous(name = "QbitAutoBlue", group = "Autonomous")
public class QbitAutoBlue extends Auto {
    public CaseScannerRect caseScanner;
    protected TeamProp propCaseDetected = TeamProp.LEFT;


    @Override
    public final void initAuto() {
        log.show("yeet");
        scan(true,"blue","left");
        while (!isStarted() && !isStopRequested()){ propCaseDetected = caseScanner.getCase(); caseScanner.log(); log.showTelemetry(); }
        camera.halt();

    }


    @Override
    public void runAuto() {
        outtake.moveClawClose();
        pause(3);


        bot.addAutoModule(DropPurpleL(1));
        bot.addAutoModule(DropPurpleC(1));
        bot.addAutoModule(DropPurpleR(1));

        if (propCaseDetected.equals(TeamProp.LEFT)) {
            DropPurpleL(-1);
        }
        else if (propCaseDetected.equals(TeamProp.CENTER)){
            DropPurpleC(-1);
        }
        else if(propCaseDetected.equals(TeamProp.RIGHT)){
            DropPurpleR(-1);
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

}


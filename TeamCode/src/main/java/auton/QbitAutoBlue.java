package auton;

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
        //  moveTime(0, 0, 0, 0);
        //  bot.addAutoModule(DropPurple(10));
        //  lift.DropPurple(10);
        // pause(10);
        //   if (propCaseDetected.equals(TeamProp.FIRST)) {
        // moveTime(0.5,0,0,0);
        //   }

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


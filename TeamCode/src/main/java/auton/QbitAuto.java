package auton;

import static global.General.bot;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import autoutil.vision.CaseScannerRect;
import elements.TeamProp;
@Autonomous(name = "QbitAuto", group = "Autonomous")
public class QbitAuto extends Auto {
    public CaseScannerRect caseScanner;
    protected TeamProp propCaseDetected = TeamProp.FIRST;


    @Override
    public final void initAuto() {
            log.show("yeet");
            scan(true,"red","left");
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


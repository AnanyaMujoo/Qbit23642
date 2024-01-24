package auton;

import static global.General.bot;
import static global.General.log;
import static global.Modes.Height.GROUND;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import autoutil.AutoFramework;
import static global.General.bot;
import static global.General.log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.vision.CaseScannerRect;
import elements.TeamProp;
import global.General;

@Autonomous(name = "Red odomtery auto", group = "Autonomous")
public class OdoAutoRed extends AutoFramework {
    protected TeamProp propCaseDetected = TeamProp.LEFT;

    public void initialize() {
        log.show("yeet");
        AutoFramework auto = this;


        auto.scanRect(true, "red", "left");
        while (!isStarted() && !isStopRequested()) {
            propCaseDetected = caseScannerRect.getCase();
            caseScannerRect.log();
            log.showTelemetry();
        }
        camera.halt();
        log.show(propCaseDetected);
    }

    @Override
    public void define() {
        // customCase(() -> {
        if (propCaseDetected == TeamProp.CENTER) {
            addPause(1);
            log.show("center");
            //addTimedSetpoint(1.0, .5, 1, -6, 31, 0);

        }
        if (propCaseDetected == TeamProp.LEFT) {
            //addTimedSetpoint(1.0, .5, 1, 11.2, -27.2, -90);
            addPause(1);
            log.show("left");
            //addTimedSetpoint(1.0, .5, 1, -11.2, 27.2, 90);

        }
        if (propCaseDetected == TeamProp.RIGHT) {
            //addTimedSetpoint(1.0, .5, 1, -5, -27.2, 0);
            addPause(1);
            log.show("right");
            //addTimedSetpoint(1.0, .5, 1, 5, 27.2, 0);

        }
    }
}
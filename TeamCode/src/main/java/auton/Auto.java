package auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import automodules.AutoModuleUser;
import elements.FieldPlacement;
import elements.FieldSide;
import global.Common;
import global.Modes;
import robot.RobotUser;
import util.template.Iterator;
import util.template.Precision;

public abstract class Auto extends LinearOpMode implements Common, Iterator, RobotUser, AutoModuleUser, Modes {
    /**
     * Base class for autons
     * NOTE: If the methods are overriden then make sure the call super.<method name>
     * this will generate by default if you use @Override
     */

    /**
     * Init method runs when the user clicks the init button to run a auton
     */
    public abstract void initAuto();

    /**
     * Run method runs when the user clicks the start button after init
     */
    public abstract void runAuto();

    /**
     * Stop method runs when the program ends
     */
    public void stopAuto() {}

    /**
     * Internal final auton method
     * NOTE: Do not use or override this
     */
    @Override
    public final void runOpMode() throws InterruptedException {
        reference(this);
        activate();
        initAuto();
        waitForStart();
        ready();
        update(true);
        runAuto();
        stopAuto();
        end();
    }


    @Override
    public boolean condition() {
        return !Precision.runOnCondition(isStopRequested(), this::end);
    }
}

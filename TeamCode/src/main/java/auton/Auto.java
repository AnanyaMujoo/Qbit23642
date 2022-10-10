package auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import automodules.AutoModuleUser;
import elements.FieldSide;
import global.Common;
import robot.RobotUser;
import util.template.Iterator;
import util.template.Precision;

public abstract class Auto extends LinearOpMode implements Common, Iterator, RobotUser, AutoModuleUser {
    /**
     * Base class for autons
     * NOTE: If the methods are overriden then make sure the call super.<method name>
     * this will generate by default if you use @Override
     */

    protected FieldSide fieldSide = FieldSide.UNKNOWN;

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
        activate(FieldSide.UNKNOWN);
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

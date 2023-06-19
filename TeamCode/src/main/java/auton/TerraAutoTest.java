package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import autoutil.AutoFramework;
import elements.Field;
import elements.FieldPlacement;
import elements.FieldSide;
import elements.GameItems;
import geometry.position.Pose;
import robotparts.RobotPart;
import util.ExceptionCatcher;
import util.User;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.General.log;
import static global.Modes.Height.HIGH;
import static global.Modes.Height.LOW;

@Autonomous(name = "TerraAutoTest", group = "auto")
public class TerraAutoTest extends AutoFramework {

    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);
    }

    @Override
    public void define() {

        addTimedSetpoint(0.5, 0.3, 4.0, 0, 90, 0);

        addCustomCode(() -> {
            log.show("Odometry Pose", odometry.getPose());
        }, 10);

    }


    @Override
    public void postProcess() { autoPlane.reflectY(); autoPlane.reflectX(); }

}

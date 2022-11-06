package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import autoutil.AutoFramework;

import static global.General.log;


@Autonomous(name = "TerraAutoTest", group = "auto")
public class TerraAuto extends AutoFramework {
    @Override
    public void initAuto() {
        setConfig(mecanumDefaultConfig);
    }

    @Override
    public void define() {
        addWaypoint(0,20,0);
        addWaypoint(20,20,90);
        addConcurrentAutoModule(DuckNew);
        addWaypoint(20,0,90);
        addCancelAutoModules();
        addSetpoint(20,0,90);
        addSetpoint(0,0,0);
    }
}

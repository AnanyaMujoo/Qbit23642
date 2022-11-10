package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import elements.FieldSide;


@Autonomous(name = "TerraAutoTest", group = "auto")
public class TerraAutoTest extends AutoFramework {
    {
        fieldSide = FieldSide.RED;
    }
    @Override
    public void initAuto() {
        setConfig(mecanumDefaultConfig);
        scan();
    }

    @Override
    public void define() {
        addSetpoint(0,20,90);
        addWaypoint(0,20,0);
        addWaypoint(20,20,90);
        addConcurrentAutoModule(DuckNew);
        addWaypoint(20,0,90);
        addCancelAutoModules();
        addSetpoint(20,0,90);
        addSetpoint(0,0,0);
    }
}

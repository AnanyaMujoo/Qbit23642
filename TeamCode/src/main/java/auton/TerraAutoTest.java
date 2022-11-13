package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import elements.FieldSide;


@Autonomous(name = "TerraAutoTest", group = "auto")
public class TerraAutoTest extends AutoFramework {

    @Override
    public void initAuto() {
        setConfig(mecanumDefaultConfig);
        scan();
    }

    @Override
    public void define() {
//        addWaypoint(15,20,0);
//        addWaypoint(0,40,90);
//        addSetpoint(0,0, 180);
//        addSetpoint(0,0,360);
//        addSetpoint(20,40,90);
    }
}

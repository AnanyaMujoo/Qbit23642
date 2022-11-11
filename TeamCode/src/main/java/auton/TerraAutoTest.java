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

    }
}

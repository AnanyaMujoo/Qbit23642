package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;

@Autonomous(name = "TerraAutoTest", group = "auto")
public class TerraAuto extends AutoFramework {
    @Override
    public void initAuto() {
        setConfig(mecanumDefaultConfig);
    }

    @Override
    public void define() {
        // TODO TEST
        addWaypoint(0, 20, 0);
        addWaypoint(20, 20, 0);
        addSetpoint(20,0,0);
    }
}

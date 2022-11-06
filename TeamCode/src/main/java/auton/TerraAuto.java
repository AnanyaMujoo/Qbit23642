package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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
        // TODO TEST
        addSetpoint(0,20,0);
//        addWaypoint(0,20,0);

//        whileTime(() -> {
//            log.show("Size", segments.size());
//
//        }, 1);
//        addWaypoint(0, 20, 0);
//        addWaypoint(20, 20, 0);
//        addSetpoint(20,0,0);
    }
}

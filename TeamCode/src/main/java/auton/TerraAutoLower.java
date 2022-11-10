package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import elements.FieldSide;
import unused.auto.TerraAutoCycles;

public class TerraAutoLower extends AutoFramework {

    @Override
    public void initAuto() {
        setConfig(mecanumDefaultConfig);
    }

    @Override
    public void define() {
        addWaypoint(0, 80, 0);
        addWaypoint(0, 121, 45);
        addWaypoint(40, 121, 90);
        addSetpoint(60, 121, 90);
        addWaypoint(0, 130, 60);
        addSetpoint(-10, 140, 60);
    }



    @Autonomous(name = "TerraAutoLowerBlue", group = "auto")
    public static class TerraAutoLowerBlue extends TerraAutoLower {{ fieldSide = FieldSide.BLUE; }}
    @Autonomous(name = "TerraAutoLowerRed", group = "auto")
    public static class TerraAutoUpperRed extends TerraAutoLower {{ fieldSide = FieldSide.RED; }}

}

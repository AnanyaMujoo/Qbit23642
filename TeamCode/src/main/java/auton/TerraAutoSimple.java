package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import elements.Case;
import elements.FieldSide;

public class TerraAutoSimple extends AutoFramework {

    @Override
    public void initAuto() {
        setConfig(mecanumDefaultConfig);
    }

    @Override
    public void preProcess() {
        caseDetected = Case.FIRST;
        if(!upper&&isFlipped() || upper&&!isFlipped()){ flipCases();}
    }

    @Override
    public void define() {
        addWaypoint(0, 80, 0);
        addWaypoint(0, 111, 0);
        addWaypoint(0, 130, 35);
        addSetpoint(-7, 140, 55);
        customCase(() -> {
            addWaypoint(-7, 128, 90);
            addWaypoint(-20, 128, 90);
            addWaypoint(-55, 130, 70);
            addWaypoint(-60, 132, 45);
            addSetpoint(-62, 70, 0);
        }, () -> {
            addWaypoint(0, 130, 35);
            addWaypoint(0, 105, 0);
            addSetpoint(0, 70, 0);
        }, () -> {
            addWaypoint(7, 128, 90);
            addWaypoint(20, 128, 90);
            addWaypoint(48, 124, 70);
            addWaypoint(50, 116, 50);
            addWaypoint(56, 95, 0);
            addSetpoint(58, 70, 0);
        });
    }

    @Override
    public void postProcess() { if(upper){ flip(); } }

    protected boolean upper = false;

    @Autonomous(name = "TerraAutoLowerBlueSimple", group = "auto")
    public static class TerraAutoLowerBlueSimple extends TerraAutoSimple {{ fieldSide = FieldSide.BLUE; }}
    @Autonomous(name = "TerraAutoLowerRedSimple", group = "auto")
    public static class TerraAutoLowerRedSimple extends TerraAutoSimple {{ fieldSide = FieldSide.RED; }}
    @Autonomous(name = "TerraAutoUpperBlueSimple", group = "auto")
    public static class TerraAutoUpperBlueSimple extends TerraAutoSimple {{ fieldSide = FieldSide.BLUE; upper = true; }}
    @Autonomous(name = "TerraAutoUpperRedSimple", group = "auto")
    public static class TerraAutoUpperRedSimple extends TerraAutoSimple {{ fieldSide = FieldSide.RED; upper = true; }}

}

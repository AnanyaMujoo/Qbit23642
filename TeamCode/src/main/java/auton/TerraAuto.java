package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import elements.Case;
import elements.FieldSide;

import static global.General.bot;

public class TerraAuto extends AutoFramework {

    @Override
    public void initAuto() {
        setConfig(mecanumDefaultConfig);
        bot.addBackgroundTask(lift.holdPosition());
        scan();
    }

    @Override
    public void preProcess() {
//        caseDetected = Case.FIRST;
        if(!upper&&isFlipped() || upper&&!isFlipped()){ flipCases();}
    }

    @Override
    public void define() {
        addWaypoint(0, 60, 0);
//        addConcurrentAutoModule(Backward);
        addWaypoint(0, 100, 35);
        addSetpoint(-1, 134, 50);
        addConcurrentAutoModule(Forward);
        customNumber(5, i -> {
            addWaypoint(14, 129, 90);
            addSetpoint(56, 127, 90);
//            addConcurrentAutoModule(Backward);
            addPause(0.5);
            addWaypoint(34, 127, 75);
            addSetpoint(1, 136, 50);
//            addConcurrentAutoModule(Forward);
        });
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
    public void postProcess() {
        autoPlane.reflectY(); autoPlane.reflectX();
        autoPlane.scale(0.99);
        if(upper){ flip(); }
    }

    protected boolean upper = false;

    @Autonomous(name = "TerraAutoLowerBlue", group = "auto")
    public static class TerraAutoLowerBlue extends TerraAuto {{ fieldSide = FieldSide.BLUE; }}
    @Autonomous(name = "TerraAutoLowerRed", group = "auto")
    public static class TerraAutoLowerRed extends TerraAuto {{ fieldSide = FieldSide.RED; }}
    @Autonomous(name = "TerraAutoUpperBlue", group = "auto")
    public static class TerraAutoUpperBlue extends TerraAuto {{ fieldSide = FieldSide.BLUE; upper = true; }}
    @Autonomous(name = "TerraAutoUpperRed", group = "auto")
    public static class TerraAutoUpperRed extends TerraAuto {{ fieldSide = FieldSide.RED; upper = true; }}

}

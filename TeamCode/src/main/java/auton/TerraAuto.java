package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import autoutil.AutoFramework;
import elements.Case;
import elements.FieldPlacement;
import elements.FieldSide;
import util.condition.DecisionList;

import static global.General.bot;

public class TerraAuto extends AutoFramework {

    @Override
    public void initAuto() {
        setConfig(mecanumDefaultConfig);
        bot.addBackgroundTask(lift.holdPosition());
        outtake.closeClaw();
        scan();
    }

    @Override
    public void preProcess() {
//        caseDetected = Case.THIRD;
        if(isFlipped()){ flipCases(); }
    }

    @Override
    public void define() {
        addWaypoint(0, 60, 0);
        addWaypoint(0, 100, 35);
        // TODO TEST
        customSidePlacement(() -> {
            addSetpoint(1, 128, 50);
        }, () -> {
            addSetpoint(-1, 130, 50);
        }, () -> {
            addSetpoint(3, 128, 50);
        }, () -> {
            addSetpoint(-1, 130, 50);
        });
        addConcurrentAutoModule(Backward);
        addPause(3.0);
        addAutoModule(new AutoModule(outtake.stageOpen(0.5)));
        addConcurrentAutoModule(Forward);
        addPause(3.0);
//        customNumber(5, i -> {
//            addWaypoint(14, 124, 90);
//            addSetpoint(56, 122, 90);
////            addConcurrentAutoModule(Backward);
//            addPause(0.5);
//            addWaypoint(34, 122, 75);
//            addSetpoint(-3, 130, 50);
////            addConcurrentAutoModule(Forward);
//        });
        customCase(() -> {
            addWaypoint(-7, 124, 90);
            addWaypoint(-20, 124, 90);
            addWaypoint(-55, 126, 70);
            addWaypoint(-60, 128, 45);
            addSetpoint(-62, 70, 0);
        }, () -> {
            addWaypoint(0, 130, 35);
            addWaypoint(0, 105, 0);
            addSetpoint(0, 80, 0);
        }, () -> {
            addWaypoint(7, 124, 90);
            addWaypoint(20, 124, 90);
            addWaypoint(48, 122, 70);
            addWaypoint(50, 114, 50);
            addWaypoint(56, 95, 0);
            addSetpoint(58, 70, 0);
        });
    }

    @Override
    public void postProcess() { autoPlane.reflectY(); autoPlane.reflectX(); }

    @Autonomous(name = "TerraAutoLowerBlue", group = "auto")
    public static class TerraAutoLowerBlue extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; }}
    @Autonomous(name = "TerraAutoLowerRed", group = "auto")
    public static class TerraAutoLowerRed extends TerraAuto {{ fieldSide = FieldSide.RED; fieldPlacement = FieldPlacement.LOWER;}}
    @Autonomous(name = "TerraAutoUpperBlue", group = "auto")
    public static class TerraAutoUpperBlue extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; }}
    @Autonomous(name = "TerraAutoUpperRed", group = "auto")
    public static class TerraAutoUpperRed extends TerraAuto {{ fieldSide = FieldSide.RED; fieldPlacement = FieldPlacement.UPPER; }}

}

package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import automodules.AutoModule;
import automodules.AutoModuleUser;
import automodules.stage.Main;
import automodules.stage.Stage;
import autoutil.AutoFramework;
import autoutil.reactors.MecanumJunctionReactor;
import autoutil.vision.JunctionScanner;
import elements.Case;
import elements.FieldPlacement;
import elements.FieldSide;
import util.condition.DecisionList;

import static global.General.bot;

public class TerraAuto extends AutoFramework {

    @Override
    public void initAuto() {
        setConfig(mecanumDefaultConfig);
        lift.maintain();
        outtake.closeClaw();
        scan(false);
        setScannerAfterInit(MecanumJunctionReactor.junctionScanner);
    }

    @Override
    public void preProcess() {
//        caseDetected = Case.THIRD;
        if(isFlipped()){ flipCases(); }
    }

    // TODO TEST

    @Override
    public void define() {
        addWaypoint(0, 40, 0);
        addConcurrentAutoModule(BackwardAuto);
        addScaledWaypoint(0.5, 0, 120, 20);
        addScaledSetpoint(1.05, 1.0, 130, 50); // 4.0, 128.0, 50.0
        addAutoModule(DropAuto);
        addConcurrentAutoModule(ForwardAuto(0));
        customNumber(5, i -> {
            addWaypoint(20, 124.5, 90);
            addScaledWaypoint(0.4, 55, 124.5, 90);
            addScaledSetpoint(1.1, 59, 124.5, 90);
            addAutoModule(GrabAuto);
            addConcurrentAutoModule(Backward);
            addWaypoint(34, 124.5, 75);
            addScaledSetpoint(1.05, 1.0, 130, 50);
            addPause(5.0);
            addAutoModule(DropAuto);
            addConcurrentAutoModule(ForwardAuto(i+1));
        });
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


//        addCustomSegment(mecanumJunctionSetpoint, 1.0, 130, 50);
    }

    @Override
    public void postProcess() {
        autoPlane.reflectY(); autoPlane.reflectX();
    }

    @Autonomous(name = "TerraAutoLowerBlue", group = "auto")
    public static class TerraAutoLowerBlue extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.LOWER; }}
    @Autonomous(name = "TerraAutoLowerRed", group = "auto")
    public static class TerraAutoLowerRed extends TerraAuto {{ fieldSide = FieldSide.RED; fieldPlacement = FieldPlacement.LOWER;}}
    @Autonomous(name = "TerraAutoUpperBlue", group = "auto")
    public static class TerraAutoUpperBlue extends TerraAuto {{ fieldSide = FieldSide.BLUE; fieldPlacement = FieldPlacement.UPPER; }}
    @Autonomous(name = "TerraAutoUpperRed", group = "auto")
    public static class TerraAutoUpperRed extends TerraAuto {{ fieldSide = FieldSide.RED; fieldPlacement = FieldPlacement.UPPER; }}

}

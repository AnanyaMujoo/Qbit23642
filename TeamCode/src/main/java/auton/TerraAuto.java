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
//        lift.maintain();
//        outtake.closeClaw();
        scan(false);
        MecanumJunctionReactor.setFlipped(isFlipped());
        setScannerAfterInit(MecanumJunctionReactor.junctionScanner);
        // TODO SAVE GYRO ANGLE
    }

    @Override
    public void preProcess() {
//        caseDetected = Case.THIRD;
        if(isFlipped()){ flipCases(); }
    }

    public void place(int i){
        if(i == 0) {
            addAccuracyScaledSetpoint(2.0, 1.0, 1.0, 133, 50);
            addCustomSegment(mecanumJunctionSetpoint, 1.0, 133, 50);
        }else{
            addScaledSetpoint(1.0, 1.0, 133, 50);
        }
        addAutoModule(DropAuto);
        addConcurrentAutoModule(ForwardAuto(i));
    }

    public void pick(){
        addScaledSetpoint(1.0, 58, 127, 90);
        addAutoModule(GrabAuto);
        addConcurrentAutoModule(BackwardAuto);
    }

    @Override
    public void define() {
        addWaypoint(0, 40, 0);
        addConcurrentAutoModule(BackwardAuto);
        addScaledWaypoint(0.8, 0, 116, 20);
        place(0);
        customNumber(5, i -> {
            addWaypoint(30, 127, 90);
            pick();
            addWaypoint(34, 127, 75);
            place(i+1);
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

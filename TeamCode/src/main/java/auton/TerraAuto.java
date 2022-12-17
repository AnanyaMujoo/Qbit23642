package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

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
import geometry.position.Pose;
import util.Timer;
import util.condition.DecisionList;

import static global.General.bot;
import static global.General.fieldPlacement;
import static global.General.fieldSide;
import static global.Modes.HeightMode.Height.HIGH;

public class TerraAuto extends AutoFramework {

    @Override
    public void initAuto() {
        setConfig(mecanumDefaultConfig);
        bot.savePose(new Pose());
        lift.maintain();
        outtake.readyStart();
        outtake.closeClaw();
        scan(false);
        MecanumJunctionReactor.setFlipped(isFlipped());
        setScannerAfterInit(MecanumJunctionReactor.junctionScanner);
        JunctionScanner.resume();
    }

    @Override
    public void preProcess() {
//        caseDetected = Case.THIRD;
    }

    public void place(int i){
        if(i == 0) {
            addConcurrentAutoModule(BackwardAutoFirst);
            customFlipped(() -> {
                double x = 5.5; double y = 129;
                addAccuracyScaledSetpoint(3.0,1.05,  x, y, 50);
                addCustomSegment(mecanumJunctionSetpoint, x, y, 50);
                addAccuracyScaledSetpoint(1.0, 1.05,x, y, 50);
            }, () -> {
                double x = -1.0; double y = 132;
                addAccuracyScaledSetpoint(3.0,1.2,  -1, 129, 50);
                addCustomSegment(mecanumJunctionSetpoint, x, y, 50);
                addAccuracyScaledSetpoint(1.0, 1.2, x, y, 50);
            });
            addAutoModule(DropAutoFirst);
        }else{
            addConcurrentAutoModule(BackwardAuto);
            customFlipped(() -> {
                addAccuracyScaledSetpoint(1.0, 1.2,4.5-(i/5.0), 130+(i/4.0), 50);
            }, () -> {
                addAccuracyScaledSetpoint(1.0, 1.2,-1.0-(i/4.0), 130-(i/4.0), 50);
            });
            addCancelAutoModules();
            addAutoModule(DropAuto);
        }
        addConcurrentAutoModule(ForwardAuto(i));
    }

    public void pick(int i){
        addBreakpoint(() -> timer.seconds() > 24.5);
        customFlipped(() -> {
            addAccuracyScaledSetpoint(2.0, 0.7, 61-(i/5.0), 125, 87);
        }, () -> {
            addAccuracyScaledSetpoint(2.0, 0.7, 57-(i/4.0), 126, 87);
        });
        addConcurrentAutoModule(GrabAuto);
        addPause(0.5);
    }
    @Override
    public void define() {
        addWaypoint(0, 40, 0);
        customFlipped(() -> {
            addScaledWaypoint(1.0, 0, 128, 0);
        }, () -> {
            addScaledWaypoint(1.0, 0, 125, 0);
        });
        addScaledWaypoint(1.0, 0, 120, 20);
        place(0);
        customNumber(5, i -> {
            customFlipped(() -> {
                addScaledWaypoint(0.4, 30, 125, 87);
                pick(i+1);
                addScaledWaypoint(0.6, 30, 128, 75);
                addScaledWaypoint(0.35, 3, 128.5, 50);
                place(i+1);
            }, () -> {
                addScaledWaypoint(0.5, 30, 125, 87);
                pick(i+1);
                addScaledWaypoint(0.7, 30, 128, 75);
                addScaledWaypoint(0.4, 3, 128.5, 50);
                place(i+1);
            });
        });
        addBreakpointReturn();
        customCase(() -> {
            addWaypoint(-7, 124, 90);
            addScaledWaypoint(0.8, -10, 124, 90);
            addScaledWaypoint(0.8, -45, 122, 60);
            addScaledWaypoint(0.8, -50, 123, 25);
            addScaledSetpoint(0.9, -62, 75, 0);
        }, () -> {
            addWaypoint(0, 130, 35);
            addWaypoint(0, 105, 0);
            addScaledSetpoint(0.9, 0, 75, 0);
        }, () -> {
            addWaypoint(7, 128, 90);
            addScaledWaypoint(0.8, 16, 128, 90);
            addScaledWaypoint(0.8, 39, 122, 58);
            addScaledWaypoint(0.8, 51, 111, 32);
            addScaledWaypoint(0.8, 56, 95, 0);
            addScaledSetpoint(0.9, 58, 75, 0);
        });
    }


    @Override
    public void postProcess() {
        autoPlane.reflectY(); autoPlane.reflectX();
    }

    @Override
    public void stopAuto() {
        bot.savePose(odometry.getPose());
        bot.saveLocationOnField();
        super.stopAuto();
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

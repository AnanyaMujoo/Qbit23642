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
        if(isFlipped()){ flipCases(); }
    }

    public void place(int i){
        if(i == 0) {
            addConcurrentAutoModule(BackwardAutoFirst);
            addAccuracyScaledSetpoint(3.0,1.05,  1.5, 130.5, 51);
            addCustomSegment(mecanumJunctionSetpoint, 1.5, 130.5, 51);
            addAccuracyScaledSetpoint(1.0, 1.05,1.2, 130.2, 51);
            addAutoModule(DropAutoFirst);
        }else{
            addConcurrentAutoModule(BackwardAuto);
            addAccuracyScaledSetpoint(1.0, 0.9, 0.7, 132.8 + (i/3.0), 51);
            addCancelAutoModules();
            addAutoModule(DropAuto);
        }
        addConcurrentAutoModule(ForwardAuto(i));
    }

    public void pick(int i){
        addBreakpoint(() -> timer.seconds() > 25);
        addAccuracyScaledSetpoint(2.0, 0.7, 61, 125+(i/3.0), 90);
        addConcurrentAutoModule(GrabAuto);
        addPause(0.5);
    }
    @Override
    public void define() {
        addWaypoint(0, 40, 0);
        addScaledWaypoint(1.0, 0, 130, 0);
        addScaledWaypoint(1.0, 0, 120, 30);
        place(0);
        customNumber(5, i -> {
            addScaledWaypoint(0.4, 30, 129, 90);
            pick(i);
            addScaledWaypoint(0.6, 30, 129, 75);
            addScaledWaypoint(0.35, 3, 133.5, 50);
            place(i+1);
        });
        addBreakpointReturn();
        customCase(() -> {
            addWaypoint(-7, 124, 90);
            addScaledWaypoint(0.8, -10, 124, 90);
            addScaledWaypoint(0.8, -45, 122, 60);
            addScaledWaypoint(0.8, -50, 123, 25);
            addScaledSetpoint(0.9, -62, 80, 0);
        }, () -> {
            addWaypoint(0, 130, 35);
            addWaypoint(0, 105, 0);
            addScaledSetpoint(0.9, 0, 80, 0);
        }, () -> {
            addWaypoint(7, 128, 90);
            addWaypoint(20, 128, 90);
            addWaypoint(48, 122, 70);
            addWaypoint(50, 114, 50);
            addWaypoint(56, 95, 0);
            addScaledSetpoint(0.9, 58, 80, 0);
        });
    }


    @Override
    public void postProcess() {
        autoPlane.reflectY(); autoPlane.reflectX();
    }

    @Override
    public void stopAuto() {
//        bot.savePose(odometry.getPose());
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


    /**
     *
     public void place(int i){
     if(i == 0) {
     addConcurrentAutoModule(new AutoModule(outtake.stageMiddle(0.0), outtake.stageFlip(0.0), lift.stageLift(0.9, HIGH.getValue()-2)));
     addAccuracyScaledSetpoint(3.0, 1.0, 1.5, 130.5, 56);
     addCustomSegment(mecanumJunctionSetpoint, 1.5, 130.5, 56);
     addAccuracyScaledSetpoint(1.0, 1.1,1.5, 130.5, 56);
     addAutoModule(new AutoModule(outtake.stageEnd(0.3)));
     }else{
     addScaledSetpoint(1.1, 1.5, 133, 53);
     }
     addAutoModule(DropAuto);
     addConcurrentAutoModule(ForwardAuto(i));
     addPause(0.5);
     }

     public void pick(int i){
     addAccuracyScaledSetpoint(2.0, 0.7, 62-(i/3.0), 129, 90);
     addConcurrentAutoModule(GrabAuto);
     addPause(0.5);
     }

     // TODO CREATE SMART SWITCH (IF NOT ENOUGH TIME LEFT SWITCH TO PARK)

     @Override
     public void define() {
     addWaypoint(0, 40, 0);
     addScaledWaypoint(0.8, 0, 116, 20);
     place(0);
     customNumber(5, i -> {
     addScaledWaypoint(0.65, 30, 129, 90);
     pick(i+1);
     addScaledWaypoint(0.6, 14, 129, 75);
     addConcurrentAutoModule(BackwardAuto);
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

     */
}

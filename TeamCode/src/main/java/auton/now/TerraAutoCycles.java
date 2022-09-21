package auton.now;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import auton.MecanumAuto;
import elements.Case;
import elements.FieldSide;

import static global.General.automodules;
import static global.General.bot;

public class TerraAutoCycles extends MecanumAuto {

    @Override
    public void initAuto() {
        scan();
        setBackgroundTasks(() -> {
            lift.holdPosition();
            intake.move(-0.8);
        });
        intake.scale = 0.6;
    }

    @Override
    public void define() {
        outtake.midCap();
        addWaypoint(20, 20, -60);
        customNumber(5, i -> {
            addCancelAutoModules();
            customCase(Case.RIGHT, () -> {
                addConcurrentAutoModule(automodules.AllianceLiftUp(automodules.LiftUpTopFast));
                if(i==0) {
                    addSetpoint(30, 47, -137);
                }else if(i>0){
                    addSetpoint(30+i, 35+i, -140);
                }
            }, Case.CENTER, () -> {
                addConcurrentAutoModule(automodules.AllianceLiftUp(automodules.LiftUpMiddleFast));
                addPause(0.5);
                addSetpoint(37, 54, -137);
            }, Case.LEFT, () -> {
                addConcurrentAutoModule(automodules.AllianceLiftUp(automodules.LiftUpBottomFast));
                addPause(0.5);
                addSetpoint(44, 61, -137);
            });
            addCancelAutoModules();
            addConcurrentAutoModule(automodules.ResetLiftAndOuttake);
            addPause(0.6);
            addWaypoint(45, 25, -115);
            addWaypoint(45, -10, -95);
            addWaypoint(0, -10, -95);
            if(i < 4) {
                addConcurrentAutoModule(automodules.IntakeCombined);
                addWaypoint(-50 - (3*i), -10, -90);
                addPause(0.5);
                addWaypoint(0, -10, -85);
                addWaypoint(35, -10, -85);
                addWaypoint(35, 25, -115);
                addPause(0.1);
            }
            caseDetected = Case.RIGHT;
        });
        addCancelAutoModules();
        addWaypoint(-45, -10, -90);
        addSetpoint(-45, 50, -90);
    }


    @Autonomous(name = "TerraAutoCyclesBlue", group = "auto")
    public static class TerraAutoCyclesBlue extends TerraAutoCycles {{ fieldSide = FieldSide.BLUE; }}
    @Autonomous(name = "TerraAutoCyclesRed", group = "auto")
    public static class TerraAutoCyclesRed extends TerraAutoCyclesBlue {{ fieldSide = FieldSide.RED; }}
}

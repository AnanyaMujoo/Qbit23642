package auton.now;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import auton.MecanumAuto;
import elements.Case;
import elements.FieldSide;

import static global.General.automodules;
import static global.General.bot;

public class TerraAutoCyclesSafe extends MecanumAuto {

    @Override
    public void initAuto() {
        scan();
        setBackgroundTasks(() -> {
            bot.lift.holdPosition();
            bot.intake.move(-0.8);
        });
        bot.intake.scale = 0.6;
    }

    @Override
    public void define() {
        bot.outtake.midCap();
        addWaypoint(20, 20, -60);
        customNumber(1, i -> {
            addCancelAutoModules();
            customCase(Case.RIGHT, () -> {
                addConcurrentAutoModule(automodules.AllianceLiftUp(automodules.LiftUpTopFast));
                addPause(0.5);
                addSetpoint(30, 47, -137);
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
        });
        addCancelAutoModules();
        addWaypoint(-45, -10, -90);
        addSetpoint(-45, 50, -90);
    }


    @Autonomous(name = "TerraAutoCyclesSafeBlue", group = "auto")
    public static class TerraAutoCyclesSafeBlue extends TerraAutoCyclesSafe {{ fieldSide = FieldSide.BLUE; }}
    @Autonomous(name = "TerraAutoCyclesSafeRed", group = "auto")
    public static class TerraAutoCyclesSafeRed extends TerraAutoCyclesSafeBlue {{ fieldSide = FieldSide.RED; }}
}

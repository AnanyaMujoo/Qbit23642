package auton.now;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import auton.MecanumAuto;
import elements.Case;
import elements.FieldSide;


public class TerraAutoCyclesSafe extends MecanumAuto {

    @Override
    public void initAuto() {
        scan();
        setBackgroundTasks(() -> {
            mecanumLift.holdPosition();
            mecanumIntake.move(-0.8);
        });
        mecanumIntake.scale = 0.6;
    }

    @Override
    public void define() {
        mecanumOuttake.midCap();
        addWaypoint(20, 20, -60);
        customNumber(1, i -> {
            addCancelAutoModules();
            customCase(Case.RIGHT, () -> {
                addConcurrentAutoModule(AllianceLiftUp(LiftUpTopFast));
                addPause(0.5);
                addSetpoint(30, 47, -137);
            }, Case.CENTER, () -> {
                addConcurrentAutoModule(AllianceLiftUp(LiftUpMiddleFast));
                addPause(0.5);
                addSetpoint(37, 54, -137);
            }, Case.LEFT, () -> {
                addConcurrentAutoModule(AllianceLiftUp(LiftUpBottomFast));
                addPause(0.5);
                addSetpoint(44, 61, -137);
            });
            addCancelAutoModules();
            addConcurrentAutoModule(ResetLiftAndOuttake);
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

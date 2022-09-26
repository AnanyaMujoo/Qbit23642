package auton.now;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import auton.MecanumAuto;
import elements.Case;
import elements.FieldSide;

import static global.General.*;

public class TerraAutoDuck extends MecanumAuto {

    @Override
    public void initAuto() {
        scan();
        setBackgroundTasks(mecanumLift::holdPosition);
        mecanumOuttake.midCap();
    }

    @Override
    public void define() {
        mecanumOuttake.midCap();
        addWaypoint(20,10,45);
        customSide(FieldSide.BLUE, () -> {
            addSetpoint(30,20,90);
            addAutoModule(automodules.OneDuckAutoBlue);
        }, FieldSide.RED, () -> {
            addSetpoint(30,20,180);
            addSetpoint(65,15,210);
            addAutoModule(automodules.OneDuckAutoRed);
        });
        customCase(Case.RIGHT, () -> {
            addConcurrentAutoModule(automodules.AllianceLiftUp(automodules.LiftUpTopFast));
            addSetpoint(-15,40,135);
            customSide(FieldSide.BLUE, () -> {
                addSetpoint(-28,55,135);
            }, FieldSide.RED, () -> {
                addSetpoint(-28,55,130);
            });
        }, Case.CENTER, () -> {
            addConcurrentAutoModule(automodules.AllianceLiftUp(automodules.LiftUpMiddleFast));
            addSetpoint(-15,40,135);
            addSetpoint(-35,60,135);
        }, Case.LEFT, () -> {
            addConcurrentAutoModule(automodules.AllianceLiftUp(automodules.LiftUpBottomFast));
            addSetpoint(-15,40,135);
            addSetpoint(-42,67,135);
        });
        addCancelAutoModules();
        addAutoModule(automodules.ResetLiftAndOuttake);
        customSide(FieldSide.BLUE, () -> {
            addSetpoint(28,62,90);
        }, FieldSide.RED, () -> {
            addSetpoint(30,67,90);
        });

    }


    @Autonomous(name = "TerraAutoDuckBlue", group = "auto")
    public static class TerraAutoDuckBlue extends TerraAutoDuck {{ fieldSide = FieldSide.BLUE; }}
    @Autonomous(name = "TerraAutoDuckRed", group = "auto")
    public static class TerraAutoDuckRed extends TerraAutoDuckBlue {{ fieldSide = FieldSide.RED; }}
}

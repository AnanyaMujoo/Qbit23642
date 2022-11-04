package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import elements.Case;
import elements.FieldSide;

public class TerraAutoDuck extends AutoFramework {


//
//    @Override
//    public Reactor getSetpointReactor() {
//        return new MecanumPIDReactor();
//    }
//
//    @Override
//    public Reactor getWaypointReactor() {
//        return new MecanumPurePursuitReactor();
//    }
//
//    @Override
//    public Generator getSetpointGenerator() {
//        return new PoseGenerator();
//    }
//
//    @Override
//    public Generator getWaypointGenerator() {
//        return new LineGenerator(lastPose);
//    }
//
//    @Override
//    public CaseScanner getCaseScanner() { return new TeamElementScanner(); }
//
//    @Override
//    public void initAuto() {
//        scan();
//        bot.addBackgroundTask(new BackgroundTask(mecanumLift::holdPosition));
//        mecanumOuttake.midCap();
//    }

    @Override
    public void define() {
        mecanumOuttake.midCap();
        addWaypoint(20,10,45);
        customSide(FieldSide.BLUE, () -> {
            addSetpoint(30,20,90);
            addAutoModule(OneDuckAutoBlue);
        }, FieldSide.RED, () -> {
            addSetpoint(30,20,180);
            addSetpoint(65,15,210);
            addAutoModule(OneDuckAutoRed);
        });
        customCase(Case.RIGHT, () -> {
            addConcurrentAutoModule(AllianceLiftUp(LiftUpTopFast));
            addSetpoint(-15,40,135);
            customSide(FieldSide.BLUE, () -> {
                addSetpoint(-28,55,135);
            }, FieldSide.RED, () -> {
                addSetpoint(-28,55,130);
            });
        }, Case.CENTER, () -> {
            addConcurrentAutoModule(AllianceLiftUp(LiftUpMiddleFast));
            addSetpoint(-15,40,135);
            addSetpoint(-35,60,135);
        }, Case.LEFT, () -> {
            addConcurrentAutoModule(AllianceLiftUp(LiftUpBottomFast));
            addSetpoint(-15,40,135);
            addSetpoint(-42,67,135);
        });
        addCancelAutoModules();
        addAutoModule(ResetLiftAndOuttake);
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
